package com.example.marghappclonepriyankaparmar.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Environment
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
import com.example.marghappclonepriyankaparmar.databinding.ItemSavedImagesBinding
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageDao
import com.example.marghappclonepriyankaparmar.roomDatabase.SavedImageEntity
import java.io.File
import java.io.FileOutputStream

class SavedImageAdapter(val context: Context, private var imageList: List<SavedImageEntity>, private val imageDao: SavedImageDao
,val onDeleteClickListener: OnDeleteClickListener) : RecyclerView.Adapter<SavedImageAdapter.SavedImageViewHolder>() {

    fun submitList(newData: List<SavedImageEntity>) {
        imageList = newData
        notifyDataSetChanged()
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(item : SavedImageEntity)
    }

    class SavedImageViewHolder(val binding: ItemSavedImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SavedImageEntity) {
            binding.downloadButton.setOnClickListener {
                downloadImage(item.imageUrl)
            }
            binding.shareButton.setOnClickListener {
               shareImage(item.imageUrl)
            }
        }

        fun scanFile(context: Context, file: File) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.path),
                arrayOf("image/*"),
                null
            )
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding = ItemSavedImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        val imageUrl = imageList[position].imageUrl
        Glide.with(context)
            .load(imageUrl)
            .into(holder.binding.ivImage)
        holder.bind(imageList[position])

        holder.binding.deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(imageList[position])
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}