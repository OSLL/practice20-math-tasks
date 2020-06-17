package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity: AppCompatActivity() {
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
        var predznazh = "=-+*"
        var Change = true
        if (intent.getStringExtra("mode") == "Расставить цифры") {
            primer.setText("=-+*")
            primer.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
                override fun afterTextChanged(arg: Editable) {
                    if (Change) {
                        var counter1 = 0
                        var counter2 = 0
                        for (j in arg.toString()) if (!j.isDigit() || j in arrayOf('<', '>', '=', '+', '-', '*', '/', '^')) Change = false
                        for (i in 0..(arg.toString().length - 1)) {
                            if (arg.toString()[i] in arrayOf('<', '>', '=', '+', '-', '*', '/', '^')) {
                                counter1 += 1
                            }
                        }
                        for (i in predznazh) {
                            if (i in arrayOf('<', '>', '=', '+', '-', '*', '/', '^')) {
                                counter2 += 1
                            }
                        }
                        if (counter1 != counter2) {
                            Change = false
                        }
                    }
                    else{
                        Change = true
                    }
                    if (!Change) {
                        primer.setText(predznazh)
                    }
                    predznazh = primer.text.toString()
                }
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
            })
        }
    }
}