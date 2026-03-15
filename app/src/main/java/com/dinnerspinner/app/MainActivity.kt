package com.dinnerspinner.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dinnerspinner.app.ui.navigation.AppNavigation
import com.dinnerspinner.app.ui.theme.DinnerSpinnerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DinnerSpinnerTheme {
                AppNavigation()
            }
        }
    }
}
