package com.example.skainet_android

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.work.*
import com.example.skainet_android.background.BackgroundWorker

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkainetApp()
        }

        requestLocationPermissions()
        startJob()
    }

    private fun startJob() {
        val constraints = Constraints.NONE
        val inputData = Data.EMPTY
        val work = OneTimeWorkRequest.Builder(BackgroundWorker::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance(this).apply {
            enqueue(work)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                0)
            return
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SkainetApp()
}