package com.mahaesuvidha.chandrapanchangalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahaesuvidha.chandrapanchangalarm.model.LiveMoonCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.LiveSunCalculator
import com.mahaesuvidha.chandrapanchangalarm.model.MoonState
import com.mahaesuvidha.chandrapanchangalarm.model.SunState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ChandraSuryaAlarmApp()
            }
        }
    }
}


@Composable
fun ChandraSuryaAlarmApp() {

    val scrollState = rememberScrollState()

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

    var showSettings by remember {
        mutableStateOf(false)
    }

    var moonAlarmOn by remember {
        mutableStateOf(true)
    }

    var sunAlarmOn by remember {
        mutableStateOf(true)
    }

    /*
     * LIVE REFRESH
     *
     * प्रत्येक 30 सेकंदांनी
     * चंद्र आणि सूर्याची स्थिती refresh होईल
     */

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

    val moonCardColor =
        Color(0xFF0D2A40)

    val sunCardColor =
        Color(0xFF2B2103)

    val white =
        Color(0xFFE8EEF5)

    val moonBlue =
        Color(0xFF64B5F6)

    val sunGold =
        Color(0xFFFFC857)

    val green =
        Color(0xFF00D26A)


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
                    .verticalScroll(scrollState)
                    .padding(20.dp)
        ) {

            // ------------------------------------------
            // HEADER
            // ------------------------------------------

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text = "🌙 चंद्र सूर्य अलार्म",
                        color = white,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "LIVE • AUTO • ACCURATE",
                        color =
                            Color.LightGray,
                        fontSize = 14.sp
                    )
                }


                Text(
                    text = "⚙️",
                    fontSize = 34.sp,
                    modifier =
                        Modifier.clickable {

                            showSettings = true
                        }
                )
            }


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            // ------------------------------------------
            // LOCATION
            // ------------------------------------------

            Text(
                text =
                    "📍 दौंड, महाराष्ट्र   • LIVE",
                color = green,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )


            // ------------------------------------------
            // MOON + SUN COLUMNS
            // ------------------------------------------

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                MoonColumn(
                    modifier =
                        Modifier.weight(1f),
                    state =
                        moonState,
                    cardColor =
                        moonCardColor,
                    accentColor =
                        moonBlue,
                    textColor =
                        white
                )


                SunColumn(
                    modifier =
                        Modifier.weight(1f),
                    state =
                        sunState,
                    cardColor =
                        sunCardColor,
                    accentColor =
                        sunGold,
                    textColor =
                        white
                )
            }


            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )


            // ------------------------------------------
            // ALARM TEST
            // ------------------------------------------

            Text(
                text = "🔔 अलार्म टेस्ट",
                color = white,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )


            TestButton(
                text =
                    "🌙 राशी बदल Test"
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            TestButton(
                text =
                    "⭐ नक्षत्र बदल Test"
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            TestButton(
                text =
                    "🔔 चरण बदल Test"
            )


            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )
        }


        // ------------------------------------------
        // SETTINGS
        // ------------------------------------------

        if (showSettings) {

            AlertDialog(

                onDismissRequest = {

                    showSettings = false
                },

                title = {

                    Text(
                        text = "⚙️ सेटिंग्स",
                        fontWeight =
                            FontWeight.Bold
                    )
                },

                text = {

                    Column {

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text =
                                    "🌙 चंद्र अलार्म",
                                modifier =
                                    Modifier.weight(1f),
                                fontSize = 18.sp
                            )

                            Switch(
                                checked =
                                    moonAlarmOn,

                                onCheckedChange = {

                                    moonAlarmOn = it
                                },

                                colors =
                                    SwitchDefaults.colors(
                                        checkedThumbColor =
                                            Color.White,

                                        checkedTrackColor =
                                            Color(0xFF1976D2)
                                    )
                            )
                        }


                        Spacer(
                            modifier =
                                Modifier.height(18.dp)
                        )


                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text =
                                    "☀️ सूर्य अलार्म",
                                modifier =
                                    Modifier.weight(1f),
                                fontSize = 18.sp
                            )

                            Switch(
                                checked =
                                    sunAlarmOn,

                                onCheckedChange = {

                                    sunAlarmOn = it
                                },

                                colors =
                                    SwitchDefaults.colors(
                                        checkedThumbColor =
                                            Color.White,

                                        checkedTrackColor =
                                            Color(0xFFFFA000)
                                    )
                            )
                        }
                    }
                },

                confirmButton = {

                    Button(
                        onClick = {

                            showSettings = false
                        }
                    ) {

                        Text(
                            text = "OK"
                        )
                    }
                }
            )
        }
    }
}


