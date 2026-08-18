package com.mahaesuvidha.chandrapanchangalarm.alarm
import android.app.*;import android.content.*;import android.os.Build;import com.mahaesuvidha.chandrapanchangalarm.model.*;import com.mahaesuvidha.chandrapanchangalarm.settings.AlarmPrefs
class AlarmScheduler(private val c:Context){private val am=c.getSystemService(AlarmManager::class.java)
 fun scheduleAll(){cancelAll();val p=AlarmPrefs(c);if(p.moon){val s=AstroCalculator.moon();scheduleBody(s,0,p)};if(p.sun){val s=AstroCalculator.sun();scheduleBody(s,10,p)}}
 private fun scheduleBody(s:AstroState,base:Int,p:AlarmPrefs){if(p.rashi) schedule(base+1,s.nextRashiMillis,"${s.body} राशी बदल",s.nextRashi);if(p.nak) schedule(base+2,s.nextNakshatraMillis,"${s.body} नक्षत्र बदल",s.nextNakshatra);if(p.pada) schedule(base+3,s.nextCharanMillis,"${s.body} चरण बदल",s.nextCharan)}
 fun scheduleTest(type:String){schedule(99,System.currentTimeMillis()+10000,"चंद्र सूर्य अलार्म","हा $type Test Alarm आहे.")}
 private fun schedule(id:Int,at:Long,title:String,msg:String){if(Build.VERSION.SDK_INT>=31&&!am.canScheduleExactAlarms()){try{c.startActivity(Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))}catch(_:Exception){};return};val i=Intent(c,AlarmReceiver::class.java).putExtra("title",title).putExtra("message",msg).putExtra("id",id);val pi=PendingIntent.getBroadcast(c,id,i,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE);am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,at,pi)}
 fun cancelAll(){for(id in 1..3)cancel(id);for(id in 11..13)cancel(id)};private fun cancel(id:Int){val pi=PendingIntent.getBroadcast(c,id,Intent(c,AlarmReceiver::class.java),PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE);if(pi!=null){am.cancel(pi);pi.cancel()}}
}scheduleRashiAlarm()
scheduleNakshatraAlarm()
scheduleCharanAlarm()
