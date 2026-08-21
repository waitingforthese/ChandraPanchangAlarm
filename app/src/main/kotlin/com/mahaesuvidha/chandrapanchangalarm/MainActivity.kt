package com.mahaesuvidha.chandrapanchangalarm
import com.mahaesuvidha.chandrapanchangalarm.model.LiveSunCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.JyotishInfo
import com.mahaesuvidha.chandrapanchangalarm.model.JyotishMaster
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
import com.mahaesuvidha.chandrapanchangalarm.model.SunState
import android.content.Context
import com.mahaesuvidha.chandrapanchangalarm.model.LivePanchangCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.PanchangState
import androidx.compose.foundation.clickable


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

    // LIVE MOON
    val moonState =
        LiveMoonCalculator.getCurrentMoonState()

    // LIVE SUN
    val sunState =
        LiveSunCalculator.getCurrentSunState()

    // LIVE PANCHANG
    val panchangState =
        LivePanchangCalculator.getCurrentPanchangState()

    // Notification Permission
    if (android.os.Build.VERSION.SDK_INT >= 33) {
        notificationPermission.launch(
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

    // Location Permission
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
                sunState = sunState,
                panchangState = panchangState,

                onTestRashi = {
                    scheduler.scheduleTest("राशी बदल")
                },

                onTestNakshatra = {
                    scheduler.scheduleTest("नक्षत्र बदल")
                },

                onTestCharan = {
                    scheduler.scheduleTest("चरण बदल")
                }
            )
        }
    }
}


@Composable
private fun ChandraSuryaHome(
    moonState: MoonState,
    sunState: SunState,
    panchangState: PanchangState,
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
        .padding(vertical = 8.dp),

    verticalAlignment = Alignment.CenterVertically
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
        fontSize = 25.sp,
        modifier = Modifier
            .clickable {
                // Settings click
            }
            .padding(8.dp)
    )
}
        // ------------------------------------------------
        // LOCATION
        // ------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),

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
        // MOON + SUN
        // ------------------------------------------------

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp),

            verticalAlignment =
                Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                MoonColumn(
                    state = moonState,
                    cardColor = moonCardColor,
                    accentColor = moonBlue,
                    textColor = white
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                SunColumn(
                    state = sunState,
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
        // PANCHANG CARD
        // ------------------------------------------------

        PanchangCard(
            state = panchangState
        )

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
            modifier = Modifier.padding(vertical = 6.dp)
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
                .padding(vertical = 10.dp)
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

            val jyotish = JyotishMaster.getInfo(
                state.rashi,
                state.nakshatra,
                state.pada
            )

            PlanetPanel(
                info = jyotish,
                accent = accentColor,
                textColor = textColor
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
    state: SunState,
    cardColor: Color,
    accentColor: Color,
    textColor: Color
) {
    val jyotish = JyotishMaster.getInfo(
        state.rashi,
        state.nakshatra,
        state.pada
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "☀️ सूर्य",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "● LIVE सध्याची स्थिती",
                color = Color(0xFF39D353),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            SmallDataRow("राशी", state.rashi.marathi, textColor)
            SmallDataRow("नक्षत्र", state.nakshatra.marathi, textColor)
            SmallDataRow("चरण", state.pada.toString(), textColor)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 6.dp),
                color = Color.White.copy(alpha = 0.15f)
            )

            Text(
                text = "ग्रह स्वामी",
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            PlanetPanel(
                info = jyotish,
                accent = accentColor,
                textColor = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            NextChangeBlock(
                title = "🔔 पुढील राशी बदल",
                change = "${state.rashi.marathi} → ${state.nextRashi}",
                time = state.nextRashiTime,
                accent = accentColor,
                textColor = textColor
            )

            NextChangeBlock(
                title = "⭐ पुढील नक्षत्र बदल",
                change = "${state.nakshatra.marathi} → ${state.nextNakshatra}",
                time = state.nextNakshatraTime,
                accent = accentColor,
                textColor = textColor
            )

            NextChangeBlock(
                title = "🔔 पुढील चरण बदल",
                change = "चरण ${state.pada} → ${state.nextCharan}",
                time = state.nextCharanTime,
                accent = accentColor,
                textColor = textColor
            )
        }
    }
}

// ==========================================================
// PANCHANG CARD
// ==========================================================

@Composable
private fun PanchangCard(
    state: PanchangState
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // ==================================================
        // TODAY PANCHANG HEADER
        // ==================================================

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(28.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF7F7F7)
            )
        ) {

            Column(
                modifier = Modifier.padding(
                    horizontal = 22.dp,
                    vertical = 18.dp
                )
            ) {

                Text(
                    text = "📅 आजचे पंचांग",

                    color = Color.Black,

                    fontSize = 23.sp,

                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                PanchangRow(
                    label = "तारीख",
                    value = state.date
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                PanchangRow(
                    label = "वार",
                    value = state.weekday
                )
            }
        }


        // ==================================================
        // TITHI
        // ==================================================

        PanchangInfoCard(
            title = "🌙 तिथी",
            current = state.tithi,
            startTime = state.tithiStartTime,
            next = state.nextTithi,
            nextTime = state.nextTithiTime
        )


        // ==================================================
        // YOGA
        // ==================================================

        PanchangInfoCard(
            title = "✨ योग",
            current = state.yoga,
            startTime = state.yogaStartTime,
            next = state.nextYoga,
            nextTime = state.nextYogaTime
        )


        // ==================================================
        // KARANA
        // ==================================================

        PanchangInfoCard(
            title = "🔔 करण",
            current = state.karana,
            startTime = state.karanaStartTime,
            next = state.nextKarana,
            nextTime = state.nextKaranaTime
        )


        // ==================================================
        // PAKSHA
        // ==================================================

        PanchangInfoCard(
            title = "🌗 पक्ष",
            current = state.paksha,
            startTime = state.pakshaStartTime,
            next = state.nextPaksha,
            nextTime = state.nextPakshaTime
        )


        // ==================================================
        // MAS
        // ==================================================

        PanchangInfoCard(
            title = "📅 मास",
            current = state.masa,
            startTime = state.masaStartTime,
            next = state.nextMasa,
            nextTime = state.nextMasaTime
        )


        // ==================================================
        // PRAHAR
        // ==================================================

        PanchangInfoCard(
            title = "⌛ प्रहर",
            current = state.prahar,
            startTime = state.praharStartTime,
            next = state.nextPrahar,
            nextTime = state.nextPraharTime
        )


        // ==================================================
        // LAGNA
        // ==================================================

        PanchangInfoCard(
            title = "⭐ लग्न",
            current = state.lagna,
            startTime = state.lagnaStartTime,
            next = state.nextLagna,
            nextTime = state.nextLagnaTime
        )
    }
}


