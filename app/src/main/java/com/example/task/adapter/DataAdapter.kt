package com.example.task.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task.R
import com.example.task.local.Data

class DataAdapter : PagingDataAdapter<Data, DataAdapter.DataViewHolder>(
    differCallback
) {

    inner class DataViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.imageViewIcon)
        val textTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val imageViewDelete: ImageView = itemView.findViewById(R.id.imageViewDelete)
        val imageViewShare: ImageView = itemView.findViewById(R.id.imageViewShare)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_view_data,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = getItem(position)!!
        holder.textTitle.text = data.title
        holder.textDescription.text = data.description
        holder.itemView.apply {
            Glide.with(this).load(data.image).into(holder.imageView)
            setOnClickListener {
                onItemClickListener?.let { it(data) }
            }
        }
        holder.imageViewDelete.apply {
            setOnClickListener {
                onDeleteClickListener?.let { it(data) }
            }
        }
        holder.imageViewShare.apply {
            setOnClickListener {
                onShareClickListener?.let { it(data) }
            }
        }
    }

//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }

    private var onItemClickListener: ((Data) -> Unit)? = null

    private var onDeleteClickListener : ((Data) -> Unit)? = null

    private var onShareClickListener : ((Data) -> Unit)? = null

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnDeleteClick(listener: (Data) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnShareClick(listener: (Data) -> Unit) {
        onShareClickListener = listener
    }
}