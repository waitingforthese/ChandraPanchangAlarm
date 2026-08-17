package com.mahaesuvidha.chandrapanchangalarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

import com.google.android.gms.location.LocationServices

import com.mahaesuvidha.chandrapanchangalarm.alarm.AlarmScheduler
import com.mahaesuvidha.chandrapanchangalarm.model.LiveMoonCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.LiveSunCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.MoonState
import com.mahaesuvidha.chandrapanchangalarm.model.SunState
import com.mahaesuvidha.chandrapanchangalarm.settings.AlarmPrefs

import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private lateinit var scheduler: AlarmScheduler

    private var locationText by mutableStateOf(
        "📍 स्थान शोधत आहे…"
    )

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            readLocation()
        }

    private val notificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        scheduler = AlarmScheduler(this)

        if (
            android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.TIRAMISU
        ) {
            notificationPermission.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        readLocation()

        scheduler.scheduleAll()

        setContent {
            MaterialTheme {
                MainApp(
                    location = locationText,
                    scheduler = scheduler
                )
            }
        }
    }

    private fun readLocation() {

        val permission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        if (
            permission !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        LocationServices
            .getFusedLocationProviderClient(this)
            .lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {

                    locationText =
                        "📍 ${"%.4f".format(location.latitude)}, " +
                        "${"%.4f".format(location.longitude)} • LIVE"

                } else {

                    locationText =
                        "📍 दौंड, महाराष्ट्र • LIVE"
                }
            }
    }
}


@Composable
private fun MainApp(
    location: String,
    scheduler: AlarmScheduler
) {

    var moonState by remember {
        mutableStateOf(
            LiveMoonCalculator.getCurrentMoonState()
        )
    }

    var sunState by remember {
        mutableStateOf(
            LiveSunCalculator.getCurrentSunState()
        )
    }

    val context = LocalContext.current

    val prefs = remember {
        AlarmPrefs(context)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        while (true) {

            delay(30_000)

            moonState =
                LiveMoonCalculator.getCurrentMoonState()

            sunState =
                LiveSunCalculator.getCurrentSunState()
        }
    }

    val backgroundColor =
        Color(0xFF081522)

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(backgroundColor)
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(16.dp)
        ) {

            Header(
                onSettingsClick = {
                    showSettings = true
                }
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text = location,
                color = Color(0xFF39D353),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                MoonCard(
                    modifier =
                        Modifier.weight(1f),

                    state = moonState
                )

                SunCard(
                    modifier =
                        Modifier.weight(1f),

                    state = sunState
                )
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Text(
                text = "🔔 अलार्म टेस्ट",

                color = Color.White,

                fontSize = 22.sp,

                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            TestButton(
                text = "🌙 राशी बदल Test Alarm"
            ) {
                scheduler.scheduleTest("राशी")
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            TestButton(
                text = "⭐ नक्षत्र बदल Test Alarm"
            ) {
                scheduler.scheduleTest("नक्षत्र")
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            TestButton(
                text = "🔔 चरण बदल Test Alarm"
            ) {
                scheduler.scheduleTest("चरण")
            }

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            Text(
                text =
                    "LIVE calculation प्रत्येक 30 सेकंदांनी अपडेट होते.",

                color = Color.LightGray,

                fontSize = 12.sp
            )

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )
        }

        if (showSettings) {

            SettingsDialog(
                prefs = prefs,

                onSave = {

                    scheduler.scheduleAll()

                    showSettings = false
                },

                onClose = {

                    showSettings = false
                }
            )
        }
    }
}


@Composable
private fun Header(
    onSettingsClick: () -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

       Column(
    Modifier.fillMaxWidth(0.82f)
        ) {

            Text(
                text =
                    "🌙 चंद्र सूर्य अलार्म",

                color =
                    Color.White,

                fontSize =
                    26.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "LIVE • AUTO • ACCURATE",

                color =
                    Color.LightGray,

                fontSize =
                    14.sp
            )
        }

        Text(
            text =
                "⚙️",

            fontSize =
                30.sp,

            modifier =
                Modifier.clickable {
                    onSettingsClick()
                }
        )
    }
}


