package com.example.holymoly

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthHelper {
    private val auth: FirebaseAuth = Firebase.auth

    fun getCurrentUserEmail(): String? {
        val currentUser = auth.currentUser
        return currentUser?.email
    }
}