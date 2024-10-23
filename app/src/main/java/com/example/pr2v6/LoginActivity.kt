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
    private var authorized: Boolean = true
    private lateinit var buttonLogin: Button

    private lateinit var sharedPreferences: SharedPreferences



    private fun CheckIfPasswordOk(): Boolean {
        if( !authorized )
        {
            val login = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()

            if (login.isNotEmpty() && password.isNotEmpty()) {
                // Сохраняем введенные данные
                authorized = true
                saveCredentials(login, password)
                Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show()
            }
            else
                return false
        }

        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        return true
    }



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

        var b = getIntent().getExtras()
        var value: Int = -1
        if( b != null )
        {
            value = b.getInt("deauthorize")
            authorized = false
            saveCredentials("", "")
        }

        if( authorized )
        {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val login = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()

            if( !this.CheckIfPasswordOk() ) {
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
        editor.putBoolean("AUTH", authorized)
        editor.putString("PASSWORD", password)
        editor.apply()
    }

    private fun loadSavedCredentials() {
        val savedLogin = sharedPreferences.getString("LOGIN", null)
        val savedPassword = sharedPreferences.getString("PASSWORD", null)
        authorized = sharedPreferences.getBoolean("AUTH", false)

        if (savedLogin != null &&
            savedPassword != null ) {
            editTextLogin.setText(savedLogin)
            editTextPassword.setText(savedPassword)
        }
    }
}