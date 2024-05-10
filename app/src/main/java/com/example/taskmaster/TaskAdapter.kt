package com.example.taskmaster

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class TaskAdapter(private val context: Context, private val tasks: MutableList<Task>) : ArrayAdapter<Task>(context, R.layout.activity_task_item, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_task_item, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.textViewTaskName),
                view.findViewById(R.id.buttonEdit),
                view.findViewById(R.id.buttonDelete)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val task = getItem(position)
        viewHolder.taskNameTextView.text = task?.taskName
        // Set task date and time

        viewHolder.editButton.setOnClickListener {
            showEditTaskDialog(task!!)
        }

        viewHolder.deleteButton.setOnClickListener {
            deleteTask(task!!)
        }

        return view!!
    }

    private fun showEditTaskDialog(task: Task) {
        val editTaskDialog = AlertDialog.Builder(context)
        editTaskDialog.setTitle("Edit Task")
        val input = EditText(context)
        input.setText(task.taskName)
        editTaskDialog.setView(input)

        editTaskDialog.setPositiveButton("OK") { dialog, _ ->
            val editedTaskName = input.text.toString().trim()
            if (editedTaskName.isNotEmpty()) {
                editTask(task, editedTaskName)
            } else {
                // Handle empty task name
            }
            dialog.dismiss()
        }

        editTaskDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        editTaskDialog.show()
    }

    private fun editTask(task: Task, editedTaskName: String) {
        task.taskName = editedTaskName
        notifyDataSetChanged() // Notify adapter about data change
    }

    private fun deleteTask(task: Task) {
        tasks.remove(task)
        notifyDataSetChanged() // Notify adapter about data change
    }

    private class ViewHolder(
        val taskNameTextView: TextView,
        val editButton: Button,
        val deleteButton: Button
    )
}
