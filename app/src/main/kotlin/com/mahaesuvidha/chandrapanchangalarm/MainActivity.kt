package com.mahaesuvidha.chandrapanchangalarm
import com.mahaesuvidha.chandrapanchangalarm.model.LiveSunCalculator
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahaesuvidha.chandrapanchangalarm.alarm.AlarmScheduler
import com.mahaesuvidha.chandrapanchangalarm.model.LiveMoonCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.MoonState

class MainActivity : ComponentActivity() {

    private lateinit var scheduler: AlarmScheduler

    private val notificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    private val locationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduler = AlarmScheduler(this)

        // Current LIVE Moon calculation
        val moonState =
            LiveMoonCalculator.getCurrentMoonState()
        val sunState =
    LiveSunCalculator.getCurrentSunState()

        // Existing automatic Moon alarm
      // scheduler.scheduleNextLiveAlarm()

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

        setContent {

            MaterialTheme {

                ChandraSuryaHome(
                    moonState = moonState,

                    onTestRashi = {
                        scheduler.scheduleRashiAlarm(
                            10_000L
                        )
                    },

                    onTestNakshatra = {
                        scheduler.scheduleNakshatraAlarm(
                            10_000L
                        )
                    },

                    onTestCharan = {
                        scheduler.scheduleCharanAlarm(
                            10_000L
                        )
                    }
                )
            }
        }
    }
}


@Composable
private fun ChandraSuryaHome(
    moonState: MoonState,
    onTestRashi: () -> Unit,
    onTestNakshatra: () -> Unit,
    onTestCharan: () -> Unit
) {

    val backgroundColor =
        Color(0xFF07111F)

    val moonCardColor =
        Color(0xFF0B2038)

    val sunCardColor =
        Color(0xFF211A08)

    val gold =
        Color(0xFFFFC83D)

    val moonBlue =
        Color(0xFF4DA3FF)

    val white =
        Color(0xFFF5F7FA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(12.dp)
    ) {

        // ------------------------------------------------
        // HEADER
        // ------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "🌙",
                fontSize = 38.sp
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "चंद्र सूर्य अलार्म",
                    color = white,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "LIVE • AUTO • ACCURATE",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "⚙️",
                fontSize = 25.sp
            )
        }


        // ------------------------------------------------
        // LOCATION
        // ------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 4.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "📍 ${moonState.location}",
                color = white,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "● LIVE",
                color = Color(0xFF39D353),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        // ------------------------------------------------
        // MOON + SUN TWO COLUMN
        // ------------------------------------------------

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp),

            verticalAlignment =
                Alignment.Top
        ) {

            // ==================================================
            // MOON COLUMN
            // ==================================================

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                MoonColumn(
                    state = moonState,
                    cardColor = moonCardColor,
                    accentColor = moonBlue,
                    textColor = white
                )
            }


            // ==================================================
            // SUN COLUMN
            // ==================================================

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                SunColumn(
                    cardColor = sunCardColor,
                    accentColor = gold,
                    textColor = white
                )
            }
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // ------------------------------------------------
        // TEST BUTTONS
        // ------------------------------------------------

        Text(
            text = "🔔 अलार्म टेस्ट",
            color = white,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                vertical = 6.dp
            )
        )

        TestButton(
            text = "🌙 राशी बदल Test",
            onClick = onTestRashi
        )

        TestButton(
            text = "⭐ नक्षत्र बदल Test",
            onClick = onTestNakshatra
        )

        TestButton(
            text = "🔔 चरण बदल Test",
            onClick = onTestCharan
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // ------------------------------------------------
        // FOOTER
        // ------------------------------------------------

        Text(
            text =
                "चंद्र सूर्य अलार्म\n" +
                "LIVE Calculation • Auto Alarm",

            color = Color.Gray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 10.dp
                )
        )
    }
}


// ==========================================================
// MOON COLUMN
// ==========================================================

