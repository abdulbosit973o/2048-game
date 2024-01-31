package com.example.a2048.domain

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.example.a2048.data.local.MySharedPreferences
import kotlin.random.Random

class AppRepositoryImpl(private val context: Context) : AppRepository {
    private val pref = MySharedPreferences
    private var isMove = false
    var moveNumber: Int = 0
    var lastMatrix: Array<Array<Int>>? = null

    companion object {
        private const val PREFS_NAME = "2048Prefs"
        private const val MATRIX_KEY = "matrixKey"
        private const val SCORE_KEY = "scoreKey"
        private const val MATRIX_SIZE = 16

        private lateinit var instance: AppRepository

        fun getInstance(context: Context): AppRepository {
            if (!(Companion::instance.isInitialized)) {
                instance = AppRepositoryImpl(context)
                (instance as AppRepositoryImpl) // Call loadGameData after initializing
            }
            return instance
        }

        fun getRepository(): AppRepository = instance
    }

    private var matrix = arrayOf(
        arrayOf(0, 0, 0, 0), arrayOf(0, 0, 0, 0), arrayOf(0, 0, 0, 0), arrayOf(0, 0, 0, 0)
//        arrayOf(2, 4, 8, 16), arrayOf(32, 64, 128, 256), arrayOf(512, 1024, 2, 4), arrayOf(8, 16, 0, 0)
    )

    private var score: Int = 0
    private val addElement = 2
    private val newElementValue: Int = 2


    init {
        addNewElement()
        addNewElement()
    }

    override fun getMatrix(): Array<Array<Int>> = matrix
    override fun getScore(): Int = score
    override fun getlastMatrix() {
        if (lastMatrix != null) {
            matrix = lastMatrix!!
            score -= moveNumber
        }
    }

    override fun getLowerScore(): Int {
        score -= moveNumber
        return score
    }

    override fun setLastMatrix() {
        lastMatrix = null
    }

    override fun saveGameData() {
        val matrixString = matrix.flatten().joinToString(",")
        pref.saveGame(matrixString)
        pref.saveScore(score)
    }

    override fun loadGameData() {
        matrix = pref.getState()
        score = pref.getScore()
    }

