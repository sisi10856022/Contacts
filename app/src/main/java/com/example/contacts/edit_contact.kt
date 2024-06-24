package com.example.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.contacts.databinding.FragmentEditContactBinding

// 确保导入 Contact 类
import com.example.contacts.Contact

class EditContactFragment : Fragment() {

    private var _binding: FragmentEditContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactDatabaseHelper: ContactDatabaseHelper
    private var contactId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getInt(ARG_CONTACT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditContactBinding.inflate(inflater, container, false)
        val view = binding.root

        contactDatabaseHelper = ContactDatabaseHelper(requireContext())

        contactId?.let {
            val contact = contactDatabaseHelper.getContact(it)
            binding.editTextName.setText(contact?.name)
            binding.editTextPhone.setText(contact?.phone)
        }

        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val phone = binding.editTextPhone.text.toString()

            // 正则表达式检查电话格式
            if (phone.length != 10 || !phone.matches(Regex("\\d{10}"))) {
                showErrorDialog("電話號碼格式錯誤，請重新輸入")
                binding.editTextPhone.text.clear()
            } else {
                if (contactId == null) {
                    contactDatabaseHelper.addContact(Contact(name = name, phone = phone))
                } else {
                    contactDatabaseHelper.updateContact(Contact(id = contactId!!, name = name, phone = phone))
                }
                showSuccessDialog("儲存成功")
            }
        }

        return view
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("確定") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("確定") { dialog, _ ->
                dialog.dismiss()
                parentFragmentManager.popBackStack() // 返回到上一个 fragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, AllContactsFragment.newInstance())
                    .commit()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CONTACT_ID = "contact_id"

        @JvmStatic
        fun newInstance(contactId: Int) =
            EditContactFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CONTACT_ID, contactId)
                }
            }
    }
}
