package com.dicoding.scancare.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.data.preference.UserModel
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.databinding.ActivityLoginBinding
import com.dicoding.scancare.ui.main.MainActivity
import com.dicoding.scancare.ui.register.RegisterActivity
import com.dicoding.scancare.ui.register.UserViewModel
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {
            val email = binding.loginInputLayout.findViewById<TextInputEditText>(R.id.loginEmailInputEditText).text.toString()
            val password = binding.loginPasswordInputLayout.findViewById<TextInputEditText>(R.id.loginPasswordInputEditText).text.toString()

            userViewModel.loginUser(email, password)
        }

        binding.tvRegisterNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        userViewModel.loginResult.observe(this) { result ->
            binding.progressBar.visibility = View.VISIBLE
            when (result) {
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Login successful")
                    val response = result.data
                    userViewModel.saveSession(
                        UserModel(userId = response.userId)
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(result.error)
                }
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}