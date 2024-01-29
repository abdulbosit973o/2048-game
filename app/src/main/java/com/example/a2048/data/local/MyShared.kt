package com.example.a2048.data.local

import android.content.Context
import android.content.SharedPreferences
import java.util.Arrays

class MyShared private constructor() {
    private var result = IntArray(3)
    fun saveScore(count: Int) {
        result = results
        if (result[2] > count) {
            result[2] = count
            Arrays.sort(result)
        }
        val sb = StringBuilder()
        sb.append(result[0]).append("#").append(result[1]).append("#").append(result[2])
        pref!!.edit().putString("move_count", sb.toString()).apply()
    }

    fun save(count: Int) {
        val currentBestResult = pref!!.getInt("best", Int.MAX_VALUE)
        if (count < currentBestResult) pref!!.edit().putInt("best", count).apply()
    }

    val best: Int
        get() = pref!!.getInt("best", Int.MAX_VALUE)

    fun getResults() : String? {
        return pref!!.getString("move_count", "0#0#0")
    }


    val results: IntArray
        get() {
            val arr = IntArray(3)
            if (pref!!.getString("move_count", "") != "") {
                val s = pref!!.getString("move_count", "")!!
                    .split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in 0..2) {
                    arr[i] = s[i].toInt()
                }
                result = arr
            } else {
                result[0] = Int.MAX_VALUE
                result[1] = Int.MAX_VALUE
                result[2] = Int.MAX_VALUE
            }
            return result
        }

    companion object {
        private var pref: SharedPreferences? = null
        fun getInstance(context: Context): MyShared {
            if (pref == null) {
                pref = context.getSharedPreferences("puzzle_15", Context.MODE_PRIVATE)
            }
            return MyShared()
        }
    }
}