@Composable
private fun MoonCard(
    modifier: Modifier,
    state: MoonState
) {

    Card(
        modifier = modifier,

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFF102B40)
            ),

        shape =
            RoundedCornerShape(22.dp)
    ) {

        Column(
            modifier =
                Modifier.padding(14.dp)
        ) {

            Text(
                text =
                    "🌙 चंद्र",

                color =
                    Color.White,

                fontSize =
                    23.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "● LIVE सध्याची स्थिती",

                color =
                    Color(0xFF39D353),

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            InfoRow(
                label = "राशी",
                value = state.rashi.marathi
            )

            InfoRow(
                label = "नक्षत्र",
                value = state.nakshatra.marathi
            )

            InfoRow(
                label = "चरण",
                value = state.pada.toString()
            )

            Divider(
                modifier =
                    Modifier.padding(
                        vertical = 12.dp
                    )
            )

            Text(
                text =
                    "ग्रह स्वामी",

                color =
                    Color(0xFF72B7FF),

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    18.sp
            )

           val jyotish =
    JyotishMaster.getInfo(
        state.rashi,
        state.nakshatra,
        state.pada
    )

PlanetPanel(
    info = jyotish,
    accent = Color(0xFF72B7FF)
)
            

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            NextChange(
                title =
                    "🔔 पुढील राशी बदल",

                change =
                    state.nextRashi,

                time =
                    state.nextRashiTime,

                color =
                    Color(0xFF72B7FF)
            )

            NextChange(
                title =
                    "⭐ पुढील नक्षत्र बदल",

                change =
                    state.nextNakshatra,

                time =
                    state.nextNakshatraTime,

                color =
                    Color(0xFF72B7FF)
            )

            NextChange(
                title =
                    "🔔 पुढील चरण बदल",

                change =
                    state.nextCharan,

                time =
                    state.nextCharanTime,

                color =
                    Color(0xFF72B7FF)
            )
        }
    }
}


@Composable
private fun SunCard(
    modifier: Modifier,
    state: SunState
) {

    Card(
        modifier = modifier,

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFF332703)
            ),

        shape =
            RoundedCornerShape(22.dp)
    ) {

        Column(
            modifier =
                Modifier.padding(14.dp)
        ) {

            Text(
                text =
                    "☀️ सूर्य",

                color =
                    Color.White,

                fontSize =
                    23.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "● LIVE सध्याची स्थिती",

                color =
                    Color(0xFF39D353),

                fontSize =
                    13.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            InfoRow(
                label = "राशी",
                value = state.rashi.marathi
            )

            InfoRow(
                label = "नक्षत्र",
                value = state.nakshatra.marathi
            )

            InfoRow(
                label = "चरण",
                value = state.pada.toString()
            )

            Divider(
                modifier =
                    Modifier.padding(
                        vertical = 12.dp
                    )
            )

            Text(
                text =
                    "ग्रह स्वामी",

                color =
                    Color(0xFFFFC857),

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    18.sp
            )

           val jyotish =
    JyotishMaster.getInfo(
        state.rashi,
        state.nakshatra,
        state.pada
    )

PlanetPanel(
    info = jyotish,
    accent = Color(0xFF72B7FF)
)
            

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            NextChange(
                title =
                    "🔔 पुढील राशी बदल",

                change =
                    state.nextRashi,

                time =
                    state.nextRashiTime,

                color =
                    Color(0xFFFFC857)
            )

            NextChange(
                title =
                    "⭐ पुढील नक्षत्र बदल",

                change =
                    state.nextNakshatra,

                time =
                    state.nextNakshatraTime,

                color =
                    Color(0xFFFFC857)
            )

            NextChange(
                title =
                    "🔔 पुढील चरण बदल",

                change =
                    state.nextCharan,

                time =
                    state.nextCharanTime,

                color =
                    Color(0xFFFFC857)
            )
        }
    }
}


