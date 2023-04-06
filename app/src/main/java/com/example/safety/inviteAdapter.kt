package com.example.safety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class inviteAdapter(private val contactList : List<ContactModel>):
    RecyclerView.Adapter<inviteAdapter.inviteHolder>() {

    class inviteHolder(private val itemView: View ) :
        RecyclerView.ViewHolder(itemView) {
            val conName = itemView.findViewById<TextView>(R.id.tVInvite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): inviteHolder {
        return inviteHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_invite,parent,false))
    }

    override fun onBindViewHolder(holder: inviteHolder, position: Int) {
        val name = contactList[position]
        holder.conName.text = name.conName
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}