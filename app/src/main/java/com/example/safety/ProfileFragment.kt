package com.example.safety

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.safety.databinding.FragmentInviteBinding
import com.example.safety.databinding.FragmentProfileBinding
import com.example.safety.databinding.ItemViewShimmerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email.toString()

        val dbProfile = Firebase.firestore


        dbProfile.collection("User Data")
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Extract the first name from the document data and set it as the text of the profile name TextView
                    val firstName = document.getString("firstName")
                    binding.profileName.text = firstName
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Profile name ", "Error getting user document: ", exception)
            }

        binding.cvInviteContacts.setOnClickListener {
            val trancs = parentFragmentManager.beginTransaction()
            trancs.replace(R.id.container, InviteFragment())
                .addToBackStack(null)
                .commit()
        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()

    }
}
