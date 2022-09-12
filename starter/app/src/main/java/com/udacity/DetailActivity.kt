package com.udacity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val status = intent.getStringExtra(STATUS_EX)
        val fileN = intent.getStringExtra(FILE_NAME)

        file_title_txt.text = fileN
        status_txt.text = status

    }





    fun backToMain(view: View){
        startActivity(Intent(this,MainActivity::class.java))

    }

}
