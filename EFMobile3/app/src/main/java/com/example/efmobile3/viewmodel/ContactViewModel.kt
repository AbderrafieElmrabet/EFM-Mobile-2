package com.example.efmobile3.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.efmobile3.model.Contact

class ContactViewModel {
    private var nextId = 1
    private val _contacts = mutableStateListOf<Contact>()
    val contacts: List<Contact> get() = _contacts

    fun addContact(name: String, status: String, priority: String) {
        val contact = Contact(
            id = nextId++,
            name = name,
            status = status,
            priority = priority
        )
        _contacts.add(contact)
    }
    fun updateContact(updatedContact: Contact) {
        val index = _contacts.indexOfFirst { it.id == updatedContact.id }
        if (index != -1 ) _contacts[index] = updatedContact
    }
    fun deleteContact(contact: Contact){
        _contacts.remove(contact)
    }
}