package com.example.skainet_android.user.userList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skainet_android.R
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.User
import com.example.skainet_android.user.userEdit.UserEditFragment

class UserListAdapter(
    private val fragment: Fragment,
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    var users = emptyList<User>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener = View.OnClickListener { view ->
        val user = view.tag as User
        fragment.findNavController().navigate(R.id.UserEditFragment, Bundle().apply {
            putString(UserEditFragment.USER_ID, user.id)
        })
    };

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val user = users[position]
        holder.textView.text = user.name
        holder.itemView.tag = user
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = users.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text)
    }
}
