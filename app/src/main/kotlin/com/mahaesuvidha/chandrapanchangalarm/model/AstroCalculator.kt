package com.mahaesuvidha.chandrapanchangalarm.model
import java.time.*
import kotlin.math.*
object AstroCalculator{
 private val zone=ZoneId.of("Asia/Kolkata"); private const val N=360.0/27.0; private const val P=N/4.0
 fun moon(now:ZonedDateTime=ZonedDateTime.now(zone))=state("चंद्र",moonLon(now),now,13.176396)
 fun sun(now:ZonedDateTime=ZonedDateTime.now(zone))=state("सूर्य",sunLon(now),now,0.98564736)
 private fun state(body:String,lon:Double,now:ZonedDateTime,speed:Double):AstroState{val r=(lon/30).toInt().coerceIn(0,11);val n=(lon/N).toInt().coerceIn(0,26);val p=((lon%N)/P).toInt().coerceIn(0,3)+1
  fun next(bound:Double):Long{val d=(bound-lon+360)%360;return now.plusSeconds((d/speed*86400).roundToLong()).toInstant().toEpochMilli()}
  val rb=(r+1)*30.0; val nb=(n+1)*N; val pb=(floor(lon/P)+1)*P
  return AstroState(body,Rashi.entries[r],Nakshatra.entries[n],p,"${Rashi.entries[r].marathi} → ${Rashi.entries[(r+1)%12].marathi}",next(rb),"${Nakshatra.entries[n].marathi} → ${Nakshatra.entries[(n+1)%27].marathi}",next(nb),"चरण $p → चरण ${if(p==4)1 else p+1}",next(pb)) }
 private fun jd(t:ZonedDateTime)=t.toInstant().epochSecond/86400.0+2440587.5
 private fun norm(x:Double):Double=((x%360)+360)%360
 private fun sunLon(t:ZonedDateTime):Double{val d=jd(t)-2451545.0;val g=Math.toRadians(norm(357.529+0.98560028*d));return norm(280.46646+0.98564736*d+1.914602*sin(g)+0.019993*sin(2*g)-24.1)}
 private fun moonLon(t:ZonedDateTime):Double{val d=jd(t)-2451545.0;val l=norm(218.316+13.176396*d);val m=norm(134.963+13.064993*d);val f=norm(93.272+13.229350*d);val x=l+6.289*sin(Math.toRadians(m))+1.274*sin(Math.toRadians(2*(l-280.466)-m))+0.658*sin(Math.toRadians(2*(l-280.466)))+0.214*sin(Math.toRadians(2*m))-0.114*sin(Math.toRadians(2*f));val ay=23.85675+d/36525.0*1.396971;return norm(x-ay)}
}
