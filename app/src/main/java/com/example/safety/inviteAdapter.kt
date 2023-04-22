package com.example.safety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.databinding.ItemInviteBinding
import com.google.android.material.button.MaterialButton

class inviteAdapter(private val contactList : List<ContactModel>):
    RecyclerView.Adapter<inviteAdapter.inviteHolder>() {

    inner class inviteHolder(var binding: ItemInviteBinding ) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): inviteHolder {
        val binding = ItemInviteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return inviteHolder(binding)
    }

    override fun onBindViewHolder(holder: inviteHolder, position: Int) {
        val name = contactList[position]
        holder.binding.tVInvite.text = name.conName
        holder.binding.inviteBtn.setOnClickListener{
            sendInvite()
        }
    }

    private fun sendInvite(){

    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}