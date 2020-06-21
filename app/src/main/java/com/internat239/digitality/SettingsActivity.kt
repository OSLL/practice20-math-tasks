package com.internat239.digitality

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        exit.setOnClickListener {
            finish()
        }

        gameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("difficulty", spinner.selectedItemPosition)
            intent.putExtra("difficulty-name", spinner.selectedItem.toString())
            intent.putExtra("mode", spinner2.selectedItemPosition)
            intent.putExtra("mode-name", spinner2.selectedItem.toString())
            startActivity(intent)
        }
        //Подчёркивание
        //textView4.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        //textView5.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}