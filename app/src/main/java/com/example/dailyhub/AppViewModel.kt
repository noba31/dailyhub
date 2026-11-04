package com.example.dailyhub

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyhub.data.Repository
import com.example.dailyhub.data.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(app)

    /** null while loading from disk. */
    val tasks: StateFlow<List<Task>?> =
        repo.tasks.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /** null = follow system theme. */
    val darkMode: StateFlow<Boolean?> =
        repo.darkMode.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun addTask(title: String) {
        val t = title.trim()
        if (t.isEmpty()) return
        viewModelScope.launch {
            repo.updateTasks { it + Task(System.currentTimeMillis(), t, false) }
        }
    }

    fun toggleTask(id: Long) {
        viewModelScope.launch {
            repo.updateTasks { list -> list.map { if (it.id == id) it.copy(done = !it.done) else it } }
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repo.updateTasks { list -> list.filterNot { it.id == id } }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { repo.setDarkMode(enabled) }
    }
}
