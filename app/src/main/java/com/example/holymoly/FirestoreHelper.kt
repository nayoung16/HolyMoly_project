package com.example.holymoly

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreHelper {
    private val db = Firebase.firestore

    fun addUserToFirestore(email: String) {

        val data = hashMapOf("user_email" to email)

        db.collection("user").document(email)
            .set(data)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun addHolidayToFirestore(holiday_title:String, start_date: String, end_date:String, category:String) {
        val data = hashMapOf(
            "holiday_title" to holiday_title,
            "start_date" to start_date,
            "end_date" to end_date,
            "category" to category)
    }

    fun getAllHolidaysFromFirestore(email: String) {
        db.collection("user")
            .document(email)
            .collection("holiday")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }
    companion object {
        private const val TAG = "FirestoreHelper"
    }

}