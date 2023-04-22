package com.example.safety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.databinding.ModelBinding

class safetyAdapter(private val memberList: List<Model>) : RecyclerView.Adapter<safetyAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ModelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ModelBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mem = memberList[position]
        holder.binding.nameModel.text = mem.name
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}