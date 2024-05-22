package exportkit.figma

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import com.example.mathapp.R
import com.example.mathapp.ui.theme.MainActivity2
import com.example.mathapp.ui.theme.ThemeUtils

class activity_main : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the button by its ID
        val button = findViewById<ImageButton>(R.id.button)

        // Set an OnClickListener to the button
        button.setOnClickListener {
            // Create an Intent to start MainActivity2
            val intent = Intent(this@activity_main, MainActivity2::class.java)
            // Start the second activity
            startActivity(intent)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
//    }
}
