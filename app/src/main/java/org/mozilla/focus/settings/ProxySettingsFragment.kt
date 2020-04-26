/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import org.mozilla.focus.R
import org.mozilla.focus.telemetry.TelemetryWrapper
import org.mozilla.focus.utils.Settings
import java.lang.NumberFormatException

class ProxySettingsFragment : BaseSettingsFragment(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        val summary_prefs_ids = setOf(
                R.string.pref_key_proxy_host,
                R.string.pref_key_proxy_port,
                R.string.pref_key_proxy_bypass)
    }

    private var summary_prefs: HashMap<String, Preference?> = HashMap()
    private var settings: Settings? = null

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.proxy_settings)
        settings = Settings.getInstance(context!!)
        for (key_id in summary_prefs_ids) {
            val key = getString(key_id)
            summary_prefs[key] = findPreference(key)
            updateSummary(key)
        }
    }

    override fun onResume() {
        super.onResume()

        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        // Update title and icons when returning to fragments.
        val updater = activity as ActionBarUpdater
        updater.updateTitle(R.string.preference_category_proxy)
        updater.updateIcon(R.drawable.ic_back)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_key_proxy_port)) {
            val port = try {
                sharedPreferences.getString(key, "65536")!!.toInt()
            } catch (e: NumberFormatException) {
                65536
            }
            if (port > 65535) {
                Toast.makeText(context, "Invalid port number", Toast.LENGTH_LONG).show()
                sharedPreferences.edit().putString(key, getString(R.string.default_proxy_port)).apply()
            }
        }
        if (key in summary_prefs.keys)
            updateSummary(key)
        TelemetryWrapper.settingsEvent(key, sharedPreferences.all[key].toString())
    }

    private fun updateSummary(key: String) {
        if (settings == null) return
        summary_prefs[key]?.summary = preferenceManager.sharedPreferences.getString(key, null)
    }
}
