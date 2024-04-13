package com.example.a2048.presentation.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.a2048.R
import com.example.a2048.data.local.MyShared
import com.example.a2048.data.local.MySharedPreferences
import com.example.a2048.databinding.ScreenHomeBinding
import com.example.a2048.presentation.info.InfoScreen
import com.example.a2048.settings.Settings
import com.example.a2048.utils.myLog


class HomeScreen : Fragment(R.layout.screen_home) {
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private lateinit var dialog: Dialog
    private val shared = Settings.getInstance()
    private val sharedPreferences = MySharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())

        binding.play.setOnClickListener {
            findNavController().navigate(R.id.gameScreen)
        }
        binding.apply {
            play.setOnClickListener {
                findNavController().navigate(R.id.action_homeScreen_to_gameSecondScreen)
            }
            info.setOnClickListener {
                findNavController().navigate(HomeScreenDirections.actionHomeScreenToAppInfoScreen())
            }

            purchase.setOnClickListener {
                showPurchaseDialog()
            }
            stats.setOnClickListener {
                showStatsDialog()
            }
            howToPlay.setOnClickListener {
                findNavController().navigate(HomeScreenDirections.actionHomeScreenToInfoScreen())

            }
            settings.setOnClickListener {
                showSettingsDialog()
            }

        }

    }

    private fun showSettingsDialog() {
        dialog.setContentView(R.layout.dialog_settings)



        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<CardView>(R.id.rate).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=game.original2048.game2048.classic2048")))
        }

        dialog.findViewById<CardView>(R.id.privacy_policy).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.freeprivacypolicy.com/live/1f7be489-2c41-46fd-93d0-a1ddf368dff2")))
        }
        dialog.findViewById<CardView>(R.id.more_games).setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=Activemedia+Solutions&hl=en&gl=US")
                )
            )
            //startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=pub:Activemedia Solutions")))
        }


        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
    }
    @SuppressLint("SetTextI18n")
    private fun showStatsDialog() {
        dialog.setContentView(R.layout.dialog_stats)

        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener { dialog.dismiss() }


        dialog.findViewById<TextView>(R.id.secondResult).text = "${shared.getRecord2()} score"
        dialog.findViewById<TextView>(R.id.firstResult).text =  "${shared.getRecord()} score"
        dialog.findViewById<TextView>(R.id.thirdResult).text =  "${shared.getRecord3()} score"


        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
    }




    private fun showPurchaseDialog() {
        dialog.setContentView(R.layout.dialog_purchase)

        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<CardView>(R.id.disable_ads).setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account")
                )
            )
        }
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    //    dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
    }
}