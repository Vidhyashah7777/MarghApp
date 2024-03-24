package com.example.marghappclonepriyankaparmar.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.marghappclonepriyankaparmar.R
import com.example.marghappclonepriyankaparmar.apicall.models.Hit
import com.example.marghappclonepriyankaparmar.databinding.ItemImagesBinding
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageDao
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ImageViewAdapter(
    val context: Context, private val imageList: List<Hit>, var savedImageDao: SavedImageDao
) :
    RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>() {

    companion object {
        var imageUriCache = ""
    }

    class ImageViewHolder(val binding: ItemImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Hit) {
            binding.shareButton.setOnClickListener {
                shareImage(get.largeImageURL)
            }

            binding.downloadButton.setOnClickListener {
                downloadImage(get.largeImageURL)
                Toast.makeText(
                    itemView.context,
                    "Download completed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun downloadImage(imageUrl: String) {
            Glide.with(itemView.context)
                .asFile()
                .load(imageUrl)
                .into(object : CustomTarget<File>() {
                    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                        val downloadsDir =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        val file = File(downloadsDir, "image_${System.currentTimeMillis()}.jpg")
                        try {
                            val inputStream = resource.inputStream()
                            val outputStream = FileOutputStream(file)
                            inputStream.copyTo(outputStream)
                            inputStream.close()
                            outputStream.close()
                            scanFile(itemView.context, file)
                            Toast.makeText(itemView.context, "Image downloaded", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                itemView.context,
                                "Failed to download image",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }


        fun shareImage(imageUrl1: String) {
            val imageUrl = imageUrl1
            val requestOptions = RequestOptions().override(300) // Adjust size as needed
            Glide.with(itemView.context)
                .asBitmap()
                .load(imageUrl)
                .apply(requestOptions)
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(
                        resource: Bitmap,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
                            val cachePath = File(itemView.context.cacheDir, "images")
                            cachePath.mkdirs()
                            val imageFile = File(cachePath, "image.jpg")
                            FileOutputStream(imageFile).use { outputStream ->
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            }
                            val imageUri = FileProvider.getUriForFile(
                                itemView.context,
                                "${itemView.context.packageName}.fileprovider",
                                imageFile
                            )
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "image/*"
                                putExtra(Intent.EXTRA_STREAM, imageUri)
                            }
                            itemView.context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "Share Image"
                                )
                            )
                        }
                        return true
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .submit()
        }

        private fun scanFile(context: Context, file: File) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.path),
                arrayOf("image/*"),
                null
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageList[position].largeImageURL
        Glide.with(context)
            .load(imageUrl)
            .thumbnail(Glide.with(context).load(imageList[position].previewURL))
            .into(holder.binding.imageView)
        holder.bind(imageList[position])

        holder.binding.likeButton.setOnClickListener {
            imageList[position].isSaved = !imageList[position].isSaved
            holder.binding.likeButton.setImageResource(if (imageList[position].isSaved) R.drawable.ic_saved else R.drawable.ic_like)
            downloadImageInCache(context, imageList[position].largeImageURL, position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun downloadImageInCache(context: Context, imageUrl: String, adapterPosition: Int) {
        val imageUrl = imageUrl
        val requestOptions = RequestOptions().override(300)
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .apply(requestOptions)
            .listener(object : RequestListener<Bitmap> {
                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any,
                    target: Target<Bitmap>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource != null) {

                        val cachePath = File(context.cacheDir, "imagesCache")
                        if (!cachePath.exists())
                            cachePath.mkdirs()

                        val imageName = getImageNameFromUrl(imageUrl)

                        val imageFile =
                            File(cachePath, "$imageName")

                        if (imageFile.exists()) {
                            Toast.makeText(
                                context,
                                "this Image is Already exist",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            FileOutputStream(imageFile).use { outputStream ->
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            }
                            imageUriCache = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                imageFile
                            ).toString()
                            insertImageUrl(
                                imageList[adapterPosition].largeImageURL,
                                imageList[adapterPosition].isSaved,
                                imageUriCache
                            )
                        }
                        Log.d("cache file", "priyanka onResourceReady: $imageUriCache")
                    }
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .submit()
    }

    fun getImageNameFromUrl(url: String): String? {
        val lastSegment = url.substringAfterLast("/")
        val imageName = if (lastSegment.contains("?")) {
            lastSegment.substringBefore("?")
        } else {
            lastSegment
        }

        return imageName
    }

    fun insertImageUrl(imageUrl: String, isSaved: Boolean, imageCache: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedImage =
                SavedImageEntity(imageUrl = imageUrl, isLiked = isSaved, imageCache = imageCache)
            if (isSaved) {
                savedImageDao.insertSavedImage(savedImage)
            } else {
                savedImageDao.deleteSavedImage(imageUrl)
            }
        }
    }
}