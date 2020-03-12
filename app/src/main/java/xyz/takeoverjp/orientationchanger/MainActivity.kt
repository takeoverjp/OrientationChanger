package xyz.takeoverjp.orientationchanger

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mSpChange: Spinner
    private lateinit var mSpNext: Spinner
    private lateinit var mBtGotoNext: Button
    private lateinit var mBtNotification: Button
    private var mNextOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    private fun convertStr2Id(str: String) : Int {
        return when(str) {
            "unspecified" -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            "behind" -> ActivityInfo.SCREEN_ORIENTATION_BEHIND
            "landscape" -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            "portrait" -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            "reverseLandscape" -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            "reversePortrait" -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            "sensorLandscape" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            "sensorPortrait" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            "userLandscape" -> ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
            "userPortrait" -> ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
            "sensor" -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
            "fullSensor" -> ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            "nosensor" -> ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
            "user" -> ActivityInfo.SCREEN_ORIENTATION_USER
            "fullUser" -> ActivityInfo.SCREEN_ORIENTATION_FULL_USER
            "locked" -> ActivityInfo.SCREEN_ORIENTATION_LOCKED
            else -> TODO("Not Implemented$str")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertId2Class(id: Int?): Class<Activity> {
        if (id == null) return UndefinedActivity::class.java as Class<Activity>
        return when(id) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED -> UnspecifiedActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_BEHIND -> BehindActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> LandscapeActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> PortraitActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE -> ReverseLandscapeActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT -> ReversePortraitActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> SensorLandscapeActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT -> SensorPortraitActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE -> UserLandscapeActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT -> UserPortraitActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_SENSOR -> SensorActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR -> FullSensorActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_NOSENSOR -> NosensorActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_USER -> UserActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER -> UserActivity::class.java
            ActivityInfo.SCREEN_ORIENTATION_LOCKED -> LockedActivity::class.java
            else -> TODO("Not Implemented$id")
        } as Class<Activity>
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
                mainActivity.requestedOrientation = convertStr2Id(tv.text.toString())
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
                mNextOrientation = convertStr2Id(tv.text.toString())
                Log.d("Main", "sp_next_orientation: mNextOrientation=$mNextOrientation")
            }
        }
        mBtGotoNext = findViewById(R.id.bt_goto_next)
        mBtGotoNext.setOnClickListener {
            Log.d("Main", "bt_goto_next: mNextOrientation=$mNextOrientation")
            val intent = Intent(mainActivity, convertId2Class(mNextOrientation))
            startActivity(intent)
        }
        mBtNotification = findViewById(R.id.bt_notification)
        mBtNotification.setOnClickListener {
            Log.d("Main", "bt_notification: mNextOrientation=$mNextOrientation")
        }
    }
}
