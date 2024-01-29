package com.example.a2048.presentation.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.a2048.R
import com.example.a2048.databinding.PageInfoBinding

class InfoPage : Fragment(R.layout.page_info) {
    private val binding by viewBinding(PageInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pos = requireArguments().getInt("POS", 0)
        when (pos) {
            0 -> {
                binding.image.setImageResource(R.drawable.image1)
                binding.text.text = "There some numbers on the board."
            }

            1 -> {
                binding.image.setImageResource(R.drawable.image2)
                binding.text.text = "Swipe any direction(Up,Down,Left,\nRight) to moves all time."
            }
            2 -> {
                binding.image.setImageResource(R.drawable.image3)
                binding.text.text = "A new tile of 2 04 4 will appear\non the board after every move."
            }
            else -> {
                binding.image.setImageResource(R.drawable.image4)
                binding.text.text = "When a 2048 tile is created,\nyou win"
            }
        }
    }


}