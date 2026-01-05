package com.example.task1.logic

import com.example.task1.interfaces.GameEventCallback
import com.example.task1.utilities.Constants

class GameManager (private val lifeCount: Int = 3){


    var crashes: Int = 0
        private set

    var spaceshipLane: Int = 2
        private set


    var score: Int = 0
        private set


    var distance: Int = 0
        private set

    var objectsMatrix =
        Array(Constants.GameDetails.ROWS) {IntArray(Constants.GameDetails.COLS) {0} }


    val isGameOver: Boolean
        get() = crashes == lifeCount

    val recordsManager: RecordsManager = RecordsManager(10)

    var gameEventListener: GameEventCallback? = null

    fun updateMatrix() {
        for (i in Constants.GameDetails.ROWS - 1 downTo 1) {
            objectsMatrix[i] = objectsMatrix[i - 1].copyOf()
        }

        distance ++ // in each loop iteration i want to increase the distance
        score += Constants.Score.DISTANCE_WORTH // for each distance passed increase score

        val generatePercent = (0 until 100).random()
        val isGenerateNew = (generatePercent < 85)
        if (isGenerateNew) {
            val targetLane = (0 until Constants.GameDetails.COLS).random()
            val generateGalaxy = (0 until 100).random() < 15
            val value = if (generateGalaxy) Constants.ObjectValues.GALAXY_VALUE else Constants.ObjectValues.OBSTACLE_VALUE
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { if (it == targetLane) value else 0 }
        } else {
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { 0 }
        }

        if (objectsMatrix[Constants.GameDetails.ROWS - 1][spaceshipLane] == Constants.ObjectValues.OBSTACLE_VALUE) {
            // crashed happened
            crashes++

            gameEventListener?.onCrash()
        }
        else if (objectsMatrix[Constants.GameDetails.ROWS - 1][spaceshipLane] == Constants.ObjectValues.GALAXY_VALUE){
            score += Constants.Score.GALAXY_WORTH
            gameEventListener?.onGalaxyCollecting()
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

    fun gameOver(lat: Double, lon: Double) {
        recordsManager.saveScore(score, lat, lon)
    }

    fun getCurrentScore(): Int {
        return score
    }

}