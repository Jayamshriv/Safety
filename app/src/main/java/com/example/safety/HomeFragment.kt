package com.example.safety

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
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


        val invAdapter = inviteAdapter(fetchContacts())
        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.rvHomeHorz)
        inviteRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inviteRecycler.adapter = invAdapter


    }


    private fun fetchContacts(): ArrayList<ContactModel> {

        val listNumber: ArrayList<ContactModel> = ArrayList()
        val cr = requireActivity().contentResolver
        val cursor = cr.query(/* uri = */ ContactsContract.Contacts.CONTENT_URI,/* projection = */
            null,/* selection = */
            null,/* selectionArgs = */
            null,
            /* sortOrder = */
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        if ((cursor != null) && (cursor.count > 0)) {

            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val num =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (num > 0) {

                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        ""
                    )

                    if (pCur != null && pCur.count > 0) {
                        while (pCur.moveToNext()) {
                            val number =
                                pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listNumber.add(ContactModel(name, number))
                        }
                        pCur.close()
                    }
                }


            }
            if (cursor != null) {
                cursor.close()
            }
        }
        return listNumber
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
