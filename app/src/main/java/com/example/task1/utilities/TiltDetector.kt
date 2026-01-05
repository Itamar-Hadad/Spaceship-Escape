package com.example.task1.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.task1.interfaces.TiltCallback

class TiltDetector(context: Context, private val tiltCallback: TiltCallback?) {
    private val sensorManager = context
        .getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensor = sensorManager
        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor

    private lateinit var sensorEventListener: SensorEventListener

    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                // Pass
            }

            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]



                handleTilt(x, y, z)
            }
        }
    }

    private fun handleTilt(x: Float, y: Float, z: Float) {
        if (System.currentTimeMillis() - timestamp >= 500) {
            timestamp = System.currentTimeMillis()
            if (x >= 3.0) {
                // tilt left
                tiltCallback?.tiltLeft()
            } else if (x <= -3.0 ) {
                // tilt right
                tiltCallback?.tiltRight()
            }
            if (z >= 3.0) {
                // Forward tilt
                tiltCallback?.tiltForward()
            } else if (z <= -3.0) {
                // Backward tilt
                tiltCallback?.tiltBackward()
            }
        }
    }

    fun start() {
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }


    fun stop() {
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }






}
