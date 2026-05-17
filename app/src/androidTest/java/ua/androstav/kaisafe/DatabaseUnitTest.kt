package ua.androstav.kaisafe

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.androstav.kaisafe.data.AppDB
import ua.androstav.kaisafe.data.ContactDao
import ua.androstav.kaisafe.data.ContactEntity
import ua.androstav.kaisafe.data.Instruction
import ua.androstav.kaisafe.data.InstructionDao

@RunWith(AndroidJUnit4::class)
class DatabaseUnitTest {
    private lateinit var db: AppDB
    private lateinit var instructionDao: InstructionDao
    private lateinit var contactDao: ContactDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java)
            .allowMainThreadQueries()
            .build()
        instructionDao = db.instructionDao()
        contactDao = db.contactDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndReadInstructions() = runBlocking {
        // 1. Перевірка масового запису та зчитування
        val instructions = listOf(
            Instruction(title = "Пожежа", content = "Дії при пожежі..."),
            Instruction(title = "Тривога", content = "Дії при тривозі...")
        )
        instructionDao.insertAll(instructions)

        // Використовуємо метод getAllOnce()
        val loaded = instructionDao.getAllOnce()

        assertEquals(2, loaded.size)
        assertEquals("Пожежа", loaded[0].title)
    }

    @Test
    fun testReadEmptyTable() = runBlocking {
        // 2. Валідація зчитування з порожньої таблиці
        val loaded = contactDao.getAllOnce()
        assertTrue("Таблиця має бути порожньою", loaded.isEmpty())
    }

    @Test
    fun testContactsSorting() = runBlocking {
        // 3. Перевірка зчитування контактів
        val contact = ContactEntity(name = "Швидка допомога", phone = "103")

        // Оскільки у ContactDao є лише insertAll (List), передаємо список з 1 елементом
        contactDao.insertAll(listOf(contact))

        val loadedContacts = contactDao.getAllOnce()
        assertEquals(1, loadedContacts.size)
        assertEquals("103", loadedContacts[0].phone)
    }
}
