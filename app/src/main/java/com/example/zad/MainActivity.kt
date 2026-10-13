package com.example.zad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.zad.navigation.AppNavGraph
import com.example.zad.ui.theme.ZadTheme

/**
 * The single Activity that hosts the entire Compose UI.
 *
 * Responsibilities:
 *   - Apply the application theme ([ZadTheme]).
 *   - Provide the root [Scaffold] for edge-to-edge layout.
 *   - Delegate all navigation to [AppNavGraph].
 *
 * Must not contain feature logic, ViewModel instantiation, or direct data access.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZadTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}