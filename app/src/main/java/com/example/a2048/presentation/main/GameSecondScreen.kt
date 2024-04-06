package com.example.a2048.presentation.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.GravityInt
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.a2048.R
import com.example.a2048.data.model.SideEnum
import com.example.a2048.databinding.ActivityPlayBinding
import com.example.a2048.domain.AppController
import com.example.a2048.presentation.utils.BackgroundUtil
import com.example.a2048.presentation.utils.MyTouchListener
import com.example.a2048.settings.Settings


class GameSecondScreen : Fragment(R.layout.activity_play) {

    private val items: MutableList<TextView> = ArrayList(16)
    private lateinit var mainView: LinearLayout
    private var repository = AppController.getInstance()
    private var settings: Settings = Settings.getInstance()
    private val binding by viewBinding(ActivityPlayBinding::bind)
    private val util = BackgroundUtil()
    private lateinit var level: TextView
    private lateinit var record: TextView
    private var clickBackCount = 0
    private lateinit var dialog: Dialog
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        level = binding.score
        record = binding.record
        mainView = binding.mainView
        dialog = Dialog(requireContext())

        loadViews()
        describeMatrixToViews()

        binding.apply {
            btnHome.setOnClickListener { showReturnHomeDialog() }
            btnRefresh.setOnClickListener { showRestartDialog() }
        }

        if (clickBackCount == 1 || !repository.isPlaying()) {
            //binding.back.setBackgroundResource(R.drawable.bg_for_back)
            binding.back.setColorFilter(Color.argb(200, 200, 200, 200))
            binding.back.isClickable = false
        }

        val myTouchListener = MyTouchListener(requireContext())
        myTouchListener.setActionSideEnumListener {

            binding.back.setBackgroundResource(R.drawable.bg_item_score)
            binding.back.setColorFilter(Color.argb(255, 255, 255, 255))
            binding.back.isClickable = true
            clickBackCount = 0


            record

            when (it) {
                SideEnum.DOWN -> {
                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }
                    repository.setState(true)
                    repository.moveUp()
                    describeMatrixToViews()
                }

                SideEnum.UP -> {
                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }
                    repository.setState(true)
                    repository.moveDown()
                    describeMatrixToViews()
                }