/*
 * ==========================================
 * MOON COLUMN
 * ==========================================
 */

@Composable
private fun MoonColumn(

    modifier: Modifier,

    state: MoonState,

    cardColor: Color,

    accentColor: Color,

    textColor: Color
) {

    Column(
        modifier =
            modifier
                .background(
                    color =
                        cardColor,
                    shape =
                        RoundedCornerShape(28.dp)
                )
                .padding(18.dp)
    ) {

        Text(
            text = "🌙 चंद्र",
            color = textColor,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Text(
            text =
                "● LIVE सध्याची स्थिती",
            color =
                Color(0xFF00D26A),
            fontSize = 15.sp,
            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        InfoRow(
            title = "राशी",
            value =
                state.rashi.marathi,
            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        InfoRow(
            title = "नक्षत्र",
            value =
                state.nakshatra.marathi,
            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        InfoRow(
            title = "चरण",
            value =
                state.pada.toString(),
            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        DividerLine()


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        Text(
            text = "ग्रह स्वामी",
            color = accentColor,
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Text(
            text =
                "राशी: ${getRashiLord(state.rashi.marathi)}",
            color =
                textColor,
            fontSize = 16.sp
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Text(
            text =
                "नक्षत्र: ${getNakshatraLord(state.nakshatra.marathi)}",
            color =
                textColor,
            fontSize = 16.sp
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Text(
            text =
                "नवांश: ${getPadaLord(state.nakshatra.marathi)}",
            color =
                textColor,
            fontSize = 16.sp
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        Text(
            text = "🔔 पुढील बदल",
            color = accentColor,
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        ChangeItem(
            title =
                "🌙 पुढील राशी बदल",

            value =
                state.nextRashi,

            time =
                state.nextRashiTime,

            accentColor =
                accentColor,

            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        ChangeItem(
            title =
                "⭐ पुढील नक्षत्र बदल",

            value =
                state.nextNakshatra,

            time =
                state.nextNakshatraTime,

            accentColor =
                accentColor,

            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        ChangeItem(
            title =
                "🔔 पुढील चरण बदल",

            value =
                state.nextCharan,

            time =
                state.nextCharanTime,

            accentColor =
                accentColor,

            textColor =
                textColor
        )
    }
}


/*
 * ==========================================
 * SUN COLUMN
 * ==========================================
 */

@Composable
private fun SunColumn(

    modifier: Modifier,

    state: SunState,

    cardColor: Color,

    accentColor: Color,

    textColor: Color
) {

    Column(
        modifier =
            modifier
                .background(
                    color =
                        cardColor,
                    shape =
                        RoundedCornerShape(28.dp)
                )
                .padding(18.dp)
    ) {

        Text(
            text = "☀️ सूर्य",
            color = textColor,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Text(
            text =
                "● LIVE सध्याची स्थिती",
            color =
                Color(0xFF00D26A),
            fontSize = 15.sp,
            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        InfoRow(
            title = "राशी",
            value =
                state.rashi.marathi,
            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        InfoRow(
            title = "नक्षत्र",
            value =
                state.nakshatra.marathi,
            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        InfoRow(
            title = "चरण",
            value =
                state.pada.toString(),
            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        DividerLine()


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )


        Text(
            text = "ग्रह स्वामी",
            color = accentColor,
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Text(
            text =
                "राशी: ${getRashiLord(state.rashi.marathi)}",
            color =
                textColor,
            fontSize = 16.sp
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Text(
            text =
                "नक्षत्र: ${getNakshatraLord(state.nakshatra.marathi)}",
            color =
                textColor,
            fontSize = 16.sp
        )


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        Text(
            text =
                "नवांश: ${getPadaLord(state.nakshatra.marathi)}",
            color =
                textColor,
            fontSize = 16.sp
        )


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        Text(
            text = "🔔 पुढील बदल",
            color = accentColor,
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        ChangeItem(
            title =
                "☀️ पुढील राशी बदल",

            value =
                state.nextRashi,

            time =
                state.nextRashiTime,

            accentColor =
                accentColor,

            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        ChangeItem(
            title =
                "⭐ पुढील नक्षत्र बदल",

            value =
                state.nextNakshatra,

            time =
                state.nextNakshatraTime,

            accentColor =
                accentColor,

            textColor =
                textColor
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        ChangeItem(
            title =
                "🔔 पुढील चरण बदल",

            value =
                state.nextCharan,

            time =
                state.nextCharanTime,

            accentColor =
                accentColor,

            textColor =
                textColor
        )
    }
}


/*
 * ==========================================
 * INFO ROW
 * ==========================================
 */

@Composable
private fun InfoRow(

    title: String,

    value: String,

    textColor: Color
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = title,
            color =
                Color.LightGray,
            fontSize = 17.sp
        )


        Text(
            text = value,
            color = textColor,
            fontSize = 21.sp,
            fontWeight =
                FontWeight.Bold
        )
    }
}


/*
 * ==========================================
 * NEXT CHANGE ITEM
 * ==========================================
 */

@Composable
private fun ChangeItem(

    title: String,

    value: String,

    time: String,

    accentColor: Color,

    textColor: Color
) {

    Text(
        text = title,
        color = accentColor,
        fontSize = 17.sp,
        fontWeight =
            FontWeight.Bold
    )


    Spacer(
        modifier =
            Modifier.height(6.dp)
    )


    Text(
        text = value,
        color = textColor,
        fontSize = 16.sp
    )


    Spacer(
        modifier =
            Modifier.height(6.dp)
    )


    Text(
        text = "📅 $time",
        color =
            Color.LightGray,
        fontSize = 14.sp
    )
}


/*
 * ==========================================
 * DIVIDER
 * ==========================================
 */

@Composable
private fun DividerLine() {

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Color(0xFF52606D)
                )
    )
}


/*
 * ==========================================
 * TEST BUTTON
 * ==========================================
 */

@Composable
private fun TestButton(
    text: String
) {

    Button(

        onClick = {

            // पुढे येथे
            // वास्तविक Alarm Test code जोडू
        },

        modifier =
            Modifier
                .fillMaxWidth()
                .height(64.dp),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    Color(0xFF6851A0)
            )
    ) {

        Text(
            text = text,
            fontSize = 19.sp,
            fontWeight =
                FontWeight.Bold
        )
    }
}


/*
 * ==========================================
 * RASHI LORD
 * ==========================================
 */

private fun getRashiLord(
    rashi: String
): String {

    return when (rashi) {

        "मेष" -> "मंगळ"
        "वृषभ" -> "शुक्र"
        "मिथुन" -> "बुध"
        "कर्क" -> "चंद्र"
        "सिंह" -> "सूर्य"
        "कन्या" -> "बुध"
        "तुळ" -> "शुक्र"
        "वृश्चिक" -> "मंगळ"
        "धनु" -> "गुरु"
        "मकर" -> "शनि"
        "कुंभ" -> "शनि"
        "मीन" -> "गुरु"

        else -> "—"
    }
}


/*
 * ==========================================
 * NAKSHATRA LORD
 * ==========================================
 */

private fun getNakshatraLord(
    nakshatra: String
): String {

    return when (nakshatra) {

        "अश्विनी" -> "केतू"
        "भरणी" -> "शुक्र"
        "कृत्तिका" -> "सूर्य"
        "रोहिणी" -> "चंद्र"
        "मृगशीर्ष" -> "मंगळ"
        "आर्द्रा" -> "राहू"
        "पुनर्वसू" -> "गुरु"
        "पुष्य" -> "शनि"
        "आश्लेषा" -> "बुध"
        "मघा" -> "केतू"
        "पूर्वाफाल्गुनी" -> "शुक्र"
        "उत्तराफाल्गुनी" -> "सूर्य"
        "हस्त" -> "चंद्र"
        "चित्रा" -> "मंगळ"
        "स्वाती" -> "राहू"
        "विशाखा" -> "गुरु"
        "अनुराधा" -> "शनि"
        "ज्येष्ठा" -> "बुध"
        "मूळ" -> "केतू"
        "पूर्वाषाढा" -> "शुक्र"
        "उत्तराषाढा" -> "सूर्य"
        "श्रवण" -> "चंद्र"
        "धनिष्ठा" -> "मंगळ"
        "शततारका" -> "राहू"
        "पूर्वाभाद्रपदा" -> "गुरु"
        "उत्तराभाद्रपदा" -> "शनि"
        "रेवती" -> "बुध"

        else -> "—"
    }
}


/*
 * ==========================================
 * PADA / NAVAMSHA LORD
 *
 * सध्या नक्षत्राच्या स्वामीप्रमाणे
 * basic display.
 *
 * पुढच्या step मध्ये actual
 * Navamsha calculation जोडू.
 * ==========================================
 */

private fun getPadaLord(
    nakshatra: String
): String {

    return getNakshatraLord(nakshatra)
}
