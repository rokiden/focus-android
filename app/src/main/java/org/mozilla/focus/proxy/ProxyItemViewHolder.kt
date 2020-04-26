/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.proxy

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import org.mozilla.focus.R
import org.mozilla.focus.fragment.BrowserFragment
import org.mozilla.focus.locale.LocaleAwareAppCompatActivity
import org.mozilla.focus.menu.browser.BrowserMenuViewHolder
import org.mozilla.focus.utils.Settings
import org.mozilla.focus.utils.asActivity

internal class ProxyItemViewHolder/* package */(
        itemView: View
) : BrowserMenuViewHolder(itemView), CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private val textView: TextView = itemView.findViewById(R.id.textView)
    private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    private val settings = Settings.getInstance(itemView.context)

    fun bind() {
        checkbox.isChecked = settings.isProxyEnabled()
        checkbox.setOnCheckedChangeListener(this)
        textView.setOnClickListener(this)
    }

    override fun setOnClickListener(browserFragment: BrowserFragment?) {
        bind()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (menu != null) {
            menu.dismiss()
        }
        settings.setProxyEnabled(isChecked)
    }

    override fun onClick(v: View?) {
        if (menu != null) {
            menu.dismiss()
        }
        (v!!.context.asActivity() as LocaleAwareAppCompatActivity).openProxySettings()
    }

    companion object {
        const val LAYOUT_ID = R.layout.menu_proxy
    }


}
