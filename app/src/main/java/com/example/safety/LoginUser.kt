package com.example.safety

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import com.example.safety.databinding.ActivityLoginUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class LoginUser : AppCompatActivity() {

    private val TAG = "LoginUser"
    private lateinit var auth: FirebaseAuth

    //    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityLoginUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.tVNewUserRegister.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.tilemailLogin.editText?.text.toString()
            val password = binding.tilpasswordLogin.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {

                            SharedPrefFile.init(this)
                            SharedPrefFile.putLoggedInfo("isUserLoggedIn",true)

                            val intent = Intent(this,MainActivity::class.java)
                            Log.d(TAG, "createUserWithEmail:success")
                            val useruid = auth.currentUser?.uid
                            val db = FirebaseFirestore.getInstance()

                            db.collection("User Data")
                                .document(useruid!!)
                                .get()
                                .addOnSuccessListener { document ->
                                    val user = document.toObject<users>()

                                    if (user != null) {
                                        val firstname = user.firstName
                                        val lastname = user.lastName
                                        val email = user.email

                                        Toast.makeText(
                                            this,
                                            "Welcome $firstname $lastname ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", it.exception)
                            Toast.makeText(
                                this,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }
        }
    }

}