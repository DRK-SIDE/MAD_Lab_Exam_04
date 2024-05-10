package com.example.taskmaster


data class Task(
    val id: Int,
    private var _taskName: String
) {
    var taskName: String
        get() = _taskName
        set(value) {
            _taskName = value
        }
}


