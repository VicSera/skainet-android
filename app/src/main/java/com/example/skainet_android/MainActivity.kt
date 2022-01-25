package com.example.skainet_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.*
import com.example.skainet_android.background.BackgroundWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkainetApp()
        }

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
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SkainetApp()
}