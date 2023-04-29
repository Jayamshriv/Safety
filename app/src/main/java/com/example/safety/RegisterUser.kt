package com.example.safety

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.safety.databinding.ActivityRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterUser : AppCompatActivity() {

    private val TAG = "Register User"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.tVAlreadyReg.setOnClickListener {
            val intent = Intent(this, LoginUser::class.java)
            startActivity(intent)
        }

        binding.RegisterBtn.setOnClickListener {
            val firstname = binding.tilfirstName.editText?.text.toString()
            val lastname = binding.tillastName.editText?.text.toString()
            val email = binding.tilemail.editText?.text.toString()
            val password = binding.tilpassword.editText?.text.toString()

            if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter all the info", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val userUid = auth.currentUser?.uid
                            val userInfo = users(firstname, lastname, email, password)
                            val db = FirebaseFirestore.getInstance()
                            db.collection("User Data")
                                .document(userUid!!)
                                .set(userInfo)
                                .addOnSuccessListener {

                                    SharedPrefFile.init(this)
                                    SharedPrefFile.putLoggedInfo("isUserLoggedIn",true)

                                    val intent = Intent(this,MainActivity::class.java)
                                    startActivity(intent)
                                    Log.d(TAG, "createUserWithEmail:success")
                                    Toast.makeText(
                                        baseContext,
                                        "User Created",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Log.d(TAG, "dataStoring: Failure")
                                    Toast.makeText(
                                        baseContext,
                                        "User Info Store failed.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                        }
                        else{
                            Log.d(TAG, "createUserWithEmail:Failure", it.exception)
                            Toast.makeText(
                                baseContext,
                                "User Creation failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
    }
}