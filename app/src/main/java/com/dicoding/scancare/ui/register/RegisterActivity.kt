package com.dicoding.scancare.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.dicoding.scancare.databinding.ActivityRegisterBinding
import com.dicoding.scancare.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.tvLoginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditTextInput.text.toString()
            val password = binding.passwordEditTextInput.text.toString()

            //validasi email
            if (email.isEmpty()){
                binding.emailEditTextInput.error = "email harus diisi"
                binding.emailEditTextInput.requestFocus()
                return@setOnClickListener
            }

            //validasi email tidak sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailEditTextInput.error = "email tidak valid"
                binding.emailEditTextInput.requestFocus()
                return@setOnClickListener
            }

            //validasi password
            if (password.isEmpty()){
                binding.passwordEditTextInput.error = "password harus diisi"
                binding.passwordEditTextInput.requestFocus()
                return@setOnClickListener
            }

            //validasi panjang password

            if (password.length < 8){
                binding.passwordEditTextInput.error = "password minimal mengandung 8 karakter"
                binding.passwordEditTextInput.requestFocus()
                return@setOnClickListener
            }

            RegisterFirebase(name,email,password)
        }
    }

    private fun RegisterFirebase(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this,"Register Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}