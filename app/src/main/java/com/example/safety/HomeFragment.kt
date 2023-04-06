package com.example.safety

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    val memberList = listOf<Model>(
        Model("loki"),
        Model("thor"),
        Model("odin"),
        Model("Jane"),
        Model("Prof")
    )

    val adapter = safetyAdapter(memberList)

    val recycler = requireView().findViewById<RecyclerView>(R.id.rvHome)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter


    /*
    * Invite RecyclerView
    */

    val contactList = listOf<itemsInvite>(
        itemsInvite("Iron Man",456788765),
        itemsInvite("Hulk",456787636),
        itemsInvite("Captain America james",98765432)
    )
    val invAdapter = inviteAdapter(contactList)
    val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.rvHomeHorz)
    inviteRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        inviteRecycler.adapter = invAdapter

    }



    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
