package ua.androstav.kaisafe.data

import androidx.lifecycle.LiveData

class InstructionRepository(private val dao: InstructionDao) {

    val allInstructions: LiveData<List<Instruction>> = dao.getAll()

    suspend fun insertAll(instructions: List<Instruction>) {
        dao.insertAll(instructions)
    }
    suspend fun getAllOnce(): List<Instruction> {
        return dao.getAllOnce()
    }

}
