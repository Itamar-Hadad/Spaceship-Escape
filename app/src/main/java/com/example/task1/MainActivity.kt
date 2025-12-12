package com.example.task1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.task1.interfaces.GameEventCallback
import com.example.task1.logic.GameManager
import com.example.task1.utilities.Constants
import com.example.task1.utilities.SignalManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_cells: Array<Array<AppCompatImageView>>

    private lateinit var main_FAB_right: MaterialButton

    private lateinit var main_FAB_left: MaterialButton

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var gameManager: GameManager

    private lateinit var timerJob: Job

    private var isGameRunning: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()
        startScheduler()
    }

    override fun onPause() {
        super.onPause()
        isGameRunning = false
    }

    override fun onResume() {
        super.onResume()
        if (!gameManager.isGameOver) {
            startScheduler()
        }
    }



    private fun findViews() {
        main_IMG_cells = Array(Constants.GameDetails.ROWS) { row ->
            Array(Constants.GameDetails.COLS) { col ->
                val id = resources.getIdentifier(
                    "cell_${row}_${col}",
                    "id",
                    packageName
                )
                findViewById(id)
            }
        }
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_FAB_right = findViewById(R.id.main_FAB_right)
        main_FAB_left = findViewById(R.id.main_FAB_left)

    }
    private fun initViews() {
        gameManager = GameManager(main_IMG_hearts.size)
        gameManager.gameEventListener = object: GameEventCallback {
            override fun onCrash() {
                SignalManager
                    .getInstance()
                    .toast("Crashed", SignalManager.ToastLength.SHORT)
                SignalManager
                    .getInstance()
                    .vibrate()
            }
        }
        initMovementButtons()
    }

    private fun startScheduler() {
        if (isGameRunning) return

        isGameRunning = true

        timerJob = lifecycleScope.launch {
            while (isGameRunning) {
                gameManager.updateMatrix()
                updateUI()
                delay(Constants.Timer.DELAY)
            }
        }
    }

    fun updateUI() {
        for(i in 0 until Constants.GameDetails.ROWS){
            for(j in 0 until Constants.GameDetails.COLS){
                if (i != Constants.GameDetails.ROWS - 1 || j != gameManager.spaceshipLane) {
                    val cell = main_IMG_cells[i][j]

                    val isObstacle = gameManager.objectsMatrix[i][j] == 1

                    cell.setImageResource(
                        when {
                            isObstacle -> R.drawable.meteor
                            else -> android.R.color.transparent
                        }
                    )
                }
            }
        }

        if (gameManager.crashes != 0){
            main_IMG_hearts[main_IMG_hearts.size - gameManager.crashes]
                .visibility = View.INVISIBLE
        }

        if (gameManager.isGameOver) {
            timerJob.cancel()
            isGameRunning = false

            Log.d("Game Status", "Game Over! ")
            changeActivity("😭\nGame Over!")
        }
    }

    private fun updateSpaceshipUI() {
        for (lane in 0 until Constants.GameDetails.COLS)
        {
            val cell = main_IMG_cells[main_IMG_cells.lastIndex][lane]
            if (lane == gameManager.spaceshipLane) {
                cell.setImageResource(R.drawable.spaceship)
            }
            else {
                cell.setImageResource(0)
            }
        }
    }

    private fun initMovementButtons() {
        main_FAB_left.setOnClickListener {view: View -> moveLeft()}
        main_FAB_right.setOnClickListener {view: View -> moveRight()}
    }

    private fun moveLeft() {
        gameManager.moveLeft()
        updateSpaceshipUI()

    }

    private fun moveRight() {
        gameManager.moveRight()
        updateSpaceshipUI()
    }

    fun changeActivity(message: String){
        val intent = Intent(this, GameOverActivity::class.java)
        var bundle = Bundle()
        bundle.putString("message", message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }



}