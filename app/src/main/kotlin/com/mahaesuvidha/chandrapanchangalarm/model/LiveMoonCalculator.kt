
package com.mahaesuvidha.chandrapanchangalarm.model

import java.time.LocalDateTime

object LiveMoonCalculator {

    fun getCurrentMoonState(): MoonState {

        /*
         * सध्या हा LIVE calculation integration साठी तयार base आहे.
         * पुढच्या step मध्ये actual astronomical calculation जोडणार आहोत.
         */

        val now = LocalDateTime.now()

        return MoonState(
            location = "दौंड, महाराष्ट्र",
            rashi = Rashi.KARKA,
            nakshatra = Nakshatra.PUSHYA,
            pada = 2,
            nextChange = "Live calculation सुरू आहे",
            nextChangeTime =
                "${now.dayOfMonth}-${now.monthValue}-${now.year} " +
                "${now.hour}:${String.format("%02d", now.minute)}"
        )
    }
}
