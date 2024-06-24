package com.example.contacts

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_CONTACTS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_PHONE TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addContact(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE, contact.phone)
        }
        db.insert(TABLE_CONTACTS, null, values)
    }

    fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val phone = getString(getColumnIndexOrThrow(COLUMN_PHONE))
                contacts.add(Contact(id, name, phone))
            }
            close()
        }
        return contacts
    }

    fun getContact(id: Int): Contact? {
        val db = readableDatabase
        val cursor = db.query(TABLE_CONTACTS, null, "$COLUMN_ID = ?", arrayOf(id.toString()), null, null, null)
        with(cursor) {
            if (moveToFirst()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val phone = getString(getColumnIndexOrThrow(COLUMN_PHONE))
                close()
                return Contact(id, name, phone)
            }
            close()
        }
        return null
    }

    fun updateContact(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE, contact.phone)
        }
        db.update(TABLE_CONTACTS, values, "$COLUMN_ID = ?", arrayOf(contact.id.toString()))
    }

    fun deleteContact(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_CONTACTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}