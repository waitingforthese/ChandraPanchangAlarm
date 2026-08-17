package com.mahaesuvidha.chandrapanchangalarm.settings
import android.content.Context
class AlarmPrefs(c:Context){private val p=c.getSharedPreferences("alarm_prefs",Context.MODE_PRIVATE)
 var moon:Boolean get()=p.getBoolean("moon",true);set(v){p.edit().putBoolean("moon",v).apply()};var sun:Boolean get()=p.getBoolean("sun",true);set(v){p.edit().putBoolean("sun",v).apply()};var rashi:Boolean get()=p.getBoolean("rashi",true);set(v){p.edit().putBoolean("rashi",v).apply()};var nak:Boolean get()=p.getBoolean("nak",true);set(v){p.edit().putBoolean("nak",v).apply()};var pada:Boolean get()=p.getBoolean("pada",true);set(v){p.edit().putBoolean("pada",v).apply()}}
