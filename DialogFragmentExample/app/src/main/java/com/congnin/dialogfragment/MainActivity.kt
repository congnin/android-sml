package com.congnin.dialogfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener, MyDialog.DialogListener {
    internal lateinit var btnEmbedDialogFragment: Button
    internal lateinit var btnDialogFragment: Button
    internal lateinit var btnDialogFragmentFullScreen: Button
    internal lateinit var btnAlertDialogFragment: Button
    internal lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        btnEmbedDialogFragment = findViewById(R.id.btnEmbedDialogFragment)
        btnDialogFragment = findViewById(R.id.btnDialogFragment)
        btnDialogFragmentFullScreen = findViewById(R.id.btnDialogFragmentFullScreen)
        btnAlertDialogFragment = findViewById(R.id.btnAlertDialogFragment)
        btnEmbedDialogFragment.setOnClickListener(this)
        btnDialogFragment.setOnClickListener(this)
        btnDialogFragmentFullScreen.setOnClickListener(this)
        btnAlertDialogFragment.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnEmbedDialogFragment -> {
                val dialogFragment = MyDialog()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayout, dialogFragment)
                ft.commit()
            }
            R.id.btnDialogFragment -> {
                val dialogFragment = MyDialog()
                val bundle = Bundle()
                bundle.putBoolean("notAlertDialog", true)
                dialogFragment.arguments = bundle
                val ft = supportFragmentManager.beginTransaction()
                val prev = supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                dialogFragment.show(ft, "dialog")
            }
            R.id.btnDialogFragmentFullScreen -> {
                val dialogFragment = MyDialog()
                val bundle = Bundle()
                bundle.putString("mobile", "0123456789")
                bundle.putBoolean("fullScreen", true)
                bundle.putBoolean("notAlertDialog", true)
                dialogFragment.arguments = bundle
                val ft = supportFragmentManager.beginTransaction()
                val prev = supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                dialogFragment.show(ft, "dialog")
            }
            R.id.btnAlertDialogFragment -> {
                val dialogFragment = MyDialog()
                val ft = supportFragmentManager.beginTransaction()
                val prev = supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                dialogFragment.show(ft, "dialog")
            }
        }
    }

    override fun onFinishEditDialog(inputText: String) {
        if (TextUtils.isEmpty(inputText)) {
            textView.text = "Please enter Mobile Number"
        } else
            textView.text = "Number entered: " + inputText
    }
}