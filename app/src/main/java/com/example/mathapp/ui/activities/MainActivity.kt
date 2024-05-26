package com.example.mathapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.example.mathapp.R
import com.example.mathapp.ui.theme.MainActivity2


class MainActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the button by its ID
        val button = findViewById<ImageButton>(R.id.button)

        // Set an OnClickListener to the button
        button.setOnClickListener {
            // Create an Intent to start MainActivity2
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            // Start the second activity
            startActivity(intent)
        }

    }

}