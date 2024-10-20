package com.example.pr2v6.com.example.pr2v6

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pr2v6.MainActivity
import com.example.pr2v6.R

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        editTextLogin = findViewById(R.id.editTextTextEmailAddress)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        buttonLogin = findViewById(R.id.buttonLoginToMain)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Загружаем сохраненные данные, если они есть
        loadSavedCredentials()

        buttonLogin.setOnClickListener {
            val login = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()

            if (login.isNotEmpty() && password.isNotEmpty()) {
                // Сохраняем введенные данные
                saveCredentials(login, password)
                Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
            } else {
                var stroka: StringBuilder = StringBuilder("Введите ")
                if( login.isEmpty() ) {
                    stroka.append("логин")
                    if( password.isEmpty())
                        stroka.append(" и пароль")
                }
                else
                    stroka.append("пароль")

                Toast.makeText(this, stroka.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCredentials(login: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("LOGIN", login)
        editor.putString("PASSWORD", password)
        editor.apply()
    }

    private fun loadSavedCredentials() {
        val savedLogin = sharedPreferences.getString("LOGIN", null)
        val savedPassword = sharedPreferences.getString("PASSWORD", null)

        if (savedLogin != null && savedPassword != null) {
            editTextLogin.setText(savedLogin)
            editTextPassword.setText(savedPassword)
        }
    }
}