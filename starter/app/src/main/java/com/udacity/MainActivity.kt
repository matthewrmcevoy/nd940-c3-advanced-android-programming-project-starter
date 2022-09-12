package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.reflect.Array.getInt

const val FILE_NAME = "filename"
const val STATUS_EX = "status"

class MainActivity : AppCompatActivity() {
    private val NOTIFICATION_ID = 0
    private val CHANNEL_ID = "download_channel"
    private val CHANNEL_NAME = "Downloads"

    private var downloadID: Long = 0
    private var URL = "NA"

    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager

        createChannel(CHANNEL_ID, CHANNEL_NAME)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val contentIntent = Intent(applicationContext, MainActivity::class.java)

        custom_button.setOnClickListener {
            if(URL=="NA"){
                Toast.makeText(this,"You must select an item first!", Toast.LENGTH_SHORT).show()
            }else {
                download()
                custom_button.buttonState = ButtonState.Loading
                notificationManager.sendNotification("Download Started", applicationContext, contentIntent)
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val contentIntent = Intent(applicationContext, DetailActivity::class.java)
            contentIntent.putExtra(FILE_NAME,when(URL){
                "https://github.com/bumptech/glide/archive/master.zip" -> "Glide-master.zip"
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip" -> "ND940-c3-advanced-android-programming-project-starter-master.zip"
                "https://github.com/square/retrofit/archive/master.zip" -> "retrofit-master.zip"
                else -> "unknown file"
            })
            if(downloadID==id){
                Log.i("FragmentActivity","Download Completed")
                custom_button.buttonState = ButtonState.Completed

                val cursor: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
                while(cursor.moveToNext()){
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when(status){
                        DownloadManager.STATUS_FAILED -> contentIntent.putExtra(STATUS_EX, "Failure")
                        DownloadManager.STATUS_SUCCESSFUL -> contentIntent.putExtra(STATUS_EX, "Successful")
                    }
                }

                notificationManager.sendNotification("Download Completed", applicationContext, contentIntent)
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

    private fun createChannel(channelId: String, channelName: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "download manager"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
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

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
//        private const val URL =
//            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