    private fun addNewElement() {
        val empty = ArrayList<Pair<Int, Int>>()

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) empty.add(Pair(i, j))
            }
        }

        if (empty.isEmpty()) return

        val randomIndex = Random.nextInt(0, empty.size)
        val findPairByRandomIndex = empty[randomIndex]

        // Define a threshold probability (e.g., 0.8) to control the chance of adding a new element
        val thresholdProbability = 0.8

        // Generate a random double between 0 and 1
        val randomValue = Random.nextDouble()

        // Check if the random value is less than the threshold probability
        if (randomValue < thresholdProbability) {
            // Add the new element (e.g., 2)
            matrix[findPairByRandomIndex.first][findPairByRandomIndex.second] = addElement
        }
    }

    private fun createBasicMatrix() = arrayOf(
        arrayOf(0, 0, 0, 0), arrayOf(0, 0, 0, 0), arrayOf(0, 0, 0, 0), arrayOf(0, 0, 0, 0)
    )


    private fun animateTileMovement(tileView: AppCompatTextView, row: Int, col: Int) {
        val translationX = col * tileView.width.toFloat()
        val translationY = row * tileView.height.toFloat()

        tileView.animate().translationX(translationX).translationY(translationY)
            .setDuration(200) // Adjust the duration as needed
            .start()
    }

    override fun moveUp() {
        val newMatrix = createBasicMatrix()
        var index: Int
        var isAdded: Boolean


        for (i in matrix.indices) {
            index = 0
            isAdded = false

            for (j in matrix[i].indices) {
                if (matrix[j][i] == 0) {

                    continue
                }
                if (newMatrix[0][i] == 0) {
                    newMatrix[0][i] = matrix[j][i]
                    continue
                }

                if (newMatrix[index][i] == matrix[j][i] && !isAdded) {
                    newMatrix[index][i] *= 2
                    isAdded = true
                    isMove = true
                    moveNumber = newMatrix[i][index]
                } else {
                    newMatrix[++index][i] = matrix[j][i]
                    isAdded = false
                }
            }

        }

        lastMatrix = matrix
        matrix = newMatrix
//        if (isCanAddNewElement && isMatrixChanged)
            addNewElement()

        isMove = false

        if (isScoreUpdated()) {
            saveGameData()
        }
        finish()
    }

    override fun moveRight() {
        val newMatrix = createBasicMatrix()
        var index: Int
        var isAdded: Boolean
        var isCanAddNewElement = false
        var isMatrixChanged = false




        for (i in matrix.indices) {
            index = 3
            isAdded = false

            for (j in matrix[i].size - 1 downTo 0) {
                if (matrix[i][j] == 0) {
                    continue
                }
                if (newMatrix[i][3] == 0) {
                    newMatrix[i][3] = matrix[i][j]
                    continue
                }

                if (newMatrix[i][index] == matrix[i][j] && !isAdded) {
                    newMatrix[i][index] *= 2
                    isAdded = true
                    isMove = true
                    score += newMatrix[i][index]
                    moveNumber = newMatrix[i][index]
                } else {
                    newMatrix[i][--index] = matrix[i][j]
                    isAdded = false
                    if (index != j) {
                        isMatrixChanged = true
                    }
                }
            }
        }

        lastMatrix = matrix
        matrix = newMatrix
//        if (isCanAddNewElement && isMatrixChanged)
            addNewElement()
        isMove = false

        if (isScoreUpdated()) {
            saveGameData()
        }

        finish()
    }

    override fun moveDown() {
        val newMatrix = createBasicMatrix()
        var index: Int
        var isAdded: Boolean
        var isCanAddNewElement = false
        var isMatrixChanged = false

        for (i in matrix.indices) {
            index = 3
            isAdded = false

            for (j in matrix[i].size - 1 downTo 0) {
                if (matrix[j][i] == 0) continue
                if (newMatrix[3][i] == 0) {
                    newMatrix[3][i] = matrix[j][i]
                    continue
                }

                if (newMatrix[index][i] == matrix[j][i] && !isAdded) {
                    newMatrix[index][i] *= 2
                    isAdded = true
                    isMove = true
                    score += newMatrix[i][index]
                    isMatrixChanged = true
                    isCanAddNewElement = true
                    moveNumber = newMatrix[i][index]
                } else {
                    newMatrix[--index][i] = matrix[j][i]
                    isAdded = false
                }
            }
        }

        lastMatrix = matrix
        matrix = newMatrix
//        if (isCanAddNewElement && isMatrixChanged)
            addNewElement()

        if (isScoreUpdated()) {
            saveGameData()
        }

        finish()
    }

    override fun moveLeft() {
        val newMatrix = createBasicMatrix()
        var index: Int
        var isAdded: Boolean

        for (i in matrix.indices) {
            index = 0
            isAdded = false

            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) continue
                if (newMatrix[i][0] == 0) {
                    newMatrix[i][0] = matrix[i][j]
                    continue
                }

                if (newMatrix[i][index] == matrix[i][j] && !isAdded) {
                    newMatrix[i][index] *= 2
                    isAdded = true
                    isMove = true
                    score += newMatrix[i][index]
                    moveNumber = newMatrix[i][index]
                } else {
                    newMatrix[i][++index] = matrix[i][j]
                    isAdded = false
                }
            }
        }

        lastMatrix = matrix
        matrix = newMatrix

         addNewElement()
        isMove = false

        if (isScoreUpdated()) {

            saveGameData()
        }

        finish()
    }

    private fun isScoreUpdated(): Boolean {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (j < matrix[i].size - 1 && matrix[i][j] == matrix[i][j + 1]) {
                    return true
                }
                if (i < matrix.size - 1 && matrix[i][j] == matrix[i + 1][j]) {
                    return true
                }
            }
        }
        return false
    }

    override fun finish(): Boolean {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) return false
            }
        }

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (j < matrix[i].size - 1 && matrix[i][j] == matrix[i][j + 1]) return false
                if (i < matrix.size - 1 && matrix[i][j] == matrix[i + 1][j]) return false
            }
        }

        return true
    }

    override fun resetGame() {
        score = 0
        matrix = createBasicMatrix()
        addNewElement()
        addNewElement()
    }


    private fun isMovable(): Boolean {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                val current = matrix[i][j]
                if (current == 0) {
                    return true
                }
                if (i > 0 && (matrix[i - 1][j] == 0 || matrix[i - 1][j] == current)) {
                    return true
                }
                if (i < matrix.size - 1 && (matrix[i + 1][j] == 0 || matrix[i + 1][j] == current)) {
                    return true
                }
                if (j > 0 && (matrix[i][j - 1] == 0 || matrix[i][j - 1] == current)) {
                    return true
                }
                if (j < matrix[i].size - 1 && (matrix[i][j + 1] == 0 || matrix[i][j + 1] == current)) {
                    return true
                }
            }
        }
        return false
    }

}