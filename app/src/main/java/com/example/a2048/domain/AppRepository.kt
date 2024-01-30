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
    fun getlastMatrix() : Array<Array<Int>>?
    fun setLastMatrix()
    fun getLowerScore(): Int
//    fun addElement()
//    fun addEnterElements()
}