// ==========================================================
// PANCHANG ROW
// आजचे पंचांग - तारीख / वार
// ==========================================================

@Composable
private fun PanchangRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,

            modifier = Modifier.weight(1f),

            color = Color(0xFF777777),

            fontSize = 19.sp,

            fontWeight = FontWeight.Medium
        )


        Text(
            text = if (value.isBlank()) "—" else value,

            color = Color(0xFF202020),

            fontSize = 20.sp,

            fontWeight = FontWeight.Bold,

            textAlign = TextAlign.End
        )
    }
}


// ==========================================================
// COMPACT PANCHANG INFO CARD
// ==========================================================

@Composable
private fun PanchangInfoCard(
    title: String,
    current: String,
    startTime: String,
    next: String,
    nextTime: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(26.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F7F7)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 12.dp
                )
        ) {

            // ==================================================
            // TITLE + CURRENT
            // ==================================================

            Row(
                modifier = Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = title,

                    color = Color(0xFF006B85),

                    fontSize = 21.sp,

                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.weight(1f)
                )

                Text(
                    text =
                        if (current.isBlank())
                            "—"
                        else
                            current,

                    color = Color.Black,

                    fontSize = 21.sp,

                    fontWeight = FontWeight.Bold,

                    textAlign = TextAlign.End
                )
            }


            Spacer(
                modifier = Modifier.height(7.dp)
            )


            HorizontalDivider(
                color = Color.LightGray
            )


            Spacer(
                modifier = Modifier.height(7.dp)
            )


            // ==================================================
            // प्रारंभ
            // ==================================================

            PanchangInfoRow(
                icon = "🟢",
                label = "प्रारंभ",
                value =
                    if (startTime.isBlank())
                        "—"
                    else
                        startTime,

                labelColor = Color(0xFF16803B),

                valueColor = Color(0xFF555555)
            )


            // ==================================================
            // पुढील बदल
            // ==================================================

            PanchangInfoRow(
                icon = "🔔",
                label = "पुढील बदल",
                value =
                    if (
                        next.isBlank() ||
                        next == "—"
                    ) {
                        "—"
                    } else {
                        "$current → $next"
                    },

                labelColor = Color(0xFF006B85),

                valueColor = Color.Black
            )


            // ==================================================
            // समाप्त
            // ==================================================

            PanchangInfoRow(
                icon = "🔴",
                label = "समाप्त",
                value =
                    if (nextTime.isBlank())
                        "—"
                    else
                        nextTime,

                labelColor = Color(0xFFC62828),

                valueColor = Color(0xFF555555)
            )
        }
    }
}


// ==========================================================
// COMPACT PANCHANG INFO ROW
// ==========================================================

@Composable
private fun PanchangInfoRow(
    icon: String,
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // --------------------------------------------------
        // ICON
        // --------------------------------------------------

        Text(
            text = icon,

            fontSize = 22.sp,

            modifier = Modifier.width(40.dp)
        )


        // --------------------------------------------------
        // LABEL
        // --------------------------------------------------

        Text(
            text = label,

            color = labelColor,

            fontSize = 18.sp,

            fontWeight = FontWeight.Bold,

            modifier = Modifier.width(115.dp)
        )


        // --------------------------------------------------
        // VALUE
        // --------------------------------------------------

        Text(
            text =
                if (value.isBlank())
                    "—"
                else
                    value,

            color = valueColor,

            fontSize = 17.sp,

            fontWeight = FontWeight.Medium,

            textAlign = TextAlign.End,

            modifier = Modifier.weight(1f)
        )
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


@Composable
private fun PlanetPanel(
    info: JyotishInfo,
    accent: Color,
    textColor: Color
) {
    Text("राशी: ${info.rashiLord}", color = textColor, fontSize = 12.sp)
    Text("नक्षत्र: ${info.nakshatraLord}", color = textColor, fontSize = 12.sp)
    Text("नवांश: ${info.navamshaRashi}", color = textColor, fontSize = 12.sp)
    Text("नवांश स्वामी: ${info.navamshaLord}", color = textColor, fontSize = 12.sp)

    if (info.enemies.isNotEmpty()) {
        Text(
            text = "⚠️ विरोधी ग्रह: ${info.enemies.joinToString(", ")}",
            color = accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun NextChangeBlock(
    title: String,
    change: String,
    time: String,
    accent: Color,
    textColor: Color
) {
    Text(title, color = accent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    Text(change, color = textColor, fontSize = 12.sp)
    Text("📅 $time", color = Color.LightGray, fontSize = 11.sp)
    Spacer(modifier = Modifier.height(8.dp))
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
}
