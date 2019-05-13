package vn.kingbee.mock

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_mock_option_dialog.*
import vn.kingbee.utils.PermissionUtils
import vn.kingbee.widget.R

class MockOptionDialog : AppCompatActivity() {
    private val functionSelections = ArrayList<String>()
    private val caseSelections = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_option_dialog)

        if (!PermissionUtils.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST)
        }

        // default load mock from sdcard if exist else load from assets
        if (!MockAssetManager.isDataInit) {
            MockAssetManager.init(applicationContext)
        }

        functionSelections.addAll(MockAssetManager.getMockOptions().keys)
        spinnerFunctions.adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
            functionSelections)
        spinnerCaseSelection.adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
            caseSelections)
        spinnerFunctions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lastFunctionIndex = position
                val function = functionSelections[position]
                caseSelections.clear()
                val elements = MockAssetManager.getMockOptions()[function]
                if (elements != null) {
                    caseSelections.addAll(elements)
                    (spinnerCaseSelection.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                    spinnerCaseSelection.setSelection(MockAssetManager.getSelectionIndex(function))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //ignore
            }
        }
        spinnerFunctions.setSelection(lastFunctionIndex)

        spinnerCaseSelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val currentFunction = functionSelections[spinnerFunctions.selectedItemPosition]
                MockAssetManager.setSelection(currentFunction, caseSelections[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //ignore
            }
        }

        function_option_mock_all.isChecked = FullMockConfigData.isFullMockSupport()
        function_option_mock_all.setOnCheckedChangeListener { _, isChecked ->
            FullMockConfigData.setFullMockSupport(isChecked)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onDoneClick(view: View) {
        finish()
    }

    companion object {

        private const val MY_PERMISSIONS_REQUEST = 102

        private var lastFunctionIndex = 0

        fun setupMockNotification(context: Context) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupMockNotificationForAndroidO(context)
            } else {
                setupMockNotificationForAndroidUnderO(context)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun setupMockNotificationForAndroidO(context: Context) {
            val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val mockActionIntent = Intent(context, MockOptionDialog::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val mBuilder = Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Mock Options")
                .setContentText("Click to setup mock data")
                .setContentIntent(PendingIntent.getActivity(context, 10, mockActionIntent, 0))
            val channelId = "bee_app_channel_id"
            val channelName = "Bank Channel"
            val importance = NotificationManager.IMPORTANCE_LOW
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            mNotifyMgr.createNotificationChannel(notificationChannel)
            mBuilder.setChannelId(channelId)
            mNotifyMgr.notify(100, mBuilder.build())
        }

        private fun setupMockNotificationForAndroidUnderO(context: Context) {
            val mockActionIntent = Intent(context, MockOptionDialog::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Mock Options")
                .setContentText("Click to setup mock data")
                .setContentIntent(PendingIntent.getActivity(context, 10, mockActionIntent, 0))
            val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr.notify(100, mBuilder.build())
        }
    }
}