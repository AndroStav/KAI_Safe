package ua.androstav.kaisafe.data

import androidx.lifecycle.LiveData

class ContactRepository(private val dao: ContactDao) {
    val allContacts: LiveData<List<ContactEntity>> = dao.getAll()

    suspend fun insertAll(contacts: List<ContactEntity>) {
        dao.insertAll(contacts)
    }

    suspend fun getAllOnce(): List<ContactEntity> = dao.getAllOnce()

    suspend fun update(contact: ContactEntity) {
        dao.update(contact)
    }

    suspend fun delete(contact: ContactEntity) {
        dao.delete(contact)
    }

}
