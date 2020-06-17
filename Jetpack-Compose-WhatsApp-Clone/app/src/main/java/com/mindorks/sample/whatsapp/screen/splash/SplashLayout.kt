package com.mindorks.sample.whatsapp.screen.splash

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.tag
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.drawBackground
import androidx.ui.graphics.Color
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.ConstraintSet
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontStyle
import com.mindorks.sample.whatsapp.R
import com.mindorks.sample.whatsapp.common.*
import com.mindorks.sample.whatsapp.navigator.Navigator
import com.mindorks.sample.whatsapp.navigator.navigateTo

private const val SURFACE_TAG = "surface"
private const val TEXT_FROM_TAG = "textFrom"
private const val TEXT_COMPANY_TAG = "textCompany"
private const val IMAGE_LOGO_TAG = "imageLogo"

@Composable
fun SplashLayout(launchHomeView: Boolean, scaffoldState: ScaffoldState) {

    Scaffold(
        scaffoldState = scaffoldState,
        bodyContent = {
            ConstraintLayout(ConstraintSet {

                val surface = tag(SURFACE_TAG)
                val textFrom = tag(TEXT_FROM_TAG)
                val textCompany = tag(TEXT_COMPANY_TAG)
                val imageLogo = tag(IMAGE_LOGO_TAG)

                surface.apply {
                    left constrainTo parent.left
                    top constrainTo parent.top
                    right constrainTo parent.right
                    bottom constrainTo parent.bottom
                }
                imageLogo.apply {
                    left constrainTo parent.left
                    top constrainTo parent.top
                    right constrainTo parent.right
                    bottom constrainTo parent.bottom
                    width to wrap
                    height to wrap
                }
                textCompany.apply {
                    bottom constrainTo parent.bottom
                    right constrainTo parent.right
                    left constrainTo parent.left
                }
                textFrom.apply {
                    bottom constrainTo textCompany.top
                    right constrainTo textCompany.right
                    left constrainTo textCompany.left
                }

            }) {
                Box(
                    modifier = Modifier.fillMaxSize() +
                            Modifier.drawBackground(colorLightGreen()) +
                            Modifier.tag(SURFACE_TAG)
                )
                Text(
                    text = "from",
                    modifier = Modifier.tag(TEXT_FROM_TAG) + Modifier.padding(bottom = _8dp),
                    color = Color.DarkGray
                )
                spacer()
                Text(
                    text = "FACEBOOK",
                    style = TextStyle(
                        fontSize = _20sp,
                        fontFamily = FontFamily.SansSerif,
                        fontStyle = FontStyle.Normal
                    ),
                    modifier = Modifier.tag(TEXT_COMPANY_TAG) + Modifier.padding(bottom = _16dp),
                    color = colorGreen()
                )

                ImageLoader.load(
                    imageUrl = R.drawable.ic_whatsapp,
                    modifier = Modifier.tag(IMAGE_LOGO_TAG)
                ) {}

                if (launchHomeView) {
                    navigateTo(Navigator.HomeScreen)
                }
            }
        }
    )
}