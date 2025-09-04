package com.weighttrackerapp

import android.os.Bundle
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.weighttrackerapp.ui.WeightTrackerApp
import com.weighttrackerapp.ui.theme.WeightTrackerAppTheme
import com.weighttrackerapp.viewmodel.WeightViewModel
import com.weighttrackerapp.viewmodel.WeightViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the system ActionBar to avoid duplicate top bars with Compose's TopAppBar
        actionBar?.hide()
        setContent {
            val context = LocalContext.current.applicationContext as Application
            val viewModel: WeightViewModel =
                ViewModelProvider(this, WeightViewModelFactory(context)).get(WeightViewModel::class.java)

            WeightTrackerAppTheme {
                WeightTrackerApp(viewModel)
            }
        }
    }
}
