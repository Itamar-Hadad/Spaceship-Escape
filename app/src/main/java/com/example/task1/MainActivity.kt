package com.example.task1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.task1.interfaces.GameEventCallback
import com.example.task1.interfaces.TiltCallback
import com.example.task1.logic.GameManager
import com.example.task1.utilities.Constants
import com.example.task1.utilities.SignalManager
import com.example.task1.utilities.SingleSoundPlayer
import com.example.task1.utilities.TiltDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_cells: Array<Array<AppCompatImageView>>

    private lateinit var main_FAB_right: MaterialButton

    private lateinit var main_FAB_left: MaterialButton

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_BTN_back: AppCompatImageButton

    private lateinit var main_LBL_score: MaterialTextView

    private lateinit var main_LBL_distance: MaterialTextView

    private lateinit var gameManager: GameManager

    private var timerJob: Job? = null

    private var isButtonsModeOn: Boolean = true

    private var isFastModeOn: Boolean = true

    private var tiltDetector: TiltDetector? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isGameOverDialogShown: Boolean = false

    private var permissionGranted: Boolean = false

    private lateinit var ssp: SingleSoundPlayer

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Location", "permission was granted")
            permissionGranted = true
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("on start", "called")
        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        findViews()
        initViews()
        startScheduler()
    }

    override fun onPause() {
        super.onPause()
        tiltDetector?.stop()
        timerJob?.cancel()
    }

    override fun onResume() {
        super.onResume()
        tiltDetector?.start()
        if (timerJob == null || timerJob?.isCancelled == true) {
            startScheduler()
        }
    }





    private fun findViews() {
        main_BTN_back = findViewById(R.id.main_BTN_back)
        main_LBL_score = findViewById(R.id.main_LBL_score)
        main_LBL_distance = findViewById(R.id.main_LBL_distance)
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
        ssp = SingleSoundPlayer(this)
        gameManager.gameEventListener = object: GameEventCallback {
            override fun onCrash() {
                SignalManager
                    .getInstance()
                    .toast("Crashed", SignalManager.ToastLength.SHORT)
                SignalManager
                    .getInstance()
                    .vibrate()

                ssp.playSound(R.raw.crashsound)
            }

            override fun onGalaxyCollecting() {
                ssp.playSound(R.raw.galaxysound)
            }
        }

        val bundle: Bundle? = intent.extras
        isButtonsModeOn = bundle?.getBoolean(Constants.BundleKeys.BUTTONS_MODE, true) ?: true
        isFastModeOn = bundle?.getBoolean(Constants.BundleKeys.FAST_MODE, true) ?: true

        main_BTN_back.setOnClickListener {
            finish()
        }

        if (isButtonsModeOn) {
            initMovementButtons()
        }
        else {
            hidemovementButtons()
            initTiltDetector()
        }


    }


    private fun hidemovementButtons() {
        main_FAB_left.visibility = View.INVISIBLE
        main_FAB_right.visibility = View.INVISIBLE
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            this,
            object : TiltCallback{
                override fun tiltRight() {
                    moveRight()
                }

                override fun tiltLeft() {
                    moveLeft()
                }

                override fun tiltForward() {
                    speedUp()
                }

                override fun tiltBackward() {
                    speedDown()
                }

            }
        )
    }


    private fun startScheduler() {
        timerJob = lifecycleScope.launch {
            while (true) {
                val delay = if (isFastModeOn) Constants.Scheduler.FAST_DELAY else Constants.Scheduler.SLOW_DELAY
                delay(delay)
                gameManager.updateMatrix()
                updateUI()
                Log.d("Scheduler", "ran scheduler to update game")
            }
        }
    }

    fun updateUI() {
        for(i in 0 until Constants.GameDetails.ROWS){
            for(j in 0 until Constants.GameDetails.COLS){
                if (i != Constants.GameDetails.ROWS - 1 || j != gameManager.spaceshipLane) {
                    val cell = main_IMG_cells[i][j]

                    val isObstacle = gameManager.objectsMatrix[i][j] == Constants.ObjectValues.OBSTACLE_VALUE

                    val isGalaxy = gameManager.objectsMatrix[i][j] == Constants.ObjectValues.GALAXY_VALUE


                    cell.setImageResource(
                        when {
                            isObstacle -> R.drawable.meteor
                            isGalaxy -> R.drawable.galaxy
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

        main_LBL_score.text = buildString{
            append("Score: ")
            append(gameManager.getCurrentScore())
        }
        main_LBL_distance.text = buildString{
            append("Distance: ")
            append(gameManager.distance)
        }

        if (gameManager.isGameOver) {
            timerJob?.cancel()
            if (!isGameOverDialogShown) {
                isGameOverDialogShown = true
                Log.d("Game Status", "Game over")
                CoroutineScope(Dispatchers.Main).launch {
                    val success = saveLocationOnGameOver()
                    if (success) {
                        showGameOverDialog()
                    }
                    isGameOverDialogShown = true
                }
            }
        }
    }

    private fun showGameOverDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.game_over_dialog, null)
        val messageScore: MaterialTextView = dialogView.findViewById(R.id.gameOver_LBL_score)
        val scoresButton: MaterialButton = dialogView.findViewById(R.id.gameOver_BTN_nextPage)
        messageScore.text = buildString {
            append("Score: ")
            append(gameManager.score)
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        scoresButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, ScoreActivity::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
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

    private fun speedUp(){
        Log.d("game speed","speeding up")
        if (!isFastModeOn) {
            isFastModeOn = true
            restartTimer()
        }

    }

    private fun speedDown(){
        Log.d("game speed","speeding down")
        if (isFastModeOn) {
            isFastModeOn = false
            restartTimer()
        }

    }

    private fun restartTimer() {
        timerJob?.cancel()
        startScheduler()
    }




    @SuppressLint("MissingPermission")
    private suspend fun saveLocationOnGameOver(): Boolean {
        if (!permissionGranted) {
            Log.d("Location", "no permission")
            return false
        }

        return try {
            val location = fusedLocationClient
                .getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                .await()

            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("Location", "my location is $latitude, $longitude")
                gameManager.gameOver(latitude, longitude) // Assuming this updates score internally
                true
            } else {
                Log.d("Location", "location is null")
                false
            }
        } catch (e: Exception) {
            Log.d("Location", "failed with exception: ${e.message}")
            false
        }
    }

    fun changeActivity(message: String){
        val intent = Intent(this, ScoreActivity::class.java)
        var bundle = Bundle()
        bundle.putString("message", message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }



}