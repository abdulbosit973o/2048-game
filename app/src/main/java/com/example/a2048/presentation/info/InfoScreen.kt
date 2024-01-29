package com.example.a2048.presentation.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.a2048.R
import com.example.a2048.databinding.ScreenInfoPageBinding

class InfoScreen : Fragment(R.layout.screen_info_page) {
    private val binding by viewBinding(ScreenInfoPageBinding::bind)
    private val adapter by lazy { InfoAdapter(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = adapter
        binding.wormDotsIndicator.attachTo(binding.viewPager)

        binding.next.setOnClickListener {
            binding.viewPager.currentItem ++
        }
        binding.skip.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position) {
                    0 -> {
                        binding.skip.visibility = View.VISIBLE
                        binding.skiptext.visibility = View.VISIBLE
                        binding.next.visibility = View.VISIBLE
                        binding.nexttext.visibility = View.VISIBLE
                        binding.close.visibility = View.GONE
                        binding.closetext.visibility = View.GONE
                    }
                    1 -> {
                        binding.skip.visibility = View.VISIBLE
                        binding.skiptext.visibility = View.VISIBLE
                        binding.next.visibility = View.VISIBLE
                        binding.nexttext.visibility = View.VISIBLE
                        binding.close.visibility = View.GONE
                        binding.closetext.visibility = View.GONE
                    }

                    2 -> {
                        binding.skip.visibility = View.VISIBLE
                        binding.skiptext.visibility = View.VISIBLE
                        binding.next.visibility = View.VISIBLE
                        binding.nexttext.visibility = View.VISIBLE
                        binding.close.visibility = View.GONE
                        binding.closetext.visibility = View.GONE
                    }
                    else -> {
                        binding.skip.visibility = View.INVISIBLE
                        binding.next.visibility = View.INVISIBLE
                        binding.skiptext.visibility = View.INVISIBLE
                        binding.nexttext.visibility = View.INVISIBLE
                        binding.close.visibility = View.VISIBLE
                        binding.closetext.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}