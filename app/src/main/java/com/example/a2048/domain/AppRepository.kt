package com.example.a2048.domain

interface AppRepository {
    fun moveUp()
    fun moveRight()
    fun moveDown()
    fun moveLeft()

    fun getMatrix(): Array<Array<Int>>
    fun resetGame()
    fun finish(): Boolean
    fun getScore(): Int
    fun getlastMatrix()
    fun setLastMatrix()
    fun getLowerScore(): Int
    fun saveGameData()
    fun loadGameData()
//    fun addElement()
//    fun addEnterElements()
}