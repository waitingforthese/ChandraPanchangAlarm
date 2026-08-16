package com.mahaesuvidha.chandrapanchangalarm

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.mahaesuvidha.chandrapanchangalarm.alarm.AlarmScheduler
import com.mahaesuvidha.chandrapanchangalarm.model.LiveMoonCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.MoonState

class MainActivity : ComponentActivity() {

    private lateinit var scheduler: AlarmScheduler

    private val notificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Alarm Scheduler तयार करा
        scheduler = AlarmScheduler(this)

        // Notification permission
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            notificationPermission.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        // LIVE Moon State फक्त एकदाच घ्या
        val moonState =
            LiveMoonCalculator.getCurrentMoonState()

        // पुढील LIVE बदलासाठी Alarm Schedule करा
        scheduler.scheduleNextLiveAlarm()

        setContent {

            MaterialTheme {

                ChandraHome(

                    state = moonState,

                    onTestRashiAlarm = {

                        scheduler.scheduleRashiAlarm(
                            10_000L
                        )
                    },

                    onTestNakshatraAlarm = {

                        scheduler.scheduleNakshatraAlarm(
                            10_000L
                        )
                    },

                    onTestCharanAlarm = {

                        scheduler.scheduleCharanAlarm(
                            10_000L
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

    onTestRashiAlarm: () -> Unit,

    onTestNakshatraAlarm: () -> Unit,

    onTestCharanAlarm: () -> Unit
) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            "🌙 चंद्र पंचांग अलार्म"
                        )

                        Text(
                            "V2.5 • LIVE MOON • AUTO ALARM",

                            style =
                                MaterialTheme
                                    .typography
                                    .labelSmall
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(

            modifier =
                Modifier
                    .padding(padding)
                    .padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Text(

                "📍 ${state.location}",

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Text(

                "● Live Calculation: ON",

                style =
                    MaterialTheme
                        .typography
                        .labelMedium
            )


            // सध्याची चंद्र स्थिती

            Card(

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Column(

                    modifier =
                        Modifier.padding(18.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    Text(

                        "सध्याची चंद्र स्थिती",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
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


            // पुढील बदल

            Card(

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Column(

                    modifier =
                        Modifier.padding(18.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    Text(

                        "🔔 पुढील चंद्र बदल",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )

                    Text(
                        state.nextChange
                    )

                    Text(
                        "⏰ ${state.nextChangeTime}"
                    )

                    Text(
                        "Alarm Type: ${state.changeType}"
                    )
                }
            }


            // राशी Test

            Button(

                onClick =
                    onTestRashiAlarm,

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Text(
                    "🌙 राशी बदल Test Alarm"
                )
            }


            // नक्षत्र Test

            Button(

                onClick =
                    onTestNakshatraAlarm,

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Text(
                    "⭐ नक्षत्र बदल Test Alarm"
                )
            }


            // चरण Test

            Button(

                onClick =
                    onTestCharanAlarm,

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Text(
                    "🔔 चरण बदल Test Alarm"
                )
            }


            Text(

                "V2.5 LIVE BUILD\n" +
                        "पुढील राशी, नक्षत्र किंवा चरण बदल " +
                        "आपोआप शोधून Alarm schedule केला जातो.\n\n" +
                        "प्रत्येक बदलासाठी वेगळा Marathi आवाज वापरला जातो.",

                style =
                    MaterialTheme
                        .typography
                        .bodySmall
            )
        }
    }
}
