package com.example.task1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task1.utilities.BackgroundMusicPlayer
import com.example.task1.utilities.Constants
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch

class MenuActivity : AppCompatActivity() {


    private lateinit var menu_SWITCH_buttons: MaterialSwitch

    private lateinit var menu_SWITCH_speed: MaterialSwitch

    private lateinit var menu_BTN_highScores: ExtendedFloatingActionButton

    private lateinit var menu_BTN_play: ExtendedFloatingActionButton

    private lateinit var menu_SWITCH_sound: MaterialSwitch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findviews()
        initviews()
    }
    override fun onResume() {
        super.onResume()
        val player = BackgroundMusicPlayer.getInstance()

        menu_SWITCH_sound.isChecked = player.isMusicOn()

        player.playIfAllowed()
    }




    private fun findviews() {
        menu_SWITCH_buttons = findViewById(R.id.menu_SWITCH_buttons)
        menu_SWITCH_speed = findViewById(R.id.menu_SWITCH_speed)
        menu_BTN_highScores = findViewById(R.id.menu_BTN_highScores)
        menu_BTN_play = findViewById(R.id.menu_BTN_play)
        menu_SWITCH_sound = findViewById(R.id.menu_SWITCH_sound)
    }

    private fun initviews() {
        menu_BTN_play.setOnClickListener { view: View -> changeToMainActivity() }
        menu_BTN_highScores.setOnClickListener { view: View -> changeToScoreActivity() }
        menu_SWITCH_sound.setOnCheckedChangeListener { _, isChecked ->
            BackgroundMusicPlayer.getInstance().setMusicOn(isChecked)
        }

    }
    private fun MenuActivity.changeToMainActivity() {
        val bundle = Bundle()
        val isButtonsModeOn = menu_SWITCH_buttons.isChecked
        val isFastModeOn = menu_SWITCH_speed.isChecked
        bundle.putBoolean(Constants.BundleKeys.BUTTONS_MODE, isButtonsModeOn)
        bundle.putBoolean(Constants.BundleKeys.FAST_MODE, isFastModeOn)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun MenuActivity.changeToScoreActivity() {
        val intent = Intent(this, ScoreActivity::class.java)
        startActivity(intent)
    }


}



