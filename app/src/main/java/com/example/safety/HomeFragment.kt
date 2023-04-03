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
        Model("Pro")
    )

    val adapter = safetyAdapter(memberList)

    val recycler = requireView().findViewById<RecyclerView>(R.id.rvHome)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
