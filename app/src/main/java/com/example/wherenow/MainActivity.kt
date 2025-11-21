package com.example.wherenow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.navigation.NavComposable
import com.example.wherenow.ui.theme.WherenowTheme
import com.example.wherenow.util.FirestoreSeeder
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*FirestoreSeeder.seedEvents { ok ->
            if (ok) Log.d("SEED", "Eventos insertados correctamente")
            else Log.e("SEED", "Error insertando eventos")
        }*/
        enableEdgeToEdge()
        setContent {
            WherenowTheme {
                //nav controller
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavComposable( //navhost indirecto
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
