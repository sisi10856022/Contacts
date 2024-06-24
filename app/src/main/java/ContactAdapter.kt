package com.example.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onActionClick: (Contact, String) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int = contacts.size

    fun updateContacts(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.text_view_name)
        private val textViewPhone: TextView = itemView.findViewById(R.id.text_view_phone)
        private val buttonCall: ImageButton = itemView.findViewById(R.id.button_call)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.button_delete)

        fun bind(contact: Contact) {
            textViewName.text = contact.name
            textViewPhone.text = contact.phone

            buttonCall.setOnClickListener {
                onActionClick(contact, "call")
            }

            buttonDelete.setOnClickListener {
                onActionClick(contact, "delete")
            }

            itemView.setOnClickListener {
                onActionClick(contact, "edit")
            }
        }
    }
}