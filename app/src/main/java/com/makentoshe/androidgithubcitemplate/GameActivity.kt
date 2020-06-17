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
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_help.*


class GameActivity: AppCompatActivity() {

    class MovableTextView(context : Context, val fixed : Boolean) : AppCompatTextView(context) {
        init {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            setTextColor(Color.parseColor("#E65A5A"))




        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            super.onTouchEvent(event)

            return when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val data = ClipData.newPlainText("", "")
                    val dsb = DragShadowBuilder(this)
                    if (Build.VERSION.SDK_INT >= 24) {
                        startDragAndDrop(data, dsb, this, 0);
                    } else {
                        startDrag(data, dsb, this, 0);
                    }
                    visibility = View.INVISIBLE
                    true
                }
                else -> false
            }
        }
    }

    private fun task() : String {
        val hard = when(intent.getStringExtra("difficulty")) {
            "Легкая" -> 1
            "Средняя" -> 2
            else -> 3
        }

        return when(intent.getStringExtra("mode")) {
            "Расставить знаки" -> game1(hard)
            "Расставить цифры" -> game2(hard)
            else -> game1(hard)
        }
    }

    private fun setTaskLayout(task : String) {

        task_layout.removeAllViews()

        var text : MovableTextView

        for(a in task) {
            text = MovableTextView(this, true)
            text.text = a.toString()


            text.performClick()

            task_layout.addView(text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        difficulty.text = difficulty.text.toString() + intent.getStringExtra("difficulty")
        mode.text = mode.text.toString() + intent.getStringExtra("mode")
        val menu = PopupMenu(this, button)
        menu.menu.add(1, 101, 1, "Настройки")
        menu.menu.add(1, 102, 2, "Как играть?")
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

        var index = -1
        val draggingView = TextView(this)
        draggingView.text = "  "
        var beginIndex = 0


        task_layout.setOnDragListener { v, event ->

            val dragView = event.localState as View
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    beginIndex = task_layout.indexOfChild(dragView)
                    index = -1
                    task_layout.removeView(dragView)
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {

                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {

                    if(index == -1) {
                        index = 0
                        for (i in 0 until task_layout.childCount) {
                            if (
                                task_layout.getChildAt(i).x <= event.x
                            ) index = i + 1
                        }

                        task_layout.addView(draggingView, index)

                        println("added view at index $index pivotX = ${event.x} 0pivotX")
                    }
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    task_layout.removeView(draggingView)
                    index = -1
                    true
                }
                DragEvent.ACTION_DROP -> {
                    task_layout.addView(dragView, index)
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    task_layout.removeView(draggingView)
                    dragView.visibility = View.VISIBLE
                    if(!event.result) task_layout.addView(dragView, beginIndex)
                    true
                }
                else -> {
                    // An unknown action type was received.
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
                    false
                }
            }
        }

        var task = task()

        skipButton.setOnClickListener {
            task = task()
            setTaskLayout(task)
        }

        setTaskLayout(task)

    }
}