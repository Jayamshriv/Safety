package com.example.safety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class safetyAdapter(private val memberList: List<Model>) : RecyclerView.Adapter<safetyAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.nameModel)
        val img = itemView.findViewById<ImageView>(R.id.imgModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.model,parent,false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mem = memberList[position]
        holder.name.text = mem.name
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}