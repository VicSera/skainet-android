package com.example.skainet_android.background

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.*
import android.os.Build
import android.os.CancellationSignal
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit.SECONDS

class BackgroundWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams), SensorEventListener {
    var lastTemperatureReading = 0f

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun doWork(): Result {
        val locationManager = getSystemService(context, LocationManager::class.java)!!
        val sensorManager = getSystemService(context, SensorManager::class.java)!!

        val executor = Executors.newSingleThreadExecutor().apply {
            execute {
                Looper.prepare()
            }
        }

        val quotes = setOf(
            "“A journey of a thousand miles begins with a single step” – Lao Tzu",
            "“Do not follow where the path may lead. Go instead where there is no path and leave a trail” – Ralph Waldo Emerson",
            "“I am not the same, having seen the moon shine on the other side of the world” – Mary Anne Radmacher",
            "“Man cannot discover new oceans unless he has the courage to lose sight of the shore” – Andre Gide",
            "“We take photos as a return ticket to a moment otherwise gone” – Katie Thurmes",
            "“There’s a sunrise and a sunset every single day, and they’re absolutely free. Don’t miss so many of them” – Jo Walton",
            "“Traveling – it leaves you speechless, then turns you into a storyteller” – Ibn Battuta",
            "“Oh the places you’ll go” – Dr. Seuss",
            "“Wherever you go becomes a part of you somehow” – Anita Desai",
            "“Take only memories, leave only footprints ” – Chief Seattle"
        )

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        Looper.prepare()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        locationManager.getCurrentLocation(
            LocationManager.GPS_PROVIDER,
            CancellationSignal(),
            executor,
            {
                Toast.makeText(context, "Location: ${it.latitude};${it.longitude}", Toast.LENGTH_SHORT).show()
            })

        while (true) {
            quotes.forEach { quote ->
                SECONDS.sleep(10)
                Toast.makeText(context, "$lastTemperatureReading;$quote", Toast.LENGTH_SHORT).show()
            }
        }
        return Result.success()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        lastTemperatureReading = event?.values?.get(0) ?: 0f
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}