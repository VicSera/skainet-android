package com.example.skainet_android

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.skainet_android.core.SkainetAppTheme

@Composable
fun SkainetApp() {
    SkainetAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            SkainetAppNavGraph()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkainetAppPreview() {
    SkainetApp()
}