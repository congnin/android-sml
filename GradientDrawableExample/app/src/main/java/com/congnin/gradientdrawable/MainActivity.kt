package com.congnin.gradientdrawable

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnProgrammatically = findViewById<Button>(R.id.btn_gradient_programmatically)
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                0XFFD98880.toInt(),
                0XFFF4D03F.toInt(),
                0XFF48C9B0.toInt()
            ))
        btnProgrammatically.background = gradientDrawable

        val btnProgrammaticallyColor1AHalf = findViewById<Button>(R.id.btn_gradient_color1_a_half)
        val gradientDrawableAHalf = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                0XFFD98880.toInt(),
                0XFFD98880.toInt(),
                0XFFF4D03F.toInt(),
                0XFF48C9B0.toInt()
            ))
        btnProgrammaticallyColor1AHalf.background = gradientDrawableAHalf

        val btnProgrammatically5Colors = findViewById<Button>(R.id.btn_gradient_5_colors)
        val gradientDrawable5Colors = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                0XFFD98880.toInt(),
                0XFFF4D03F.toInt(),
                0XFF48C9B0.toInt(),
                0XFF2C3E50.toInt(),
                0XFFAF7AC5.toInt()
            ))
        btnProgrammatically5Colors.background = gradientDrawable5Colors
    }
}