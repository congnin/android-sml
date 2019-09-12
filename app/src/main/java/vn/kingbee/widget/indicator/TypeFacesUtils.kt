package vn.kingbee.widget.indicator

import android.content.Context
import android.graphics.Typeface
import timber.log.Timber
import java.util.*

object TypeFacesUtils {
    private val cache = Hashtable<String, Typeface>()

    operator fun get(c: Context, assetPath: String): Typeface? {
        synchronized(cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    val t = Typeface.createFromAsset(c.assets, assetPath)
                    cache.put(assetPath, t)
                } catch (e: Exception) {
                    Timber.e("Could not get typeface because %s", e.message)
                    return null
                }

            }

            return cache.get(assetPath)
        }
    }
}