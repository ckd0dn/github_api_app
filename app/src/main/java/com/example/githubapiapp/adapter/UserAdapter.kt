package com.example.githubapiapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapiapp.databinding.ItemUserBinding
import com.example.githubapiapp.model.User

class UserAdapter(val onClick: (User) -> Unit) : ListAdapter<User, UserAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val viewBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(viewBinding.root){

            //뷰와 아이템을 바인딩
            fun bind(item: User) {
                viewBinding.usernameTextView.text = item.username
                //클릭기능
                viewBinding.root.setOnClickListener{
                    onClick(item)
                }
            }

        }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }

    //생성시 뷰홀더 넘김
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    //현재 포지션반환
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


}