package ua.androstav.nausafe

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.androstav.nausafe.data.ContactEntity
import ua.androstav.nausafe.data.Instruction
import ua.androstav.nausafe.ui.contacts.ContactsAdapter
import ua.androstav.nausafe.ui.home.InstructionAdapter

class AppUnitTests {

    @Test
    fun contactEntity_data_isCorrect() {
        val contact = ContactEntity(name = "Швидка", phone = "103")
        assertEquals("Швидка", contact.name)
        assertEquals("103", contact.phone)
    }

    @Test
    fun instructionEntity_data_isCorrect() {
        val instr = Instruction(title = "Тривога", content = "Що робити...")
        assertEquals("Тривога", instr.title)
        assertEquals("Що робити...", instr.content)
    }

    @Test
    fun contactEntity_DefaultId_IsZero() {
        val contact = ContactEntity(name = "Пожежна", phone = "101")
        assertEquals("ID за замовчуванням має бути 0", 0, contact.id)
    }
}
