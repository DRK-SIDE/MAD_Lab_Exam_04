package com.example.taskmaster

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDBHelper
    private lateinit var taskListAdapter: TaskAdapter
    private lateinit var taskListView: ListView
    private lateinit var editTextTask: EditText
    private lateinit var buttonAdd: Button
    private lateinit var loadingDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TaskDBHelper(this)

        taskListView = findViewById(R.id.taskListView)
        editTextTask = findViewById(R.id.editTextTask)
        buttonAdd = findViewById(R.id.buttonAdd)

        // Show loading dialog
        loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("Loading tasks...")
        loadingDialog.setCancelable(false)
        loadingDialog.show()

        // Simulate loading data or any background task
        Handler().postDelayed({
            // Dismiss loading dialog after 2 seconds (simulating data loading)
            loadingDialog.dismiss()

            // Load tasks from the database
            loadTasks()
        }, 2000) // Simulating a 2-second loading time

        buttonAdd.setOnClickListener {
            val taskName = editTextTask.text.toString().trim()
            if (taskName.isNotEmpty()) {
                val isInserted = dbHelper.addTask(taskName)
                if (isInserted) {
                    editTextTask.text.clear()
                    updateTaskList()
                } else {
                    Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }

        taskListView.setOnItemClickListener { parent, view, position, id ->
            val selectedTask = taskListAdapter.getItem(position)
            // Implement task selection logic (e.g., opening a detail view)
        }

        taskListView.setOnItemLongClickListener { parent, view, position, id ->
            val selectedTask = taskListAdapter.getItem(position)
            selectedTask?.let { deleteTask(it) }
            true
        }
    }

    private fun loadTasks() {
        taskListAdapter = TaskAdapter(this, dbHelper.getAllTasks().toMutableList())
        taskListView.adapter = taskListAdapter
    }

    private fun deleteTask(task: Task) {
        val isDeleted = dbHelper.deleteTask(task)
        if (isDeleted) {
            updateTaskList()
        } else {
            Toast.makeText(this, "Failed to delete task", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTaskList() {
        taskListAdapter.clear()
        taskListAdapter.addAll(dbHelper.getAllTasks())
    }
}
