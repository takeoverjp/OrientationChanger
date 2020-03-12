package xyz.takeoverjp.orientationchanger

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    private lateinit var mSpChange: Spinner
    private lateinit var mSpNext: Spinner
    private lateinit var mBtGotoNext: Button
    private lateinit var mBtNotification: Button
    private var mNextOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private data class Orientation(val id: Int, val name: String, val _class: Class<Activity>)

    @Suppress("UNCHECKED_CAST")
    private val orientations = arrayOf(
        Orientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
            "unspecified",
            UnspecifiedActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND,
            "behind",
            BehindActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            "landscape",
            LandscapeActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            "portrait",
            PortraitActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
            "reverseLandscape",
            ReverseLandscapeActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
            "reversePortrait",
            ReversePortraitActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
            "sensorLandscape",
            SensorLandscapeActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,
            "sensorPortrait",
            SensorPortraitActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE,
            "userLandscape",
            UserLandscapeActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
            "userPortrait",
            UserPortraitActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR,
            "sensor",
            SensorActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,
            "fullSensor",
            FullSensorActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,
            "nosensor",
            NosensorActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_USER,
            "user",
            UserActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER,
            "fullUser",
            FullUserActivity::class.java as Class<Activity>),
        Orientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED,
            "locked",
            LockedActivity::class.java as Class<Activity>)
    )

    private fun convertName2Id(name: String) : Int {
        val orientation = orientations.find {it.name == name}
        return orientation!!.id
    }

    private fun convertId2Name(id: Int) : String {
        val orientation = orientations.find {it.id == id}
        return orientation!!.name
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertId2Class(id: Int?): Class<Activity> {
        if (id == null) return UndefinedActivity::class.java as Class<Activity>
        val orientation = orientations.find {it.id == id}
        return orientation!!._class
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSpChange = findViewById(R.id.sp_change_orientation)
        val mainActivity = this
        mSpChange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view == null) return
                val tv = view as TextView
                mainActivity.requestedOrientation = convertName2Id(tv.text.toString())
                Log.d("Main", "sp_change_orientation: activity.requestedOrientation=${mainActivity.requestedOrientation}")
            }
        }
        mSpNext = findViewById(R.id.sp_next_orientation)
        mSpNext.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view == null) return
                val tv = view as TextView
                mNextOrientation = convertName2Id(tv.text.toString())
                Log.d("Main", "sp_next_orientation: mNextOrientation=$mNextOrientation")
            }
        }
        mBtGotoNext = findViewById(R.id.bt_goto_next)
        mBtGotoNext.setOnClickListener {
            Log.d("Main", "bt_goto_next: mNextOrientation=$mNextOrientation")
            val intent = Intent(mainActivity, convertId2Class(mNextOrientation))
            startActivity(intent)
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "start_activity_channel_id",
                "Start Activity Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        mBtNotification = findViewById(R.id.bt_notification)
        mBtNotification.setOnClickListener {
            Log.d("Main", "bt_notification: mNextOrientation=$mNextOrientation")
            val intent = Intent(applicationContext, convertId2Class(mNextOrientation))
            val pendinIntent = PendingIntent.getActivity(applicationContext,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val notification = NotificationCompat.Builder(applicationContext,
            "start_activity_channel_id")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("OrientationChanger")
                .setContentText("Go to ${convertId2Name(mNextOrientation)}($mNextOrientation) Activity")
                .setContentIntent(pendinIntent)
                .build()
            manager.notify(mNextOrientation, notification)
        }
    }
}
