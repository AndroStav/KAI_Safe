package ua.androstav.kaisafe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.androstav.kaisafe.R
import ua.androstav.kaisafe.utils.FileLogger

class HomeFragment : Fragment() {

    private val viewModel: InstructionViewModel by viewModels()
    private lateinit var adapter: InstructionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerInstructions)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = InstructionAdapter(emptyList())
        recycler.adapter = adapter

        viewModel.allInstructions.observe(viewLifecycleOwner) { instructions ->
            adapter.updateData(instructions)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Логування перегляду екрану
        context?.let {
            FileLogger.log(it, "SCREEN", "Home Fragment (Instructions) Viewed")
        }
    }
}