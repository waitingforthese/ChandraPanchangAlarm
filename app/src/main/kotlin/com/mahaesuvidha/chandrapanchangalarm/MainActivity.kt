package com.mahaesuvidha.chandrapanchangalarm

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mahaesuvidha.chandrapanchangalarm.alarm.AlarmScheduler
import com.mahaesuvidha.chandrapanchangalarm.model.MoonState
import com.mahaesuvidha.chandrapanchangalarm.model.Rashi
import com.mahaesuvidha.chandrapanchangalarm.model.Nakshatra

class MainActivity : ComponentActivity() {
    private lateinit var scheduler: AlarmScheduler

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduler = AlarmScheduler(this)

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            MaterialTheme {
                ChandraHome(
                    state = MoonState(
                        location = "दौंड, महाराष्ट्र",
                        rashi = Rashi.KARKA,
                        nakshatra = Nakshatra.PUSHYA,
                        pada = 2,
                        nextChange = "कर्क → सिंह",
                        nextChangeTime = "17 ऑगस्ट 2026, 4:19 PM"
                    ),
                    onTestAlarm = {
                        scheduler.scheduleTestAlarm(10_000L)
                    }
                )
            }
        }
    }
}

@Composable
private fun ChandraHome(state: MoonState, onTestAlarm: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("🌙 चंद्र पंचांग अलार्म") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("📍 ${state.location}", style = MaterialTheme.typography.titleMedium)

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("सध्याची चंद्र स्थिती", style = MaterialTheme.typography.titleLarge)
                    Text("राशी: ${state.rashi.marathi}")
                    Text("नक्षत्र: ${state.nakshatra.marathi}")
                    Text("चरण: ${state.pada}")
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("पुढील राशी बदल", style = MaterialTheme.typography.titleLarge)
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
                "टीप: 17-08-2026, दौंड, 4:19 PM हा validation reference आहे. " +
                    "Production calculation मध्ये ही वेळ hard-code केली जाणार नाही.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
