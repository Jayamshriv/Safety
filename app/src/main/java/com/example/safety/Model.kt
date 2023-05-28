package com.example.safety

import com.google.firebase.firestore.DocumentSnapshot

data class Model(
    val name: String,
    val phoneNumber: String,
    val batPer: Int,
    val lat : String,
    val long: String,
    val connectionInfo: String
){
    constructor(): this(" ","",0,"","","")
}
