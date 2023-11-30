package com.example.holymoly

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreHelper {
    private val db = Firebase.firestore
    fun addUserToFirestore(email: String, nickName: String) {
        val user = hashMapOf(
            "email" to email,
            "nickName" to nickName
        )

        db.collection("hello")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    companion object {
        private const val TAG = "FirestoreHelper"
    }

}