package com.example.holymoly

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreHelper {
    private val db = Firebase.firestore
    fun addUserToFirestore(email: String, nickName: String) {

    }

    companion object {
        private const val TAG = "FirestoreHelper"
    }

}