package com.mahaesuvidha.chandrapanchangalarm

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mahaesuvidha.chandrapanchangalarm.alarm.AlarmScheduler
import com.mahaesuvidha.chandrapanchangalarm.model.LiveMoonCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.MoonState

class MainActivity : ComponentActivity() {

    private lateinit var scheduler: AlarmScheduler

    private val locationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }

    private val notificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduler = AlarmScheduler(this)

        // Notification permission
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            notificationPermission.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        // Location permission
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        val moonState =
            LiveMoonCalculator.getCurrentMoonState()

        setContent {
            MaterialTheme {

                ChandraHome(
                    state = moonState,

                    // राशी Test
                    onRashiTest = {
                        scheduler.scheduleTestAlarm(
                            10_000L,
                            "rashi"
                        )
                    },

                    // नक्षत्र Test
                    onNakshatraTest = {
                        scheduler.scheduleTestAlarm(
                            10_000L,
                            "nakshatra"
                        )
                    },

                    // चरण Test
                    onCharanTest = {
                        scheduler.scheduleTestAlarm(
                            10_000L,
                            "charan"
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChandraHome(
    state: MoonState,
    onRashiTest: () -> Unit,
    onNakshatraTest: () -> Unit,
    onCharanTest: () -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text("🌙 चंद्र पंचांग अलार्म")

                        Text(
                            "V2.4 • LIVE MOON • AUTO ALARM",
                            style =
                                MaterialTheme.typography.labelSmall
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .padding(padding)
                .padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Text(
                "📍 ${state.location}",
                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                "● Live Calculation: ON",
                style =
                    MaterialTheme.typography.labelMedium
            )

            // Current Moon State
            Card(
                Modifier.fillMaxWidth()
            ) {

                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "सध्याची चंद्र स्थिती",
                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Text(
                        "राशी: ${state.rashi.marathi}"
                    )

                    Text(
                        "नक्षत्र: ${state.nakshatra.marathi}"
                    )

                    Text(
                        "चरण: ${state.pada}"
                    )
                }
            }

            // Next Change
            Card(
                Modifier.fillMaxWidth()
            ) {

                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "🔔 पुढील चंद्र बदल",
                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Text(
                        state.nextChange
                    )

                    Text(
                        "⏰ ${state.nextChangeTime}"
                    )
                }
            }

            // Rashi Test
            Button(
                onClick = onRashiTest,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "🔊 राशी बदल Test"
                )
            }

            // Nakshatra Test
            Button(
                onClick = onNakshatraTest,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "🔊 नक्षत्र बदल Test"
                )
            }

            // Charan Test
            Button(
                onClick = onCharanTest,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "🔊 चरण बदल Test"
                )
            }

            Text(
                "V2.4 TEST BUILD\n" +
                    "राशी, नक्षत्र आणि चरण यांचे " +
                    "स्वतंत्र Marathi Voice Alarm Test करता येईल.",
                style =
                    MaterialTheme.typography.bodySmall
            )
        }
    }
}
