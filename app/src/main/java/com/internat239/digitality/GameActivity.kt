package com.internat239.digitality


import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*


class GameActivity: AppCompatActivity() {

    private var statistics = Statistics()
    private lateinit var gameStats : Statistics.Companion.GameStats

    class MovableTextView(
        context : Context,
        private val fixed : Boolean,
        var infinitive : Boolean
    ) : AppCompatTextView(context) {
        var task = false
        private val standWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, resources.displayMetrics).toInt()

        init {
            layoutParams = ViewGroup.LayoutParams(
                standWidth,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            setTextColor(Color.parseColor("#E65A5A"))
            gravity = Gravity.CENTER

        }

        fun decreaseWidth(newWidth : Int) {
            var tempNewWidth = newWidth
            if(tempNewWidth > standWidth) {
                tempNewWidth = standWidth
            }
            val lp = layoutParams
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize / width * tempNewWidth)
            lp.width = tempNewWidth
            requestLayout()
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            super.onTouchEvent(event)

            if(fixed) return false

            return when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val target = if(infinitive && !task) clone() else this
                    val data = ClipData.newPlainText("", "")
                    val dsb = DragShadowBuilder(this)
                    if (Build.VERSION.SDK_INT >= 24) {
                        target.startDragAndDrop(data, dsb, target, 0)
                    } else {
                        startDrag(data, dsb, this, 0)
                    }
                    true
                }
                else -> false
            }
        }

        private fun clone(): MovableTextView {
            val cloned = MovableTextView(context, fixed, infinitive)
            cloned.text = text
            cloned.visibility = View.INVISIBLE
            (parent as LinearLayout).addView(cloned)
            return cloned
        }
    }


    private class TaskOnDragListener : View.OnDragListener {
        private var beginIndex = -1
        private var index = -2
        private var text = ""
        private var dropped = false

        override fun onDrag(v: View?, event: DragEvent?): Boolean {
            if(event == null) return false

            val vll = v as LinearLayout

            val dragView = event.localState as MovableTextView
            return when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    dropped = false
                    beginIndex = vll.indexOfChild(dragView)
                    println(beginIndex)
                    vll.removeView(dragView)
                    text = dragView.text.toString()
                    dragView.text = ""
                    if(beginIndex != -1) {
                        vll.addView(dragView, beginIndex)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {

                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    val begi = index

                    index = 0
                    for (i in 0 until vll.childCount) {
                        if (
                            vll.getChildAt(i).x + vll.getChildAt(i).width / 2 <= event.x + dragView.width / 2
                        ) index = i
                    }

                    if(begi != index) {
                        vll.removeView(dragView)
                        vll.addView(dragView, index)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    vll.removeView(dragView)
                    index = -2
                    true
                }
                DragEvent.ACTION_DROP -> {
                    dragView.task = true
                    dropped = true
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    vll.removeView(dragView)
                    dragView.text = text
                    dragView.visibility = View.VISIBLE
                    if(dropped)
                        vll.addView(dragView, index)
                    if(!event.result && beginIndex != -1)
                        vll.addView(dragView, beginIndex)
                    index = -2
                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e("DragDrop", "Unknown action type received by OnDragListener.")
                    false
                }
            }
        }

    }

    private class SymbolsOnDragListener : View.OnDragListener {
        private var dropped = false
        private var beginIndex = -1

        override fun onDrag(v: View?, event: DragEvent?): Boolean {
            if(event == null)
                return false

            val vll = v as LinearLayout

            val dragView = event.localState as MovableTextView
            return when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    beginIndex = vll.indexOfChild(dragView)
                    dropped = false
                    vll.removeView(dragView)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    try {
                        if ((!event.result && beginIndex != -1 || dropped) && !dragView.infinitive) {
                            (dragView.parent as? LinearLayout)?.removeView(dragView)
                            vll.addView(dragView, beginIndex)
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                    true
                }
                DragEvent.ACTION_DROP -> {
                    dropped = true
                    true
                }
                else -> true
            }
        }
    }

    private fun setTaskLayout(task : String) {

        task_layout.removeAllViews()

        var text : MovableTextView

        for(a in task) {
            text = MovableTextView(this, true, false)
            text.text = a.toString()

            task_layout.addView(text)
        }
    }

    private fun setSymbolsLayout(exclude: String = "") {

        symbols_layout.removeAllViews()

        var text : MovableTextView

        var symbols = when(intent.getIntExtra("mode", 1)) {
            0 -> "123456789"
            1 -> "+-*"
            else -> "123456789+-*"
        }

        if(intent.getIntExtra("difficulty", 1) == 2 && intent.getIntExtra("mode", 1) != 0)
            symbols += "^%&|"

        for(a in symbols) {
            if(!a.isDigit() || a !in exclude) {

                text = MovableTextView(this, false, !a.isDigit())
                text.text = a.toString()

                symbols_layout.addView(text)
            }
        }
    }

    private fun widthFix(layout: LinearLayout) {
        val maxWidth = (layout.parent as ConstraintLayout).width - layout.paddingLeft - layout.paddingRight
        for(mtv in layout.children) {
            (mtv as MovableTextView).decreaseWidth(maxWidth / layout.childCount)
        }
    }

    private fun task(difficulty : Int, mode : Int) : Pair<String, String> {
        return when(mode) {
            0 -> game1(difficulty)
            1 -> game2(difficulty)
            else -> game3(difficulty)
        }
    }

    private fun createTask() : Pair<String, String> {
        val difficulty = intent.getIntExtra("difficulty", 0) + 1
        val mode = intent.getIntExtra("mode", 0)
        val task = task(difficulty, mode)
        gameStats = Statistics.Companion.GameStats(difficulty, mode, task.first, task.second, task.second.length - task.first.length)
        return task
    }


    private fun getStats() {

        val user = FirebaseAuth.getInstance().currentUser
        var statisticsCloud : Statistics? = null
        statistics = Statistics.getLocal(getString(R.string.stat_path), this)
        if(user != null)
            statisticsCloud = Statistics.getCloud(Firebase.firestore, user.uid)

        if(statisticsCloud != null && statisticsCloud.gamesCount >= statistics.gamesCount)
            statistics = statisticsCloud
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        difficulty.text = getString(R.string.difficulty_annotation, intent.getStringExtra("difficulty-name"))
        mode.text = getString(R.string.mode_annotation, intent.getStringExtra("mode-name"))
        val menu = PopupMenu(this, button)
        menu.menu.add(1, 101, 1, "Settings")
        menu.menu.add(1, 102, 2, "How to play?")
        menu.setOnMenuItemClickListener {
            when(it.itemId) {
                101 -> {
                    finish()
                    true
                }
                102 -> {
                    val intent = Intent(this, HelpActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        button.setOnClickListener {
            menu.show()
        }

        button.setOnDragListener { _, event ->
            when(event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    gameStats.moves++
                    true
                }
                else -> true
            }
        }


        getStats()


        task_layout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            widthFix(v as LinearLayout)
        }

        symbols_layout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            widthFix(v as LinearLayout)
        }



        task_layout.setOnDragListener(TaskOnDragListener())

        symbols_layout.setOnDragListener(SymbolsOnDragListener())
        var exclude = ""

        var task = createTask()

        setTaskLayout(task.first)
        setSymbolsLayout()

        skipButton.setOnClickListener {
            if(!gameStats.result)
                gameStats.endTime = Calendar.getInstance().time
            statistics.addGame(gameStats)

            skipButton.setText(R.string.skip_button)
            task = createTask()
            exclude = ""
            setTaskLayout(task.first)
            setSymbolsLayout()
        }

        checkButton.setOnClickListener {
            var left = ""
            var right = ""
            var side = false

            for(i in 0 until task_layout.childCount) {
                if((task_layout.getChildAt(i) as TextView).text == "=") {
                    side = true
                } else if(!side) {
                    left += (task_layout.getChildAt(i) as TextView).text
                } else {
                    right += (task_layout.getChildAt(i) as TextView).text
                }
            }
            try {
                if (Solver.solve(left) == Solver.solve(right)) {
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                    skipButton.setText(R.string.next_button)
                    gameStats.result = true
                    gameStats.endTime = Calendar.getInstance().time
                }
                else
                    Toast.makeText(this, "Incorrect :(", Toast.LENGTH_SHORT).show()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }


        hintButton.setOnClickListener {
            val hint = hint(task)
            task = Pair(hint.first, task.second)

            gameStats.hintsCount++
            setTaskLayout(hint.first)

            if(hint.second != ' ') {
                exclude += hint.second
                setSymbolsLayout(exclude)

                gameStats.moves++
            }
            else {
                Toast.makeText(this, "Чего тебе ещё надо, собака?", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun finish() {
        statistics.saveLocal(getString(R.string.stat_path), this)
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null)
            statistics.updateCloud(Firebase.firestore, user.uid)
        super.finish()
    }
}