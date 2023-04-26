package com.example.safety

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safety.databinding.FragmentInviteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InviteFragment : Fragment() {

    private var contactsFetched : ArrayList<ContactModel> = ArrayList()
    private lateinit var inviteAdapter: inviteAdapter
    private lateinit var binding: FragmentInviteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInviteBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        Log.v("ProfileFragment :"," 1")
        inviteAdapter= inviteAdapter(requireContext(),fetchContacts())
        fetchContactsFromDatabase()
        Log.v("ProfileFragment :"," 2")


        lifecycleScope.launch {
            Log.v("ProfileFragment :"," 3")
            insertContactsInDatabase(fetchContacts())
            Log.v("ProfileFragment :"," 4")
        }
        binding.rvHomeHorz.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeHorz.adapter = inviteAdapter

        }

    private fun showProgressbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun fetchContactsFromDatabase() {
        val dataBase= SafetyDataBase.getDataBase(requireContext())
        dataBase.contactDao().getAllData().observe(viewLifecycleOwner){
            contactsFetched.clear()
            contactsFetched.addAll(it)
            inviteAdapter.notifyDataSetChanged()
        }
        hideProgressbar()
    }

    private fun hideProgressbar() {
        binding.progressBar.visibility = View.GONE
    }

    private suspend fun insertContactsInDatabase(Contacts: java.util.ArrayList<ContactModel>) {
        val dataBase = SafetyDataBase.getDataBase(requireContext())
        dataBase.contactDao().insertAll(Contacts)
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

            showProgressbar()

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
        fun newInstance() = InviteFragment()
    }
}