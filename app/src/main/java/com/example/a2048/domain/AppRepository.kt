package com.example.a2048.domain

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.example.a2048.settings.Settings
import kotlin.random.Random

class AppRepository private constructor() {

    var level = 0
    private var record2 = 0
    private var record3 = 0
    private var lastLevel = 0
    private var record = 0


    companion object {
        @SuppressLint("StaticFieldLeak")

        private lateinit var settings: Settings


        @SuppressLint("StaticFieldLeak")
        private var instance: AppRepository? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        fun init(context: Context) {
            Companion.context = context
            settings = Settings.getInstance()
        }

        fun getInstance(): AppRepository {
            if (instance == null)
                instance = AppRepository()
            return instance!!
        }
    }

    init {
        record = settings.getRecord()
        record2 = settings.getRecord2()
        record3 = settings.getRecord3()
    }

    fun getRecord() = record

    private val NEW_ELEMENT = 2


    val matrix = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )
    val lastMatrix = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )


    init {
        if (settings.isPlaying()) {

            loadData()
        } else {
            addFirst()
            addFirst()
        }
    }

    fun addFirst() {
        val emptyList = ArrayList<Pair<Int, Int>>()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) {
                    emptyList.add(Pair(i, j))
                }
            }
        }


        val randomPos = Random.nextInt(0, emptyList.size)
        matrix[emptyList[randomPos].first][emptyList[randomPos].second] = NEW_ELEMENT
        lastMatrix[emptyList[randomPos].first][emptyList[randomPos].second] = NEW_ELEMENT
    }

    private fun addNewElement() {

        val emptyList = ArrayList<Pair<Int, Int>>()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) {
                    emptyList.add(Pair(i, j))
                }
            }
        }


        if (emptyList.isEmpty() && !isClickable()) {
            Toast.makeText(context, "Game over", Toast.LENGTH_SHORT).show()
            clear()
        } else {
            val randomPos = Random.nextInt(0, emptyList.size)
            matrix[emptyList[randomPos].first][emptyList[randomPos].second] = NEW_ELEMENT
        }
    }

    fun moveToLeft() {
        val temp = matrix.copyData()
        lastLevel = level

        for (i in matrix.indices) {
            val amounts = ArrayList<Int>(4)
            var bool = true
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) continue
                if (amounts.isEmpty()) amounts.add(matrix[i][j])
                else {
                    if (amounts.last() == matrix[i][j] && bool) {
                        level += amounts.last()
                        record = if (record < level) level else record


                        amounts[amounts.size - 1] = amounts.last() * 2
                        bool = false
                    } else {
                        amounts.add(matrix[i][j])
                        bool = true
                    }
                }
                matrix[i][j] = 0
            }

            for (k in amounts.indices) {
                matrix[i][k] = amounts[k]
            }
        }

        if (!temp.contentDeepEquals(matrix)) {
            swap(lastMatrix, matrix)
            addNewElement()
        }
    }

    fun moveToRight() {
        val temp = matrix.copyData()
        lastLevel = level

        for (i in matrix.indices) {
            val amounts = ArrayList<Int>(4)
            var bool = true
            for (j in matrix[i].size - 1 downTo 0) {
                if (matrix[i][j] == 0) continue
                if (amounts.isEmpty()) amounts.add(matrix[i][j])
                else {
                    if (amounts.last() == matrix[i][j] && bool) {
                        level += amounts.last()
                        record = if (record < level) level else record


                        amounts[amounts.size - 1] = amounts.last() * 2
                        bool = false
                    } else {
                        amounts.add(matrix[i][j])
                        bool = true
                    }
                }
                matrix[i][j] = 0
            }

            for (k in amounts.indices) {
                matrix[i][matrix[i].size - k - 1] = amounts[k]
            }
        }

        if (!temp.contentDeepEquals(matrix)){
            addNewElement()
            swap(lastMatrix, temp)
        }
    }

    fun moveUp() {
        val temp = matrix.copyData()
        lastLevel = level

        for (i in matrix.indices) {
            val amount = ArrayList<Int>()
            var bool = true
            for (j in matrix[i].indices) {
                if (matrix[j][i] == 0) continue
                if (amount.isEmpty()) amount.add(matrix[j][i])
                else {
                    if (amount.last() == matrix[j][i] && bool) {
                        level += amount.last()

                        record = if (record < level) level else record
                        amount[amount.size - 1] = amount.last() * 2
                        bool = false
                    } else {
                        bool = true
                        amount.add(matrix[j][i])
                    }
                }
                matrix[j][i] = 0
            }
            for (j in 0 until amount.size) {
                matrix[j][i] = amount[j]
            }
        }
        if (!temp.contentDeepEquals(matrix)) {
            addNewElement()
            swap(lastMatrix, temp)
        }
        /* if(!isClickable()){
             Toast.makeText(context, "Game over", Toast.LENGTH_SHORT).show()
             clear()
         }*/
    }

    fun moveDown() {

        val temp = matrix.copyData()
        lastLevel = level

        for (i in matrix.indices) {
            val amount = ArrayList<Int>()
            var bool = true
            for (j in matrix[i].size - 1 downTo 0) {
                if (matrix[j][i] == 0) continue
                if (amount.isEmpty()) amount.add(matrix[j][i])
                else {
                    if (amount.last() == matrix[j][i] && bool) {
                        level += amount.last()

                        record = if (record < level) level else record
                        amount[amount.size - 1] = amount.last() * 2
                        bool = false
                    } else {
                        bool = true
                        amount.add(matrix[j][i])
                    }
                }
                matrix[j][i] = 0
            }
            for (k in amount.size - 1 downTo 0) {
                matrix[3 - k][i] = amount[k]
            }

        }

        if (!temp.contentDeepEquals(matrix)) {
            addNewElement()
            swap(lastMatrix, temp)
        }
    }

    fun clear() {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                matrix[i][j] = 0
            }
        }
        level = 0
        lastLevel = 0

        swap(lastMatrix, matrix)
        settings.saveState(false)
        addFirst()
        addFirst()
    }

    fun backOldState() {
        swap(matrix, lastMatrix)
        level = lastLevel
    }

    fun saveItems() {
        val sb: StringBuilder = StringBuilder()
        val sbLast: StringBuilder = StringBuilder()

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                sb.append(matrix[i][j].toString())
                sbLast.append(lastMatrix[i][j])
                sb.append("##")
                sbLast.append("##")
            }
        }


        settings.savePos(sb.toString())
        settings.saveLastMatrix(sbLast.toString())

        settings.saveScore(level)
        settings.saveLastScore(lastLevel)
        settings.saveState(true)
    }


    fun loadData() {
        level = settings.getScore()
        lastLevel = settings.getScoreLast()

        record = settings.getRecord()

        val txt = settings.getItems()?.split("##")!!
        val txtLast = settings.lastItems()?.split("##")!!

        for (i in 0 until txt.size - 1) {
            matrix[i / 4][i % 4] = txt[i].toInt()

            if (txtLast.isNotEmpty()) {
                lastMatrix[i / 4][i % 4] = txtLast[i].toInt()
            }

        }
    }

    fun swap(a: Array<Array<Int>>, b: Array<Array<Int>>) {
        for (i in b.indices) {
            for (j in b[i].indices) {
                a[i][j] = b[i][j]
            }
        }
    }

    fun setState(boolean: Boolean) {
        settings.saveState(boolean)
    }

    fun isPlaying() = settings.isPlaying()

    fun isClickable(): Boolean {

        for (i in matrix.indices) {
            val emptyList = ArrayList<Int>(4)
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) {
                    return true
                }
                if (emptyList.isEmpty()) {
                    emptyList.add(matrix[i][j])
                } else {
                    if (emptyList.last() == matrix[i][j]) {
                        return true
                    } else {
                        emptyList.add(matrix[i][j])
                    }
                }
            }
        }

        for (i in matrix[0].indices) {
            val emptyList = ArrayList<Int>(4)

            for (j in matrix.indices) {
                if (matrix[j][i] == 0) {
                    return true
                }
                if (emptyList.isEmpty()) {
                    emptyList.add(matrix[j][i])
                } else {
                    if (emptyList.last() == matrix[j][i])
                        return true
                    else
                        emptyList.add(matrix[j][i])
                }
            }
        }

        return false
    }

    fun restart() {
        clear()
        instance = null
    }

    fun swapRecords(record: Int, record2: Int, record3: Int, level: Int) {
        if (record < level) {
            if (record2 != 0) {
                this@AppRepository.record3 = record2
                this@AppRepository.record2 = record
            }
            this@AppRepository.record = level


        } else if (record2 < level) {
            if (record3 != 0) {
                this@AppRepository.record3 = record2
            }
            this@AppRepository.record2 = level
        } else if (record3 < level) {
            this@AppRepository.record3 = record2
        } else if (record3 < level) {
            this@AppRepository.record3 = level
        }
    }
}
fun Array<Array<Int>>.copyData() = map { it.clone() }.toTypedArray()

