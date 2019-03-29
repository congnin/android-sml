package vn.kingbee.widget.utils

import android.content.Context
import android.os.Build
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.app.Activity
import android.support.v4.content.ContextCompat

class PermissionUtils {
    companion object {
        fun isRuntimePermissionRequired(): Boolean {
            return Build.VERSION.SDK_INT >= 23
        }

        fun isGranted(context: Context, permissionName: String): Boolean {
            return ActivityCompat
                .checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED
        }

        fun isPermissionsGranted(
            activity: Activity,
            permissions: Array<out String>,
            requestCode: Int
        ): Boolean {

            val requestPermission = ArrayList<String>()

            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission.add(permission)
                }
            }

            if (!requestPermission.isEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    requestPermission.toTypedArray(),
                    requestCode
                )
                return false
            }

            return true
        }

        fun isPermissionsGranted(
            activity: Activity,
            requestCode: Int,
            vararg permissions: String
        ): Boolean {
            return isPermissionsGranted(activity, permissions, requestCode)
        }

        fun isResultPermissionsGranted(vararg grantResults: Int): Boolean {
            if (grantResults.isNotEmpty()) {
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
                return true
            } else {
                return false
            }
        }
    }
}