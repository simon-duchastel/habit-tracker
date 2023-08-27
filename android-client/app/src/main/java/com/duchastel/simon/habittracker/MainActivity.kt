package com.duchastel.simon.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.duchastel.simon.habittracker.repositories.IdentityRepository
import com.duchastel.simon.habittracker.ui.theme.HabitTrackerTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitTrackerTheme {
                val identityRepository = koinInject<IdentityRepository>()
                var userId: String? by remember { mutableStateOf(null) }
                LaunchedEffect(Unit) {
                    userId = identityRepository.userId().also {
                        println("TODO - user id is $it")
                    }
                }
                Text("User id is: ${if (userId != null) { userId } else { "not set yet" }}")

            }
        }
    }
}
