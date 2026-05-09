package ua.androstav.kaisafe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ua.androstav.kaisafe.R
import ua.androstav.kaisafe.data.Instruction

class InstructionAdapter(
    private var instructions: List<Instruction>
) : RecyclerView.Adapter<InstructionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_instruction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instruction = instructions[position]
        holder.title.text = instruction.title

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", instruction.title)
                putString("content", instruction.content)
            }
            it.findNavController().navigate(
                R.id.action_navigation_home_to_instructionDetailFragment,
                bundle
            )
        }
    }

    override fun getItemCount() = instructions.size

    fun updateData(newInstructions: List<Instruction>) {
        instructions = newInstructions
        notifyDataSetChanged()
    }
}
