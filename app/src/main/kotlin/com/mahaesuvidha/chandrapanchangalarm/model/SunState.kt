@Composable
private fun SunColumn(
    state: SunState,
    cardColor: Color,
    accentColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            Text(
                text = "☀️ सूर्य",
                color = textColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "● LIVE सध्याची स्थिती",
                color = Color(0xFF00E676),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            InfoRow(
                label = "राशी",
                value = state.rashi.marathi,
                textColor = textColor
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            InfoRow(
                label = "नक्षत्र",
                value = state.nakshatra.marathi,
                textColor = textColor
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            InfoRow(
                label = "चरण",
                value = state.pada.toString(),
                textColor = textColor
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 18.dp),
                color = Color.White.copy(alpha = 0.20f)
            )

            Text(
                text = "🔔 पुढील बदल",
                color = accentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "☀️ पुढील राशी बदल",
                color = accentColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = state.nextRashi,
                color = textColor,
                fontSize = 16.sp
            )

            Text(
                text = "📅 ${state.nextRashiTime}",
                color = textColor.copy(alpha = 0.75f),
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "⭐ पुढील नक्षत्र बदल",
                color = accentColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = state.nextNakshatra,
                color = textColor,
                fontSize = 16.sp
            )

            Text(
                text = "📅 ${state.nextNakshatraTime}",
                color = textColor.copy(alpha = 0.75f),
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "🔔 पुढील चरण बदल",
                color = accentColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = state.nextCharan,
                color = textColor,
                fontSize = 16.sp
            )

            Text(
                text = "📅 ${state.nextCharanTime}",
                color = textColor.copy(alpha = 0.75f),
                fontSize = 14.sp
            )
        }
    }
}
