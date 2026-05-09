package ua.androstav.kaisafe.ui.contacts

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ua.androstav.kaisafe.databinding.FragmentContactsBinding
import ua.androstav.kaisafe.data.ContactEntity
import android.text.InputFilter

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)

        binding.recyclerContacts.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            // Передаємо функцію showEditDeleteDialog при кліку на контакт
            binding.recyclerContacts.adapter = ContactsAdapter(contacts) { selectedContact ->
                showEditDeleteDialog(selectedContact)
            }
        }
        // Приховування кнопки "+" при прокрутці
        binding.recyclerContacts.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Якщо прокручуємо вниз (dy > 0) і кнопка видима -> ховаємо її
                if (dy > 0 && binding.fabAddContact.isShown) {
                    binding.fabAddContact.hide()
                }
                // Якщо прокручуємо вгору (dy < 0) і кнопка схована -> показуємо її
                else if (dy < 0 && !binding.fabAddContact.isShown) {
                    binding.fabAddContact.show()
                }
            }
        })

        // Обробник натискання на кнопку "+"
        binding.fabAddContact.setOnClickListener {
            showAddContactDialog()
        }

        return binding.root
    }

    private fun showAddContactDialog() {
        val context = requireContext()

        // Створюємо макет для полів вводу
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val nameInput = EditText(context).apply {
            hint = "Ім'я контакту (до 50 символів)"
            maxLines = 1
            filters = arrayOf(InputFilter.LengthFilter(50)) // Жорсткий ліміт на 50 символів
        }

        val phoneInput = EditText(context).apply {
            hint = "Номер телефону"
            inputType = InputType.TYPE_CLASS_PHONE
            maxLines = 1
            filters = arrayOf(InputFilter.LengthFilter(20)) // Жорсткий ліміт на 20 символів
        }

        layout.addView(nameInput)
        layout.addView(phoneInput)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Додати новий контакт")
            .setView(layout)
            .setPositiveButton("Зберегти", null) // null потрібен, щоб вікно не закривалось автоматично при помилці
            .setNegativeButton("Скасувати") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = nameInput.text.toString().trim()
                val phone = phoneInput.text.toString().trim()

                // === ВАЛІДАЦІЯ (ДЛЯ НЕГАТИВНИХ ТЕСТ-КЕЙСІВ) ===
                if (name.isEmpty()) {
                    nameInput.error = "Поле ім'я не може бути порожнім"
                    return@setOnClickListener
                }
                if (name.length > 50) {
                    nameInput.error = "Ім'я занадто довге (макс. 50 символів)"
                    return@setOnClickListener
                }
                if (phone.length > 20) {
                    phoneInput.error = "Номер занадто довгий (макс. 20 символів)"
                    return@setOnClickListener
                }
                if (phone.isEmpty()) {
                    phoneInput.error = "Поле телефону не може бути порожнім"
                    return@setOnClickListener
                }
                // Перевірка: номер має містити лише цифри, '+', дужки або пробіли
                if (!phone.matches("^[0-9+() -]+$".toRegex())) {
                    phoneInput.error = "Номер містить недопустимі літери/символи"
                    return@setOnClickListener
                }

                // Якщо валідація пройдена - зберігаємо
                viewModel.addContact(name, phone)
                Toast.makeText(context, "Контакт успішно збережено", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showEditDeleteDialog(contact: ContactEntity) {
        val context = requireContext()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val nameInput = EditText(context).apply {
            setText(contact.name) // Підставляємо існуюче ім'я
            maxLines = 1
            filters = arrayOf(android.text.InputFilter.LengthFilter(50))
        }

        val phoneInput = EditText(context).apply {
            setText(contact.phone) // Підставляємо існуючий телефон
            inputType = InputType.TYPE_CLASS_PHONE
            maxLines = 1
            filters = arrayOf(android.text.InputFilter.LengthFilter(20))
        }

        layout.addView(nameInput)
        layout.addView(phoneInput)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Редагувати контакт")
            .setView(layout)
            .setPositiveButton("Зберегти", null)
            .setNeutralButton("Видалити") { _, _ ->
                // Видаляємо контакт
                viewModel.deleteContact(contact)
                Toast.makeText(context, "Контакт видалено", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Скасувати") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val newName = nameInput.text.toString().trim()
                val newPhone = phoneInput.text.toString().trim()

                // === ВАЛІДАЦІЯ (ЯК У ВІКНІ ДОДАВАННЯ) ===
                if (newName.isEmpty()) {
                    nameInput.error = "Поле ім'я не може бути порожнім"
                    return@setOnClickListener
                }
                if (newName.length > 50) {
                    nameInput.error = "Ім'я занадто довге (макс. 50 символів)"
                    return@setOnClickListener
                }
                if (newPhone.isEmpty()) {
                    phoneInput.error = "Поле телефону не може бути порожнім"
                    return@setOnClickListener
                }
                if (newPhone.length > 20) {
                    phoneInput.error = "Номер занадто довгий (макс. 20 символів)"
                    return@setOnClickListener
                }
                if (!newPhone.matches("^[0-9+() -]+$".toRegex())) {
                    phoneInput.error = "Номер містить недопустимі літери/символи"
                    return@setOnClickListener
                }

                // Створюємо оновлену копію контакту (id залишається той самий!) і зберігаємо
                val updatedContact = contact.copy(name = newName, phone = newPhone)
                viewModel.updateContact(updatedContact)

                Toast.makeText(context, "Контакт оновлено", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}