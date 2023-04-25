package com.example.safety

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.databinding.ItemInviteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class inviteAdapter(private var context: Context,private val contactList: List<ContactModel>):
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

            CoroutineScope(Dispatchers.IO).launch {
                val dataBase = SafetyDataBase.getDataBase(context)
                val invName = dataBase.contactDao().getNumber(name.conName)
                Log.d("InvAdapter",invName.number)
                sendInvite(invName.number)
                Toast.makeText(context,"Invite SMS sent to $invName",Toast.LENGTH_SHORT).show()
            }
        }
    }

//C:\z__material\ANDROID\App Versions\Safety\app-debug(2).apk
    private fun sendInvite(number: String) {
        val intent = Intent(Intent(Intent.ACTION_SENDTO))
        intent.data = Uri.parse("sms:$number")
//        intent.type="application/vnd.android.package-archive"
//        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(app_debug.apk))
        intent.putExtra("sms_body","I invite you to Safety App")
        context.startActivity(intent)

    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}