package ua.androstav.kaisafe.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.androstav.kaisafe.data.Instruction
import ua.androstav.kaisafe.data.AppDB
import ua.androstav.kaisafe.data.InstructionRepository

class InstructionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InstructionRepository
    val allInstructions: LiveData<List<Instruction>>

    init {
        val dao = AppDB.getDatabase(application).instructionDao()
        repository = InstructionRepository(dao)
        allInstructions = repository.allInstructions

        // Попереднє заповнення бази (лише при першому запуску)
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getAllOnce().isEmpty()) {
                val defaultInstructions = listOf(
                    Instruction(title = "Тривога", content =
                                "1. Візьми документи, телефон, воду, при можливості - аптечку та зарядний пристрій.\n" +
                                "2. Без паніки прямуй до найближчого укриття.\n" +
                                "3. У дорозі дотримуйся спокою, не біжи, не створюй натовп.\n" +
                                "4. Допоможи тим, хто поруч (людям з інвалідністю, викладачам).\n" +
                                "5. Перебувай в укритті до офіційного сигналу «Відбій тривоги».\n" +
                                "6. Не виходь назовні без дозволу адміністрації або офіційного повідомлення."),

                    Instruction(title = "Пожежа", content =
                                "1. Негайно повідом охорону або виклич пожежну службу.\n" +
                                "2. Попередь інших і евакуюйся за вказівниками «Вихід».\n" +
                                "3. Не користуйся ліфтом.\n" +
                                "4. Якщо є дим — рухайся повзучи або пригнувшись, закривши рот тканиною."),

                    Instruction(title = "Перша допомога", content =
                                "1. Виклич швидку допомогу або звернися до медпункту КАІ.\n" +
                                "2. Якщо поряд є постраждалий — перевір дихання, поклади його в стабільне положення.\n" +
                                "3. Не залишай людину саму до прибуття допомоги.")
                )
                repository.insertAll(defaultInstructions)
            }
        }
    }
}
