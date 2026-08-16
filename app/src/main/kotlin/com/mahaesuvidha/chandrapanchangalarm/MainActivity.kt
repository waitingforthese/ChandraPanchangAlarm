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
import androidx.compose.material3.ExperimentalMaterial3Api
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

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            notificationPermission.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        val moonState = LiveMoonCalculator.getCurrentMoonState()

        /*
         * पुढील चंद्र बदलासाठी automatic alarm.
         *
         * पुढच्या step मध्ये LiveMoonCalculator मधून
         * exact timestamp मिळाल्यावर येथे जोडला जाईल.
         */

        setContent {
            MaterialTheme {
                ChandraHome(
                    state = moonState,
                    onTestAlarm = {
                        scheduler.scheduleTestAlarm(10_000L)
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
    onTestAlarm: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🌙 चंद्र पंचांग अलार्म")

                        Text(
                            "V2.2 • LIVE MOON • DAUND",
                            style = MaterialTheme.typography.labelSmall
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                "📍 ${state.location}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                "● Live Calculation: ON",
                style = MaterialTheme.typography.labelMedium
            )

            Card(
                Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "सध्याची चंद्र स्थिती",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("राशी: ${state.rashi.marathi}")
                    Text("नक्षत्र: ${state.nakshatra.marathi}")
                    Text("चरण: ${state.pada}")
                }
            }

            Card(
                Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "पुढील चंद्र बदल",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(state.nextChange)
                    Text("⏰ ${state.nextChangeTime}")
                }
            }

            Button(
                onClick = onTestAlarm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🔔 10 सेकंदांचा Test Alarm")
            }

            Text(
                "V2.2 LIVE BUILD\n" +
                    "चंद्राची राशी, नक्षत्र आणि चरण Live calculation मधून घेतले जात आहेत.\n" +
                    "पुढील बदलासाठी Automatic Alarm scheduling जोडण्याचे काम सुरू आहे.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
