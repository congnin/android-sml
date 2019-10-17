package vn.kingbee.feature.workers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber
import vn.kingbee.widget.R
import java.util.*

class SelectImageActivity : AppCompatActivity() {

    private var mPermissionRequestCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_selection)

        if (savedInstanceState != null) {
            mPermissionRequestCount = savedInstanceState.getInt(KEY_PERMISSIONS_REQUEST_COUNT, 0)
        }

        // Make sure the app has correct permissions to run
        requestPermissionsIfNecessary()

        // Create request to get image from filesystem when button clicked
        findViewById<View>(R.id.selectImage).setOnClickListener {
            val chooseIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PERMISSIONS_REQUEST_COUNT, mPermissionRequestCount)
    }

    /**
     * Request permissions twice - if the user denies twice then show a toast about how to update
     * the permission for storage. Also disable the button if we don't have access to pictures on
     * the device.
     */
    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            if (mPermissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                mPermissionRequestCount++
                ActivityCompat.requestPermissions(
                    this,
                    sPermissions.toTypedArray(),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                Toast.makeText(
                    this, R.string.set_permissions_in_settings,
                    Toast.LENGTH_LONG
                ).show()
                findViewById<View>(R.id.selectImage).isEnabled = false
            }
        }
    }

    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in sPermissions) {
            hasPermissions = hasPermissions and (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED)
        }
        return hasPermissions
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            requestPermissionsIfNecessary() // no-op if permissions are granted already.
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> handleImageRequestResult(data)
                else -> Timber.tag(TAG).d("Unknown request code.")
            }
        } else {
            Timber.tag(TAG).e("Unexpected Result code %s", resultCode)
        }
    }

    private fun handleImageRequestResult(data: Intent) {
        var imageUri: Uri? = null
        if (data.clipData != null) {
            imageUri = data.clipData!!.getItemAt(0).uri
        } else if (data.data != null) {
            imageUri = data.data
        }

        if (imageUri == null) {
            Timber.tag(TAG).e("Invalid input image Uri.")
            return
        }

        val filterIntent = Intent(this, BlurActivity::class.java)
        filterIntent.putExtra(Constants.KEY_IMAGE_URI, imageUri.toString())
        startActivity(filterIntent)
    }

    companion object {
        private const val TAG = "SelectImageActivity"

        private const val REQUEST_CODE_IMAGE = 100
        private const val REQUEST_CODE_PERMISSIONS = 101

        private const val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
        private const val MAX_NUMBER_REQUEST_PERMISSIONS = 2

        private val sPermissions: List<String> = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}