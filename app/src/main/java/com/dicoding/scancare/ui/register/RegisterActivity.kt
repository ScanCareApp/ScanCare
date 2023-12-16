package com.dicoding.scancare.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.dicoding.scancare.R
import com.dicoding.scancare.ViewModelFactory
import com.dicoding.scancare.data.preference.UserModel
import com.dicoding.scancare.data.remote.ResultState
import com.dicoding.scancare.databinding.ActivityRegisterBinding
import com.dicoding.scancare.ui.main.MainActivity
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener {
            val name = binding.nameEditTextLayout.findViewById<TextInputEditText>(R.id.nameEditText).text.toString()
            val address = binding.addressEditTextLayout.findViewById<TextInputEditText>(R.id.adressInputEditText).text.toString()
            val email = binding.emailEditTextLayout.findViewById<TextInputEditText>(R.id.emailEditTextInput).text.toString()
            val password = binding.passwordEditTextLayout.findViewById<TextInputEditText>(R.id.passwordEditTextInput).text.toString()

            userViewModel.registerUser(address, email, password, name)
        }

        userViewModel.registerResult.observe(this, Observer { result ->
            binding.progressBar.visibility = View.VISIBLE
            when (result) {
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Registration successful")
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
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}