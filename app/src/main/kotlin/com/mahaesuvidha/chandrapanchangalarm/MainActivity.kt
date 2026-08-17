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

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        scheduler =
            AlarmScheduler(this)

        // Notification permission
        if (
            android.os.Build.VERSION.SDK_INT >= 33
        ) {

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

        // Current Moon State
        val moonState =
            LiveMoonCalculator
                .getCurrentMoonState()

        // तीनही पुढील alarms schedule
        scheduler.scheduleNextLiveAlarms()

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

@OptIn(
    ExperimentalMaterial3Api::class
)
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
                            "V2.6 • LIVE MOON • AUTO ALARM",
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

            // ----------------------------------------
            // CURRENT MOON
            // ----------------------------------------

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

            // ----------------------------------------
            // NEXT RASHI
            // ----------------------------------------

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        "🌙 पुढील राशी बदल",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )

                    Text(
                        state.nextRashi
                    )

                    Text(
                        "📅 ${state.nextRashiTime}"
                    )
                }
            }

            // ----------------------------------------
            // NEXT NAKSHATRA
            // ----------------------------------------

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        "⭐ पुढील नक्षत्र बदल",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )

                    Text(
                        state.nextNakshatra
                    )

                    Text(
                        "📅 ${state.nextNakshatraTime}"
                    )
                }
            }

            // ----------------------------------------
            // NEXT CHARAN
            // ----------------------------------------

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        "🔔 पुढील चरण बदल",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )

                    Text(
                        state.nextCharan
                    )

                    Text(
                        "📅 ${state.nextCharanTime}"
                    )
                }
            }

            // ----------------------------------------
            // TEST RASHI
            // ----------------------------------------

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

            // ----------------------------------------
            // TEST NAKSHATRA
            // ----------------------------------------

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

            // ----------------------------------------
            // TEST CHARAN
            // ----------------------------------------

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
                "V2.6 LIVE BUILD\n" +
                        "राशी, नक्षत्र आणि प्रत्येक चरणाचा पुढील बदल स्वतंत्रपणे शोधला जातो.\n" +
                        "तिन्ही प्रकारचे Automatic Alarm स्वतंत्रपणे schedule केले जातात.\n" +
                        "प्रत्येक बदलासाठी वेगळा Marathi आवाज वापरला जातो.",

                style =
                    MaterialTheme
                        .typography
                        .bodySmall
            )
        }
    }
}
