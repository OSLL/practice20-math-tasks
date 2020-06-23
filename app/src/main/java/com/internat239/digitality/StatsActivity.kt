package com.internat239.digitality

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_stats.*

class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        var stat:Statistics
        var statsString = "You not play yet"

        try {
            stat = Statistics.get(getString(R.string.stat_path),this)
            statsString = "Count of games: ${stat.gamesCount} \n" +
                    "Average time: ${stat.timeAverage()} \n" +
                    "Average moves: ${stat.movesAverage()} \n" +
                    "Average hints: ${stat.hintsCountAverage()}"
        }catch (e:Exception){}


        statsView.text = statsString

        exit.setOnClickListener {
            finish()
        }
    }
}