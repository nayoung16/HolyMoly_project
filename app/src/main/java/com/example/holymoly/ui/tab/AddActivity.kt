package com.example.holymoly.ui.tab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.holymoly.R

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar!!.hide();
    }
}