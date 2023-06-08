package com.example.safety


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safety.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.ConnectException

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var mapFragment : UserLocationFragment
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
                        val name = user?.fullName.orEmpty()
                        val latitude = user?.lat.orEmpty()
                        val longitude = user?.long.orEmpty()
                        val phoneNumber = user?.phoneNumber.orEmpty()
                        val batPer = user!!.batPer
                        val connectionInfo = user.connectionInfo.orEmpty()

                        val model = Model(name,phoneNumber,batPer,latitude,longitude,connectionInfo)
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


        binding.mapIcon.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container,MapsFragment())
                .addToBackStack(null)
                .commit()
        }


        binding.dotIcon.setOnClickListener {
            val popMenu = PopupMenu(it.context,it)
            popMenu.menuInflater.inflate(R.menu.home_menu,popMenu.menu)

            popMenu.setOnMenuItemClickListener {

                when(it.itemId){
                    R.id.settings ->{
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container,ProfileFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    else -> {false}
                }
            }
            popMenu.show()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
