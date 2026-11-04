package com.example.dailyhub.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "daily_hub")

data class Task(val id: Long, val title: String, val done: Boolean)

class Repository(private val context: Context) {
    private val tasksKey = stringPreferencesKey("tasks")
    private val darkKey = booleanPreferencesKey("dark_mode")

    /** Saved tasks; survive app restarts. */
    val tasks: Flow<List<Task>> = context.dataStore.data.map { decode(it[tasksKey]) }

    /** null = follow system setting. */
    val darkMode: Flow<Boolean?> = context.dataStore.data.map { it[darkKey] }

    suspend fun updateTasks(transform: (List<Task>) -> List<Task>) {
        context.dataStore.edit { prefs ->
            prefs[tasksKey] = encode(transform(decode(prefs[tasksKey])))
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[darkKey] = enabled }
    }

    private fun encode(list: List<Task>): String {
        val arr = JSONArray()
        list.forEach {
            arr.put(JSONObject().put("id", it.id).put("title", it.title).put("done", it.done))
        }
        return arr.toString()
    }

    private fun decode(json: String?): List<Task> {
        if (json.isNullOrEmpty()) return emptyList()
        return try {
            val arr = JSONArray(json)
            List(arr.length()) {
                val o = arr.getJSONObject(it)
                Task(o.getLong("id"), o.getString("title"), o.getBoolean("done"))
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
