package com.example.task1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView

class GameOverActivity : AppCompatActivity() {



    private lateinit var main_LBL_title_background: MaterialTextView

    private lateinit var main_LBL_title: MaterialTextView

    private lateinit var main_FAB_play_again: ExtendedFloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    private fun findViews() {
        main_LBL_title_background = findViewById(R.id.main_LBL_title_background)
        main_LBL_title = findViewById(R.id.main_LBL_title)
        main_FAB_play_again = findViewById(R.id.main_FAB_play_again)
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        val message = bundle?.getString("message", "🤷🏻‍♂️\nUnknown Status!")
        main_LBL_title_background.text = message
        main_LBL_title.text = message
        main_FAB_play_again.setOnClickListener {
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}