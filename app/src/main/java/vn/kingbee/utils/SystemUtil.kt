package vn.kingbee.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Base64
import timber.log.Timber
import vn.kingbee.widget.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec

@SuppressLint("ObsoleteSdkInt")
object SystemUtil {

    private val PASSWORD = "azxsdcvfbgnhmjklploki".toCharArray()

    private val SALT = byteArrayOf(
        0xde.toByte(),
        0x33.toByte(),
        0x10.toByte(),
        0x12.toByte(),
        0xde.toByte(),
        0x33.toByte(),
        0x10.toByte(),
        0x12.toByte()
    )
    private const val PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES"

    fun hasKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= 19
    }

    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= 21
    }

    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= 16
    }

    @Throws(Exception::class)
    fun encryptFile(inputFile: File, encryptedFile: File) {

        val fis = FileInputStream(inputFile)
        val fos = FileOutputStream(encryptedFile)

        val keyFactory = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES)
        val key = keyFactory.generateSecret(PBEKeySpec(PASSWORD))
        val pbeCipher = Cipher.getInstance(PBE_WITH_MD5_AND_DES)
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, PBEParameterSpec(SALT, 20))

        val cos = CipherOutputStream(fos, pbeCipher)

        try {
            var b: Int
            val d = ByteArray(8)
            while ((fis.read(d)) != -1) {
                b = fis.read(d)
                cos.write(d, 0, b)
            }
        } catch (e: Exception) {
            Timber.e(e.message)
        } finally {
            cos.flush()
            cos.close()
            fis.close()
        }

    }

    @Throws(Exception::class)
    fun decryptFile(encryptedFile: File, decryptedFile: File) {

        val fis = FileInputStream(encryptedFile)
        val fos = FileOutputStream(decryptedFile)

        val keyFactory = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES)
        val key = keyFactory.generateSecret(PBEKeySpec(PASSWORD))
        val pbeCipher = Cipher.getInstance(PBE_WITH_MD5_AND_DES)
        pbeCipher.init(Cipher.DECRYPT_MODE, key, PBEParameterSpec(SALT, 20))

        val cis = CipherInputStream(fis, pbeCipher)

        try {
            var b: Int
            val d = ByteArray(8)
            while ((cis.read(d)) != -1) {
                b = cis.read(d)
                fos.write(d, 0, b)
            }

        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            fos.flush()
            fos.close()
            cis.close()
        }
    }

    @Throws(GeneralSecurityException::class, UnsupportedEncodingException::class)
    fun encrypt(property: String): String {
        val keyFactory = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES)
        val key = keyFactory.generateSecret(PBEKeySpec(PASSWORD))
        val pbeCipher = Cipher.getInstance(PBE_WITH_MD5_AND_DES)
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, PBEParameterSpec(SALT, 20))
        return base64Encode(pbeCipher.doFinal(property.toByteArray(charset("UTF-8"))))
    }

    @Throws(GeneralSecurityException::class, UnsupportedEncodingException::class)
    fun decrypt(strEncrypt: String): String {
        var decryptedStrResult = ""
        val encryptionBytes = base64Decode(strEncrypt)
        val keyFactory = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES)
        val key = keyFactory.generateSecret(PBEKeySpec(PASSWORD))
        val pbeCipher = Cipher.getInstance(PBE_WITH_MD5_AND_DES)
        pbeCipher.init(Cipher.DECRYPT_MODE, key, PBEParameterSpec(SALT, 20))
        val decrypt = pbeCipher.doFinal(encryptionBytes)
        decryptedStrResult = String(decrypt)
        return decryptedStrResult
    }

    fun base64Encode(bytes: ByteArray): String {
        val base64Encode = Base64.encodeToString(bytes, Base64.DEFAULT)
        return base64Encode.replace("\\+".toRegex(), "_")
    }

    fun base64Decode(input: String): ByteArray {
        val base64EncodeStr = input.replace("\\_".toRegex(), "+")
        return Base64.decode(base64EncodeStr, Base64.DEFAULT)
    }

    @SuppressLint("PrivateApi", "HardwareIds")
    fun getSerialNumber(application: Application): String {
        var serialNumber = ""
        try {
            val cl = Class.forName("android.os.SystemProperties")
            val getMethod = cl.getMethod("get", String::class.java)

            serialNumber = getMethod.invoke(cl, "gsm.sn1") as String
            if (serialNumber.isEmpty()) {
                serialNumber = getMethod.invoke(cl, "ril.serialnumber") as String
            }
            if (serialNumber.isEmpty()) {
                serialNumber = getMethod.invoke(cl, "ro.serialno") as String
            }
            if (serialNumber.isEmpty()) {
                serialNumber = getMethod.invoke(cl, "sys.serialnumber") as String
            }
            if (serialNumber.isEmpty()) {
                serialNumber = Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)
            }
            if (serialNumber.isEmpty()) {
                serialNumber = Build.SERIAL
            }
        } catch (e: Exception) {
            Timber.e("Get SerialNumber Error: ${e.printStackTrace()}")
        }
        return serialNumber
    }

    fun overridePendingTransactionOnPressed(context: Context) {
        (context as Activity).overridePendingTransition(R.anim.anim_activity_previous, R.anim.anim_activity_previous_release)
    }
}