@Composable
private fun InfoRow(
    label: String,
    value: String
) {

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,

            color =
                Color.LightGray,

            fontSize =
                15.sp
        )

        Text(
            text = value,

            color =
                Color.White,

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


@Composable
private fun NextChange(
    title: String,
    change: String,
    time: String,
    color: Color
) {

    Text(
        text = title,

        color = color,

        fontSize = 17.sp,

        fontWeight = FontWeight.Bold
    )

    Spacer(
        modifier =
            Modifier.height(4.dp)
    )

    Text(
        text = change,

        color =
            Color.White,

        fontSize =
            15.sp
    )

    Text(
        text = "📅 $time",

        color =
            Color.LightGray,

        fontSize =
            14.sp
    )

    Spacer(
        modifier =
            Modifier.height(14.dp)
    )
}


@Composable
private fun TestButton(
    text: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            text = text,

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


@Composable
private fun SettingsDialog(
    prefs: AlarmPrefs,
    onSave: () -> Unit,
    onClose: () -> Unit
) {

    var moonEnabled by remember {
        mutableStateOf(prefs.moon)
    }

    var sunEnabled by remember {
        mutableStateOf(prefs.sun)
    }

    var rashiEnabled by remember {
        mutableStateOf(prefs.rashi)
    }

    var nakshatraEnabled by remember {
        mutableStateOf(prefs.nak)
    }

    var padaEnabled by remember {
        mutableStateOf(prefs.pada)
    }

    AlertDialog(

        onDismissRequest =
            onClose,

        title = {

            Text(
                text =
                    "⚙️ अलार्म सेटिंग्स"
            )
        },

        text = {

            Column {

                ToggleRow(
                    text =
                        "🌙 चंद्र अलार्म",

                    checked =
                        moonEnabled
                ) {
                    moonEnabled = it
                }

                ToggleRow(
                    text =
                        "☀️ सूर्य अलार्म",

                    checked =
                        sunEnabled
                ) {
                    sunEnabled = it
                }

                ToggleRow(
                    text =
                        "राशी बदल",

                    checked =
                        rashiEnabled
                ) {
                    rashiEnabled = it
                }

                ToggleRow(
                    text =
                        "नक्षत्र बदल",

                    checked =
                        nakshatraEnabled
                ) {
                    nakshatraEnabled = it
                }

                ToggleRow(
                    text =
                        "चरण बदल",

                    checked =
                        padaEnabled
                ) {
                    padaEnabled = it
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    prefs.moon =
                        moonEnabled

                    prefs.sun =
                        sunEnabled

                    prefs.rashi =
                        rashiEnabled

                    prefs.nak =
                        nakshatraEnabled

                    prefs.pada =
                        padaEnabled

                    onSave()
                }
            ) {

                Text(
                    "Save"
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onClose
            ) {

                Text(
                    "Cancel"
                )
            }
        }
    )
}


@Composable
private fun ToggleRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (
        Boolean
    ) -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = text,

            modifier =
                Modifier.weight(1f)
        )

        Switch(
            checked =
                checked,

            onCheckedChange =
                onCheckedChange
        )
    }
}
@Composable
private fun PlanetPanel(
    info: JyotishInfo,
    accent: Color
) {

    Text(
        text = "ग्रह स्वामी",
        color = accent,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(
        Modifier.height(6.dp)
    )

    PlanetLine(
        label = "राशी",
        planet = info.rashiLord,
        info = info
    )

    PlanetLine(
        label = "नक्षत्र",
        planet = info.nakshatraLord,
        info = info
    )

    PlanetLine(
        label = "नवांश",
        planet = info.navamshaLord,
        info = info
    )

    Text(
        text =
            "नवांश राशी: ${info.navamshaRashi}",

        color = Color.LightGray,

        fontSize = 13.sp
    )

    if (info.enemies.isNotEmpty()) {

        Spacer(
            Modifier.height(6.dp)
        )

        Text(
            text =
                "🔴 शत्रू ग्रह: " +
                    info.enemies.joinToString(" • "),

            color =
                Color(0xFFFF5252),

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}
@Composable
private fun PlanetLine(
    label: String,
    planet: String,
    info: JyotishInfo
) {

    val isEnemy =
        info.enemies.contains(planet)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = "$label:",

            color =
                Color.LightGray,

            fontSize = 13.sp
        )

        Text(
            text = planet,

            color =
                if (isEnemy)
                    Color(0xFFFF5252)
                else
                    Color.White,

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}
// ======================================================
// JYOTISH MASTER
// ======================================================

data class JyotishInfo(
    val rashiLord: String,
    val nakshatraLord: String,
    val navamshaRashi: String,
    val navamshaLord: String,
    val enemies: List<String>
)

object JyotishMaster {

    private val rashiLords = mapOf(
        Rashi.MESHA to "मंगळ",
        Rashi.VRISHABHA to "शुक्र",
        Rashi.MITHUNA to "बुध",
        Rashi.KARKA to "चंद्र",
        Rashi.SIMHA to "सूर्य",
        Rashi.KANYA to "बुध",
        Rashi.TULA to "शुक्र",
        Rashi.VRISHCHIKA to "मंगळ",
        Rashi.DHANU to "गुरु",
        Rashi.MAKARA to "शनि",
        Rashi.KUMBHA to "शनि",
        Rashi.MEENA to "गुरु"
    )

    private val nakshatraLords = mapOf(
        Nakshatra.ASHWINI to "केतू",
        Nakshatra.BHARANI to "शुक्र",
        Nakshatra.KRITTIKA to "सूर्य",
        Nakshatra.ROHINI to "चंद्र",
        Nakshatra.MRIGASHIRSHA to "मंगळ",
        Nakshatra.ARDRA to "राहू",
        Nakshatra.PUNARVASU to "गुरु",
        Nakshatra.PUSHYA to "शनि",
        Nakshatra.ASHLESHA to "बुध",
        Nakshatra.MAGHA to "केतू",
        Nakshatra.PURVA_PHALGUNI to "शुक्र",
        Nakshatra.UTTARA_PHALGUNI to "सूर्य",
        Nakshatra.HASTA to "चंद्र",
        Nakshatra.CHITRA to "मंगळ",
        Nakshatra.SWATI to "राहू",
        Nakshatra.VISHAKHA to "गुरु",
        Nakshatra.ANURADHA to "शनि",
        Nakshatra.JYESHTHA to "बुध",
        Nakshatra.MOOLA to "केतू",
        Nakshatra.PURVA_ASHADHA to "शुक्र",
        Nakshatra.UTTARA_ASHADHA to "सूर्य",
        Nakshatra.SHRAVANA to "चंद्र",
        Nakshatra.DHANISHTHA to "मंगळ",
        Nakshatra.SHATABHISHA to "राहू",
        Nakshatra.PURVA_BHADRAPADA to "गुरु",
        Nakshatra.UTTARA_BHADRAPADA to "शनि",
        Nakshatra.REVATI to "बुध"
    )

    private val enemyMap = mapOf(
        "सूर्य" to setOf("शुक्र", "शनि"),
        "चंद्र" to emptySet(),
        "मंगळ" to setOf("बुध"),
        "बुध" to setOf("चंद्र"),
        "गुरु" to setOf("बुध", "शुक्र"),
        "शुक्र" to setOf("सूर्य", "चंद्र"),
        "शनि" to setOf("सूर्य", "चंद्र"),
        "राहू" to setOf("सूर्य", "चंद्र", "मंगळ"),
        "केतू" to setOf("सूर्य", "चंद्र", "मंगळ")
    )

    fun getInfo(
        rashi: Rashi,
        nakshatra: Nakshatra,
        pada: Int
    ): JyotishInfo {

        val rashiLord =
            rashiLords[rashi] ?: "—"

        val nakshatraLord =
            nakshatraLords[nakshatra] ?: "—"

        val nakshatraIndex =
            Nakshatra.entries.indexOf(nakshatra)

        val navamshaIndex =
            (nakshatraIndex * 4 + (pada - 1)) % 12

        val navamsha =
            Rashi.entries[navamshaIndex]

        val navamshaLord =
            rashiLords[navamsha] ?: "—"

        val allLords = listOf(
            rashiLord,
            nakshatraLord,
            navamshaLord
        )

        val enemies =
            allLords.filter { planet ->
                allLords.any { other ->
                    planet != other &&
                    (
                        enemyMap[planet]?.contains(other) == true ||
                        enemyMap[other]?.contains(planet) == true
                    )
                }
            }.distinct()

        return JyotishInfo(
            rashiLord = rashiLord,
            nakshatraLord = nakshatraLord,
            navamshaRashi = navamsha.marathi,
            navamshaLord = navamshaLord,
            enemies = enemies
        )
    }
}
