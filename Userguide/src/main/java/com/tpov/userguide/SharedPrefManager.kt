package com.tpov.userguide

import android.content.Context

object SharedPrefManager {
    private const val PREF_KEY = "userguide_key"
    private const val PREF_KEY_COUNTER_VIEW = "userguide_key_view"
    private const val PREF_COUNTER = "counter"

    //counter++
    fun incrementCounter(context: Context) {
        val sharedPreferences = context.getSharedPreferences(this.PREF_KEY, Context.MODE_PRIVATE)
        val counter = sharedPreferences.getInt(this.PREF_COUNTER, 0)
        val updatedCounter = counter + 1
        sharedPreferences.edit().putInt(this.PREF_COUNTER, updatedCounter).apply()
    }

    fun getCounter(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(this.PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(this.PREF_COUNTER, 0)
    }

    fun setCounter(context: Context, count: Int) {
        val sharedPreferences = context.getSharedPreferences(this.PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(this.PREF_COUNTER, count).apply()
    }

    fun getCounterView(context: Context, idView: Int): Int {
        val sharedPreferences = context.getSharedPreferences(this.PREF_KEY_COUNTER_VIEW, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(idView.toString(), 0)
    }

    fun setCounterView(context: Context, idView: Int) {
        val sharedPreferences = context.getSharedPreferences(this.PREF_KEY_COUNTER_VIEW, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(idView.toString(), 0).apply()
    }

    //counterView--
    fun decrementCounterView(context: Context, idView: Int) {
        val sharedPreferences = context.getSharedPreferences(this.PREF_KEY_COUNTER_VIEW, Context.MODE_PRIVATE)
        val counter = sharedPreferences.getInt(idView.toString(), 0)
        val updatedCounter = counter - 1
        sharedPreferences.edit().putInt(idView.toString(), updatedCounter).apply()
    }
}
