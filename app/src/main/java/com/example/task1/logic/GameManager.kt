package com.example.task1.logic

import com.example.task1.interfaces.GameEventCallback
import com.example.task1.utilities.Constants

class GameManager (private val lifeCount: Int = 3){


    var crashes: Int = 0
        private set

    var spaceshipLane: Int = 1
        private set

    var objectsMatrix =
        Array(Constants.GameDetails.ROWS) {IntArray(Constants.GameDetails.COLS) {0} }


    val isGameOver: Boolean
        get() = crashes == lifeCount

    var gameEventListener: GameEventCallback? = null

    fun updateMatrix() {
        for (i in Constants.GameDetails.ROWS - 1 downTo 1) {
            objectsMatrix[i] = objectsMatrix[i - 1].copyOf()
        }

        val generatePercent = (0 until 100).random()
        val isGenerateNew = (generatePercent < 80)
        if (isGenerateNew) {
            val targetLane = (0 until Constants.GameDetails.COLS).random()
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { if (it == targetLane) 1 else 0 }
        } else {
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { 0 }
        }

        if (objectsMatrix[Constants.GameDetails.ROWS - 1][spaceshipLane] == 1) {
            // crashed happened
            crashes++

            gameEventListener?.onCrash()
        }

    }

    fun moveLeft(){
        if(spaceshipLane > 0) {
            spaceshipLane--
        }
    }

    fun moveRight(){
        if(spaceshipLane < Constants.GameDetails.COLS - 1){
            spaceshipLane++
        }
    }


}