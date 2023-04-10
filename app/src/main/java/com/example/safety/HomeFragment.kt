package com.example.safety


import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    lateinit var invAdapter: inviteAdapter
    private val fetchedContacts: ArrayList<ContactModel> = ArrayList()
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
        Log.v("FetchContacts","1")

        val adapter = safetyAdapter(memberList)

        Log.v("FetchContacts","2")
        val recycler = requireView().findViewById<RecyclerView>(R.id.rvHome)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        Log.v("FetchContacts","3")
        /*
        * Invite RecyclerView
        */

        Log.v("FetchContacts","4")
        invAdapter= inviteAdapter(fetchContacts())
        Log.v("FetchContacts","5")
        fetchDatabaseContacts()

        /*
        * Coroutine
        */
        Log.v("FetchContacts","6")


//        lifecycleScope.launch{
//            Log.v("Fetch Contacts","7")
//            insertDatabaseContacts(fetchContacts())
//            Log.v("FetchContacts","8")
//        }
        CoroutineScope(Dispatchers.IO).launch {
        Log.v("Fetch Contacts","7")
            insertDatabaseContacts(fetchContacts())
            Log.v("FetchContacts","8")
        }
        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.rvHomeHorz)
        inviteRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inviteRecycler.adapter = invAdapter

        Log.v("FetchContacts","9")
    }

    private fun fetchDatabaseContacts() {
        Log.v("FetchContacts","10")
        val database = SafetyDataBase.getDataBase(requireContext())
        Log.v("FetchContacts","11")
        database.contactDao().getAllData().observe(viewLifecycleOwner) {
            fetchedContacts.clear()
            fetchedContacts.addAll(it)
            Log.v("FetchContacts","12")
            invAdapter.notifyDataSetChanged()
            Log.v("Fetch Contacts","13")
        }
    }

    private suspend fun insertDatabaseContacts(contactList: List<ContactModel>) {
        Log.v("FetchContacts","14")
        val database = SafetyDataBase.getDataBase(requireContext())

        database.contactDao().insertAll(contactList)
        Log.v("FetchContacts","15")
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
                cursor.getColumnIndex("Name")
                val id =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
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
            if(cursor != null){
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
