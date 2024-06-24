package com.example.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllContactsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var contactDatabaseHelper: ContactDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_contacts, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        contactDatabaseHelper = ContactDatabaseHelper(requireContext())
        adapter = ContactAdapter(contactDatabaseHelper.getAllContacts()) { contact, action ->
            when (action) {
                "edit" -> {
                    val fragment = EditContactFragment.newInstance(contact.id)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                "delete" -> {
                    showDeleteConfirmationDialog(contact)
                }
                "call" -> {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                    startActivity(intent)
                }
            }
        }
        recyclerView.adapter = adapter

        return view
    }

    private fun showDeleteConfirmationDialog(contact: Contact) {
        AlertDialog.Builder(requireContext())
            .setMessage("確定刪除此聯絡人?")
            .setPositiveButton("Yes") { _, _ ->
                contactDatabaseHelper.deleteContact(contact.id)
                adapter.updateContacts(contactDatabaseHelper.getAllContacts())
            }
            .setNegativeButton("No", null)
            .show()
    }

    companion object {
        fun newInstance(): AllContactsFragment {
            return AllContactsFragment()
        }
    }
}