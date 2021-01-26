package ru.bimlibik.pictureswitcher.ui.pictures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.databinding.ItemPictureBinding

class PicturesAdapter(private val viewModel: PicturesViewModel)
    : ListAdapter<Picture, PicturesAdapter.PictureViewHolder>(PictureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        if (position >= itemCount - 1) {
            viewModel.loadMore()
        }
        holder.bind(position)
    }

    /**
     * ViewHolder for picture
     */
    inner class PictureViewHolder(private val binding: ItemPictureBinding) :
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