package com.makentoshe.androidgithubcitemplate

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*

class Statistics : Serializable {

    var gamesCount = 0
        private set

    private var games = mutableListOf<GameStats>()

    private var movesSum = 0
    private var timeSum = 0
    private var hintsCountSum = 0


    fun movesAverage() = movesSum / gamesCount
    fun timeAverage() = timeSum / gamesCount
    fun hintsCountAverage() = hintsCountSum / gamesCount

    fun addGame(gameStats: GameStats) {
        games.add(gameStats)
        gamesCount++
        movesSum += gameStats.moves
        timeSum += gameStats.time
        hintsCountSum += gameStats.hintsCount
    }

    fun addGame(difficulty : Int, mode : Int, task: String, answer: String, moves : Int, minMoves : Int, timeStart : Date, timeEnd : Date, hintsCount : Int, result: Boolean) {
        addGame(GameStats(difficulty, mode, task, answer, moves, minMoves, timeStart, timeEnd, hintsCount, result))
    }

    fun save(file : String, context: Context) {
        val fos = context.openFileOutput(file, Context.MODE_PRIVATE)
        val os = ObjectOutputStream(fos)
        os.writeObject(this)
        os.close()
        fos.close()
    }

    override fun toString(): String {
        var res = ""
        for(gs in games) {
            res += gs.toString() + "\n"
        }
        return res
    }

    companion object {
        data class GameStats(
            val difficulty : Int,
            val mode : Int,
            val task : String,
            val answer : String,
            var moves : Int,
            val minMoves : Int,
            val startTime : Date,
            var endTime : Date,
            var hintsCount : Int,
            var result : Boolean
        ) : Serializable {
            constructor() : this(0, 0, "", "", 0, 0, Date(), Date(), 0, false)
            constructor(
                difficulty: Int,
                mode: Int,
                task : String,
                answer: String,
                minMoves: Int
            ) : this(
                difficulty,
                mode,
                task,
                answer,
                0,
                minMoves,
                Calendar.getInstance().time,
                Date(),
                0,
                false)
            val time
                get() = (endTime.time - startTime.time).toInt()

            override fun toString(): String {
                return "Game(difficulty = $difficulty, mode = $mode, task = $task, answer = $answer, moves = $moves" +
                        " minMoves = $minMoves, startTime = $startTime, endTime = $endTime, hintsCount = $hintsCount, result = $result)"
            }
        }

        fun get(file : String, context: Context) : Statistics {
            if (file != "") {
                val fi = context.openFileInput(file)
                val ois = ObjectInputStream(fi)
                val s = (ois.readObject() as Statistics)
                ois.close()
                fi.close()
                return s
            }
            return Statistics()
        }
    }
}