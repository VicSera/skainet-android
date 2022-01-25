package com.example.skainet_android.background

import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit.SECONDS

class BackgroundWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // perform long running operation
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

        Looper.prepare()
        while (true) {
            quotes.forEach {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                SECONDS.sleep(10)
            }
        }
        return Result.success()
    }
}