                SideEnum.RIGHT -> {
                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }
                    repository.setState(true)
                    repository.moveToLeft()
                    describeMatrixToViews()
                }

                SideEnum.LEFT -> {

                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }
                    repository.setState(true)
                    repository.moveToRight()
                    describeMatrixToViews()
                }
            }
        }
        mainView.setOnTouchListener(myTouchListener)


        binding.btnHome.setOnClickListener {
            repository.saveItems()
            findNavController().navigateUp()
        }
    }
    private fun showFinishGameDialog() {
        dialog.setContentView(R.layout.dialog_lost_game)


        var record = record.text.toString().toInt()
        var record2 = settings.getRecord2()
        var record3 = settings.getRecord3()



        Log.d("RECORD", "$record")
        Log.d("RECORD", "$record2")
        Log.d("RECORD", "$record3")
        val level = level.text.toString().toInt()

        if (record < level) {
            record3 = record2
            record2 = record
            record = level
        } else if (level in (record2 + 1)..<record) {
            println()
            record3 = record2
            record2 = level
        } else if (level in (record3 + 1)..<record2) {
            record3 = level
        }


        /*if (record < level.text.toString().toInt()) {

            if (record2 != 0) {
                record3 = record2
                record2 = record
            }
            record = level.text.toString().toInt()

        } else if (record2 < level.text.toString().toInt()) {
            if (record3 != 0) {
                record3 = record2
            }
            record2 = level.text.toString().toInt()
        } else if (record3 < level.text.toString().toInt()) {
            record3 = record2
        } else if (record3 < level.text.toString().toInt()) {
            record3 = level.text.toString().toInt()
        }*/

        repository.saveRecords(record, record2, record3)




        dialog.setCancelable(false)
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
           // viewModel.restartGame()
            repository.restart()

            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener {
            repository.restart()
            navController.navigateUp()
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

    private fun showReturnHomeDialog() {
        dialog.setContentView(R.layout.dialog_restart)
        dialog.findViewById<TextView>(R.id.textView).text = "Return home?"
        dialog.findViewById<CardView>(R.id.no).setOnClickListener {
            Log.d("TAG", "showQuestionHomeDialog: ${binding.score.text.toString()}")
            //pref.saveBestScores(binding.score.text.toString().toInt())

            navController.navigateUp()
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.yes).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.setDialog(Gravity.CENTER, true)
    }
    private fun Dialog.setDialog(@GravityInt gravityInt: Int, cancelable: Boolean) {
        this.show()
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //    this.window?.attributes?.windowAnimations = R.style.DialogAnimation
        this.window?.setGravity(gravityInt)
        this.setCancelable(cancelable)
    }

    private fun showRestartDialog() {

        dialog.setContentView(R.layout.dialog_restart)
        dialog.findViewById<TextView>(R.id.textView).text = "Restart game"

        dialog.findViewById<CardView>(R.id.no).setOnClickListener {
          //  pref.saveBestScores(binding.score.text.toString().toInt())
            Log.d("TAG", "showPurchaseDialog:${binding.score.text}")
            repository.clear()
            describeMatrixToViews()
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.yes).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<ImageView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.setDialog(Gravity.CENTER, true)
    }


    private fun openGameOverDialog() {
        showFinishGameDialog()
//        val builder = AlertDialog.Builder(requireContext())
//        val view: View = layoutInflater.inflate(R.layout.game_over_dialog, null)
//
//        builder.setView(view)
//        val gameOverDialog = builder.create()
//
//        view.findViewById<TextView>(R.id.txt_score).text = repository.level.toString()
//        view.findViewById<Button>(R.id.btn_restart).setOnClickListener {
//            repository.clear()
//            gameOverDialog.dismiss()
//            repository = AppRepository.getInstance()
//            describeMatrixToViews()
//        }
//        view.findViewById<Button>(R.id.btn_home)?.setOnClickListener {
//            repository.restart()
//            repository = AppRepository.getInstance()
//            gameOverDialog.dismiss()
//            finish()
//        }
//
//
//        var record = record.text.toString().toInt()
//        var record2 = settings.getRecord2()
//        var record3 = settings.getRecord3()
//
//
//
//        Log.d("RECORD", "$record")
//        Log.d("RECORD", "$record2")
//        Log.d("RECORD", "$record3")
//        var level = level.text.toString().toInt()
//
//        if (record < level) {
//            record3 = record2
//            record2 = record
//            record = level
//        } else if (record > level && record2 < level) {
//            println()
//            record3 = record2
//            record2 = level
//        } else if (record2 > level && record3 < level) {
//            record3 = level
//        }
//
//
//        /*if (record < level.text.toString().toInt()) {
//
//            if (record2 != 0) {
//                record3 = record2
//                record2 = record
//            }
//            record = level.text.toString().toInt()
//
//        } else if (record2 < level.text.toString().toInt()) {
//            if (record3 != 0) {
//                record3 = record2
//            }
//            record2 = level.text.toString().toInt()
//        } else if (record3 < level.text.toString().toInt()) {
//            record3 = record2
//        } else if (record3 < level.text.toString().toInt()) {
//            record3 = level.text.toString().toInt()
//        }*/
//
//        repository.saveRecords(record, record2, record3)
//
//
//
//        gameOverDialog.setCancelable(false)
//
//
//        gameOverDialog.show()
    }



    private fun loadViews() {


        /*binding.back.setOnClickListener {
            finish()
        }*/
        for (i in 0 until mainView.childCount) {
            val linear = mainView.getChildAt(i) as LinearLayout
            for (j in 0 until linear.childCount) {
                items.add(linear.getChildAt(j) as TextView)

            }
        }
       binding.back.setOnClickListener {
            clickBackCount++
            repository.backOldState()
            describeMatrixToViews()
        }
        /*findViewById<ImageView>(R.id.button_restart).setOnClickListener{
            repository.clear()
            describeMatrixToViews()
        }*/
    }

    @SuppressLint("ResourceAsColor")
    private fun describeMatrixToViews() {
        if (clickBackCount == 1 || !repository.isPlaying()) {

            binding.back.setBackgroundResource(R.drawable.bg_for_back)
            binding.back.isClickable = false
        }
        level.text = repository.level.toString()
        record.text = repository.getRecord().toString()
        val _matrix = repository.matrix
        for (i in _matrix.indices) {
            for (j in _matrix[i].indices) {
                items[i * _matrix.size + j].apply {
                    text = if (_matrix[i][j] == 0) ""
                    else _matrix[i][j].toString()
                    if (_matrix[i][j] == 2 || _matrix[i][j] == 4) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            this.setTextColor(this.context.getColor(R.color.white))
                        };
                    } else
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            this.setTextColor(this.context.getColor(R.color.white))
                        };
                    setBackgroundResource(util.colorByCount(_matrix[i][j]))
//                    setTextColor(util.textColorByCount(_matrix[i][j]))
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        repository.saveItems()
    }

}