package com.example.safety


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safety.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val db = Firebase.firestore
        Log.v("FetchContacts", "1")

        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        Log.v("FetchContacts", "2")


        db.collection("User Data")
            .get()
            .addOnSuccessListener { result ->

                val usersList = mutableListOf<Model>()
                for (usersInfo in result.documents) {
                    val user = usersInfo.toObject(users::class.java)
                    if (!result.isEmpty) {
                        val model = Model(user?.firstName.orEmpty())
                        usersList.add(model)
                    }
                    val adapter = safetyAdapter(usersList)
                    binding.rvHome.adapter = adapter
                    binding.rvHome.adapter?.notifyDataSetChanged()
                }

            }
            .addOnFailureListener { exception ->
                Log.e("FireStoreData", "Error getting documents: ", exception)
                Toast.makeText(
                    requireContext(),
                    "Error while retrieving data from firestore",
                    Toast.LENGTH_SHORT
                ).show()
            }





    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