@Composable
private fun MoonColumn(
    state: MoonState,
    cardColor: Color,
    accentColor: Color,
    textColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = cardColor
            )
    ) {

        Column(
            modifier =
                Modifier.padding(10.dp)
        ) {

            Text(
                text = "🌙 चंद्र",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "● LIVE  सध्याची स्थिती",
                color = Color(0xFF39D353),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // ----------------------------------------------
            // CURRENT DATA
            // ----------------------------------------------

            SmallDataRow(
                label = "राशी",
                value = state.rashi.marathi,
                color = textColor
            )

            SmallDataRow(
                label = "नक्षत्र",
                value = state.nakshatra.marathi,
                color = textColor
            )

            SmallDataRow(
                label = "चरण",
                value = state.pada.toString(),
                color = textColor
            )


            Divider(
                modifier =
                    Modifier.padding(
                        vertical = 6.dp
                    ),

                color =
                    Color.White.copy(
                        alpha = 0.15f
                    )
            )


            // ----------------------------------------------
            // PLANET LORD
            // ----------------------------------------------

            Text(
                text = "ग्रह स्वामी",
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "राशी: —",
                color = textColor,
                fontSize = 12.sp
            )

            Text(
                text = "नक्षत्र: —",
                color = textColor,
                fontSize = 12.sp
            )

            Text(
                text = "नवांश: —",
                color = textColor,
                fontSize = 12.sp
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            // ----------------------------------------------
            // NEXT CHANGES
            // ----------------------------------------------

            Text(
                text = "🔔 पुढील बदल",
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

           Text(
    text = "🌙 पुढील राशी बदल",
    color = accentColor,
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold
)

Text(
    text = "${state.rashi.marathi} → ${state.nextRashi}",
    color = textColor,
    fontSize = 12.sp
)

Text(
    text = "📅 ${state.nextRashiTime}",
    color = Color.LightGray,
    fontSize = 11.sp
)

Spacer(modifier = Modifier.height(8.dp))

Text(
    text = "⭐ पुढील नक्षत्र बदल",
    color = accentColor,
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold
)

Text(
    text = "${state.nakshatra.marathi} → ${state.nextNakshatra}",
    color = textColor,
    fontSize = 12.sp
)

Text(
    text = "📅 ${state.nextNakshatraTime}",
    color = Color.LightGray,
    fontSize = 11.sp
)

Spacer(modifier = Modifier.height(8.dp))

Text(
    text = "🔔 पुढील चरण बदल",
    color = accentColor,
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold
)

Text(
    text = "चरण ${state.pada} → ${state.nextCharan}",
    color = textColor,
    fontSize = 12.sp
)

Text(
    text = "📅 ${state.nextCharanTime}",
    color = Color.LightGray,
    fontSize = 11.sp
)

        }
    }
}


// ==========================================================
// SUN COLUMN
// ==========================================================

@Composable
private fun SunColumn(
    cardColor: Color,
    accentColor: Color,
    textColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = cardColor
            )
    ) {

        Column(
            modifier =
                Modifier.padding(10.dp)
        ) {

            Text(
                text = "☀️ सूर्य",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "● LIVE  सध्याची स्थिती",
                color = Color(0xFF39D353),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )


            SmallDataRow(
                label = "राशी",
                value = "—",
                color = textColor
            )

            SmallDataRow(
                label = "नक्षत्र",
                value = "—",
                color = textColor
            )

            SmallDataRow(
                label = "चरण",
                value = "—",
                color = textColor
            )


            Divider(
                modifier =
                    Modifier.padding(
                        vertical = 6.dp
                    ),

                color =
                    Color.White.copy(
                        alpha = 0.15f
                    )
            )


            Text(
                text = "ग्रह स्वामी",
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "राशी: —",
                color = textColor,
                fontSize = 12.sp
            )

            Text(
                text = "नक्षत्र: —",
                color = textColor,
                fontSize = 12.sp
            )

            Text(
                text = "नवांश: —",
                color = textColor,
                fontSize = 12.sp
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            Text(
                text = "🔔 पुढील बदल",
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "सूर्य LIVE गणना जोडणार",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "📅 —",
                color = Color.LightGray,
                fontSize = 11.sp
            )
        }
    }
}


// ==========================================================
// SMALL DATA ROW
// ==========================================================

@Composable
private fun SmallDataRow(
    label: String,
    value: String,
    color: Color
) {

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 4.dp
                ),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 12.sp
        )

        Text(
            text = value,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}


// ==========================================================
// TEST BUTTON
// ==========================================================

@Composable
private fun TestButton(
    text: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 3.dp
                ),

        shape =
            RoundedCornerShape(14.dp)
    ) {

        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
