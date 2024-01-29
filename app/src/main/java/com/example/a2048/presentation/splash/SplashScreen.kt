package com.example.a2048.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.a2048.R
import com.example.a2048.databinding.ScreenHomeBinding
import com.example.a2048.databinding.ScreenSplashBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen: Fragment(R.layout.screen_splash) {
    private val binding by viewBinding(ScreenSplashBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToHomeScreen())
        }, 1000)
    }
}