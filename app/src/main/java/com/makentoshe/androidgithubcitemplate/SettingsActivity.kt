package com.makentoshe.androidgithubcitemplate

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        exit.setOnClickListener {
            finish()
        }

        val difficulty = arrayOf("Легкая", "Средняя", "Сложная")
        val adapter = ArrayAdapter(this,R.layout.spinner_item,difficulty)
        spinner.adapter = adapter

        val mode = arrayOf("Расставить знаки", "Расставить цифры", "Комбинированный")
        val adapter2 = ArrayAdapter(this,R.layout.spinner_item,mode)
        spinner2.adapter = adapter2
    }
}