package com.example.a2048.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class Settings constructor(context: Context) {

    fun savePos(txt: String) {
        editor.putString("CURRENT_MATRIX", txt).apply()
    }

    fun saveLastMatrix(txt: String) {
        editor.putString("LAST", txt).apply()
    }

    fun saveLastMatrix2(txt: String) {
        editor.putString("LAST_2", txt).apply()
    }

    fun saveLastMatrix3(txt: String) {
        editor.putString("LAST_3", txt).apply()
    }

    /*fun saveState(boolean: Boolean){
        editor.putBoolean("STATE", boolean).apply()
    }*/
//    fun getState() = pref.getBoolean("STATE", false)


    fun saveState(boolean: Boolean){
        editor.putBoolean("STATE", boolean).apply()
    }

    fun isPlaying() = pref.getBoolean("STATE", false)
    fun getItems(): String? {
        return pref.getString("CURRENT_MATRIX", "")
    }

    fun lastItems(): String? {
        return pref.getString("LAST", "")
    }

    fun lastItems2(): String? {
        return pref.getString("LAST_2", "")
    }

    fun lastItems3(): String? {
        return pref.getString("LAST_3", "")
    }

    fun saveScore(score: Int) {
        editor.putInt("SCORE", score).apply()
    }

    fun saveLastScore(lastScore: Int) {
        editor.putInt("LAST_SCORE", lastScore).apply()
    }

    fun saveLastScore2(lastScore2: Int) {
        editor.putInt("LAST2_SCORE", lastScore2).apply()
    }

    fun saveLastScore3(lastScore3: Int) {
        editor.putInt("LAST3_SCORE", lastScore3).apply()
    }
    fun saveRecord(record:Int){
        editor.putInt("RECORD", record).apply()
    }

    fun getScore(): Int = pref.getInt("SCORE", 0)
    fun getScoreLast(): Int = pref.getInt("LAST_SCORE", 0)
    fun getScoreLast2(): Int = pref.getInt("LAST2_SCORE", 0)
    fun getScoreLast3(): Int = pref.getInt("LAST3_SCORE", 0)
    fun getRecord():Int = pref.getInt("RECORD", 0)

    private var pref: SharedPreferences = context.getSharedPreferences("2048", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: Settings

        fun init(context: Context){
            if (!(Companion::instance.isInitialized)){
                instance = Settings(context)
            }
        }
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        fun getInstance(): Settings {

            return instance
        }
    }
    fun clearAll(){
        editor.clear().apply()
    }
    fun getRecord2() = pref.getInt("RECORD2", 0)
    fun getRecord3() = pref.getInt("RECORD3", 0)

    fun saveRecord2(record2: Int){
        editor.putInt("RECORD2", record2).apply()
    }
    fun saveRecord3(record3: Int){
        editor.putInt("RECORD3", record3).apply()
    }

}