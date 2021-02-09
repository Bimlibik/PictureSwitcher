package ru.bimlibik.pictureswitcher.ui.favorite_pictures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.databinding.ItemFavoritePictureBinding

class FavoritePicturesAdapter(private val viewModel: FavoritePicturesViewModel) :
    ListAdapter<Picture, FavoritePicturesAdapter.PictureViewHolder>(PictureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding = ItemFavoritePictureBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(position)
    }


    /**
     * ViewHolder for picture
     */
    inner class PictureViewHolder(private val binding: ItemFavoritePictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position)
            binding.item = item
            binding.viewModel = viewModel
        }
    }

}

/**
 * DiffUtil
 */
class PictureDiffCallback : DiffUtil.ItemCallback<Picture>() {
    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem == newItem
    }
}