package com.makentoshe.androidgithubcitemplate


import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_game.view.*


class GameActivity: AppCompatActivity() {

    class MoveableTextView(
        context : Context,
        private val fixed : Boolean,
        var infinitive : Boolean
    ) : AppCompatTextView(context) {
        init {
            layoutParams = ViewGroup.LayoutParams(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, resources.displayMetrics).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            setTextColor(Color.parseColor("#E65A5A"))
            textAlignment = View.TEXT_ALIGNMENT_CENTER

        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            super.onTouchEvent(event)

            if(fixed) return false

            return when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val target = if(infinitive) clone() else this
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

        private fun clone(): MoveableTextView {
            val cloned = MoveableTextView(context, fixed, infinitive)
            cloned.text = text
            (parent as LinearLayout).addView(cloned)
            cloned.visibility = View.INVISIBLE
            return cloned
        }
    }

    private fun task() : Pair<String, String> {
        val hard = intent.getIntExtra("difficulty", 1) + 1

        return when(intent.getIntExtra("mode", 1)) {
            0 -> game1(hard)
            1 -> game2(hard)
            else -> game3(hard)
        }
    }

    private fun setTaskLayout(task : String) {

        task_layout.removeAllViews()

        var text : MoveableTextView

        for(a in task) {
            text = MoveableTextView(this, true, false)
            text.text = a.toString()

            task_layout.addView(text)
        }
    }

    private fun setSymbolsLayout() {

        symbols_layout.removeAllViews()

        var text : MoveableTextView

        var symbols = when(intent.getIntExtra("mode", 1)) {
            0 -> "123456789"
            1 -> "+-*"
            else -> "123456789+-*"
        }

        if(intent.getIntExtra("difficulty", 1) == 2)
            symbols += "^%&|"

        for(a in symbols) {
            text = MoveableTextView(this, false, !a.isDigit())
            text.text = a.toString()

            symbols_layout.addView(text)
        }
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



        var index = -2
        val draggingView = MoveableTextView(this, true, false)
        var beginIndex = 0


        task_layout.setOnDragListener { v, event ->
            val vll = v as LinearLayout

            val dragView = event.localState as MoveableTextView
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    beginIndex = task_layout.indexOfChild(dragView)
                    println(beginIndex)
                    index = -2
                    task_layout.removeView(dragView)
                    if(beginIndex != -1) {
                        vll.addView(draggingView, beginIndex)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {

                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    val begi = index

                    index = -1
                    for (i in 0 until task_layout.childCount) {
                        if (
                            vll.getChildAt(i).x + vll.getChildAt(i).width / 2 <= event.x + dragView.width / 2
                        ) index = i
                    }

                    if(begi != index) {
                        task_layout.removeView(draggingView)
                        task_layout.addView(draggingView, index)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    task_layout.removeView(draggingView)
                    index = -2
                    true
                }
                DragEvent.ACTION_DROP -> {
                    task_layout.addView(dragView, index)
                    dragView.infinitive = false
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    task_layout.removeView(draggingView)
                    dragView.visibility = View.VISIBLE
                    index = -2
                    if(!event.result && beginIndex != -1) task_layout.addView(dragView, beginIndex)
                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e("DragDrop", "Unknown action type received by OnDragListener.")
                    false
                }
            }
        }

        symbols_layout.setOnDragListener { v, event ->
            val vll = v as LinearLayout

            val dragView = event.localState as MoveableTextView
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    val i = vll.indexOfChild(dragView)
                    vll.removeView(dragView)
                    //vll.addView(dragView, i)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    if(!event.result && beginIndex == -1 && !dragView.infinitive)
                        vll.addView(dragView)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    task_layout.removeView(dragView)
                    if(!dragView.text.toString()[0].isDigit())
                        dragView.infinitive = true
                    if(!dragView.infinitive)
                        vll.addView(dragView)
                    beginIndex = -1
                    true
                }
                else -> true
            }
        }

        var task = task()

        setTaskLayout(task.first)
        setSymbolsLayout()

        skipButton.setOnClickListener {
            skipButton.setText(R.string.skip_button)
            task = task()
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
                if (Solver().solve(left) == Solver().solve(right)) {
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                    skipButton.setText(R.string.next_button)
                }
                else
                    Toast.makeText(this, "Incorrect :(", Toast.LENGTH_SHORT).show()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }


    }
}