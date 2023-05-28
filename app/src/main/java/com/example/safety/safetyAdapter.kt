package com.example.safety

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.databinding.ModelBinding

class safetyAdapter(private val memberList: List<Model>) :
    RecyclerView.Adapter<safetyAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ModelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ModelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mem = memberList[position]
        holder.binding.nameModel.text = mem.name
        holder.binding.addModel.text = "Latitude: ${mem.lat}\nLongitude: ${mem.long}"
        holder.binding.batPerModel.text = "${mem.batPer}%"
        holder.binding.connectionTvModel.text = "${mem.connectionInfo}"
        holder.binding.callImgModel.setOnClickListener {

            val phoneNum = mem.phoneNumber
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNum")
            holder.itemView.context.startActivity(intent)

        }


        holder.itemView.setOnClickListener {

            val latitude = mem.lat.toDouble()
            val longitude = mem.long.toDouble()
            val fragment = UserLocationFragment.newInstance(latitude, longitude)

            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}