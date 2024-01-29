package com.example.a2048.presentation.info

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class InfoAdapter(fragmentManager: Fragment) :FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
       return InfoPage().apply {
           arguments = bundleOf(Pair("POS", position))
       }
    }
}