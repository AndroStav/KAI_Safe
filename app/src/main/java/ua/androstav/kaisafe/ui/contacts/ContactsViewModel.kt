package ua.androstav.kaisafe.ui.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.androstav.kaisafe.data.AppDB
import ua.androstav.kaisafe.data.ContactEntity
import ua.androstav.kaisafe.data.ContactRepository

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    val allContacts: LiveData<List<ContactEntity>>

    init {
        val dao = AppDB.getDatabase(application).contactDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts

        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getAllOnce().isEmpty()) {
                val defaultContacts = listOf(
                    ContactEntity(name = "Пожежна служба", phone = "101"),
                    ContactEntity(name = "Поліція", phone = "102"),
                    ContactEntity(name = "Швидка допомога", phone = "103")
                )
                repository.insertAll(defaultContacts)
            }
        }
    }

    fun addContact(name: String, phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newContact = ContactEntity(name = name, phone = phone)
            // Використовуємо існуючий метод insertAll для збереження одного запису
            repository.insertAll(listOf(newContact))
        }
    }

    fun updateContact(contact: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(contact)
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(contact)
        }
    }
}