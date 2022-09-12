package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var URL = "NA"

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if(URL=="NA"){
                Toast.makeText(this,"You must select an item first!", Toast.LENGTH_SHORT).show()
            }else {
                download()
                custom_button.buttonState = ButtonState.Loading
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(downloadID==id){
                Log.i("FragmentActivity","Download Completed")
                custom_button.buttonState = ButtonState.Completed
            }
        }
    }

    fun onRadioSelected(view: View){
        if(view is RadioButton){
            val checked = view.isChecked
            when(view.getId()){
                R.id.glide_radio_bttn -> if(checked){
                    Log.i("Frag","Glide Bttn Checked")
                    custom_button.buttonEnabled = true
                    custom_button.invalidate()
                    URL = "https://github.com/bumptech/glide/archive/master.zip"
                }
                R.id.curRepo_radio_bttn -> if(checked){
                    Log.i("Frag","Current Repo Bttn Checked")
                    custom_button.buttonEnabled = true
                    custom_button.invalidate()
                    URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                }
                R.id.retro_radio_bttn -> if(checked){
                    Log.i("Frag","Retro Bttn Checked")
                    custom_button.buttonEnabled = true
                    custom_button.invalidate()
                    URL = "https://github.com/square/retrofit/archive/master.zip"
                }
            }
        }
    }

    private fun download() {
        Log.i("FragmentActivity","Download called")
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
//        private const val URL =
//            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
