package com.example.a2048.presentation.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.a2048.R
import com.example.a2048.data.local.MyShared
import com.example.a2048.data.local.MySharedPreferences
import com.example.a2048.data.model.SideEnum
import com.example.a2048.databinding.ScreenPlayGameBinding
import com.example.a2048.presentation.utils.MyBackgroundUtil
import com.example.a2048.presentation.utils.MyTouchListener


class GameScreen : Fragment(R.layout.screen_play_game) {

    private val binding by viewBinding(ScreenPlayGameBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val views = ArrayList<AppCompatTextView>(16)
    private val viewModel: GameViewModel by viewModels()
    private lateinit var dialog: Dialog
    private var isUsed = false
    var score = 0
    private  val pref = MySharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog = Dialog(requireContext())
        setupViews()
        setupTouchListener()
        setupObservers()
        viewModel.loadGameData()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // create a dialog to ask yes no questions whether or not the user wants to exit
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.undo.setOnClickListener {
            if (!isUsed) {
                viewModel.getlastMatrix();
                isUsed = true
                binding.undoimg.setColorFilter(Color.argb(200, 200, 200, 200))
            }
        }

        binding.restartGame.setOnClickListener {
            showPurchaseDialog()
        }
        binding.home.setOnClickListener {
            showQuestionHomeDialog()
        }

        viewModel.gameFinishLiveData.observe(viewLifecycleOwner) { isGameFinished ->
            if (isGameFinished) {
                pref.saveBestScores(score)
                showFinishGameDialog()
            }
        }

        viewModel.scoreLiveData.observe(viewLifecycleOwner) { score ->
           if (pref.getBestScore() <= score) {
               pref.saveBestScore(score)
               binding.best.text = score.toString()
           }
            updateScore(score)
        }

        viewModel.loadData()

    }

    private fun showFinishGameDialog() {
        dialog.setContentView(R.layout.dialog_lost_game)
        dialog.findViewById<CardView>(R.id.home).setOnClickListener {
            navController.navigateUp()
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.share).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = "Tavsiya etaman: ${R.string.app_name}\n\nhttps://play.google.com/store/apps/details?id=org.telegram.messenger"
            intent.setType("text/plain")
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.app_name)
            )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
        }

        dialog.findViewById<CardView>(R.id.restart).setOnClickListener {
            viewModel.restartGame()
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //    dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
    }

    private fun showQuestionHomeDialog() {
        dialog.setContentView(R.layout.dialog_restart)
        dialog.findViewById<TextView>(R.id.textView).text = "Return home?"
        dialog.findViewById<CardView>(R.id.no).setOnClickListener {
            Log.d("TAG", "showQuestionHomeDialog: ${binding.score.text.toString()}")
            pref.saveBestScores(binding.score.text.toString().toInt())

            navController.navigateUp()
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.yes).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //    dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
    }

    private fun showPurchaseDialog() {

        dialog.setContentView(R.layout.dialog_restart)
        dialog.findViewById<TextView>(R.id.textView).text = "Restart game"

        dialog.findViewById<CardView>(R.id.no).setOnClickListener {
            pref.saveBestScores(binding.score.text.toString().toInt())
            Log.d("TAG", "showPurchaseDialog:${binding.score.text}")
            viewModel.restartGame()
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.yes).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //    dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
    }

    override fun onPause() {
        viewModel.saveGameData()
        super.onPause()
    }

    private fun updateScore(score: Int) {
        binding.score.text = score.toString()
    }

    private fun setupViews() {
        for (i in 0 until binding.mainView.childCount) {
            val line = binding.mainView[i] as LinearLayoutCompat
            for (j in 0 until line.childCount) {
                views.add(line[j] as AppCompatTextView)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        val myTouchListener = MyTouchListener(requireContext())
        myTouchListener.setActionSideEnumListener {
            when (it) {
                SideEnum.UP -> {
                    viewModel.finish()
                    viewModel.moveUp()
                    isUsed = false
                    binding.undoimg.setColorFilter(Color.argb(255, 255, 255, 255))
                }

                SideEnum.RIGHT -> {
                    viewModel.finish()
                    viewModel.moveRight()
                    isUsed = false
                    binding.undoimg.setColorFilter(Color.argb(255, 255, 255, 255))
                }

                SideEnum.DOWN -> {

                    viewModel.finish()
                    viewModel.moveDown()
                    isUsed = false
                    binding.undoimg.setColorFilter(Color.argb(255, 255, 255, 255))
                }

                SideEnum.LEFT -> {

                    viewModel.finish()
                    viewModel.moveLeft()
                    isUsed = false
                    binding.undoimg.setColorFilter(Color.argb(255, 255, 255, 255))
                }
            }
        }
        binding.mainView.setOnTouchListener(myTouchListener)
    }


    private fun setupObservers() {
        viewModel.matrixLiveData.observe(viewLifecycleOwner) { matrix ->
            updateUI(matrix)
            binding.best.text = pref.getBestScore().toString()
        }
    }

    private fun updateUI(matrix: Array<Array<Int>>) {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                val textView = views[i * 4 + j]
                if (matrix[i][j] == 0) {
                    textView.text = ""
                } else {
                    textView.text = "${matrix[i][j]}"
                }

                textView.setBackgroundResource(MyBackgroundUtil.backgroundByAmount(matrix[i][j]))
//                animateTileMovement(textView, i, j)
            }
        }
    }

    private fun animateTileMovement(tileView: AppCompatTextView, row: Int, col: Int) {
        val targetX = col * tileView.width.toFloat()
        val targetY = row * tileView.height.toFloat()

        ViewCompat.animate(tileView).x(targetX).y(targetY).setDuration(200)
            .setListener(object : ViewPropertyAnimatorListenerAdapter() {

            }).start()
    }

}