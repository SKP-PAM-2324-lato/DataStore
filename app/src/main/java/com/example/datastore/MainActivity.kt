package com.example.datastore

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val SAVED_TEXT_KEY = stringPreferencesKey("saved_text")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textField = findViewById<EditText>(R.id.edit_text)
        val saveButton = findViewById<Button>(R.id.save)
        val readButton = findViewById<Button>(R.id.read)

        saveButton.setOnClickListener {
            val textToSave = textField.text.toString()
            lifecycleScope.launch{
                saveData(textToSave)
            }
            textField.text.clear()
        }

        readButton.setOnClickListener {
            lifecycleScope.launch {
                val savedText = loadData()
                textField.setText(savedText)
            }
        }
    }

    private suspend fun saveData(text:String){
        applicationContext.dataStore.edit { preferences -> preferences[SAVED_TEXT_KEY] = text }
    }

    private suspend fun loadData(): String?{
        val preferences = applicationContext.dataStore.data.first()
        return preferences[SAVED_TEXT_KEY]
    }
}