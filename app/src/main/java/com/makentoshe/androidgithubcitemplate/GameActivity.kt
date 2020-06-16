package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity: AppCompatActivity() {

    class MovableTextView(context : Context) : AppCompatTextView(context) {
        init {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            setTextColor(Color.parseColor("#E65A5A"))

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
            text = MovableTextView(this)
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

        var task = task()

        skipButton.setOnClickListener {
            task = task()
            setTaskLayout(task)
        }

        setTaskLayout(task)

    }
}