package edu.washington.ruiheli.tictactoe

import android.util.Log
import kotlin.math.abs

class GameUtil{
    companion object {
        fun checkWinner(board: Array<Array<Int>>): Boolean{
            var size = board.size
            Log.i("BOARD", "board size is $size")
            return checkVertical(board) || checkHor(board) || checkBackSlash(board) || checkForwardSlash(board)
        }

        fun checkTie(board: Array<Array<Int>>) :Boolean{
            var tieSum = board.size * board.size
            var sum = 0
            board.forEach {
                it.forEach {
                    sum += abs(it)
                }
            }
            if (sum == tieSum){
                return true
            }
            return false
        }
    }
}

private fun checkHor(board: Array<Array<Int>>): Boolean {
    var sum = 0
    board.forEach {
        it.forEach {
            sum += it
        }
        if (abs(sum) == board.size) {
            return true
        }
        sum = 0
    }
    return false
}

private fun checkVertical(board: Array<Array<Int>>): Boolean {
    var sum = 0
    for( i in 0 until board.size){
        for (j in 0 until board[0].size){
            sum += board[j][i]
        }
        if (abs(sum) == board.size) {
            return true
        }
        sum = 0
    }
    return false
}

private fun checkBackSlash(board: Array<Array<Int>>): Boolean{
    var sum = 0
    for (i in 0 until board.size){
        sum += board[i][i]
    }
    if (abs(sum) == board.size) {
        return true
    }
    return false
}

private fun checkForwardSlash(board: Array<Array<Int>>): Boolean{
    var sum = 0
    for (i in 0 until board.size){
        sum += board[i][board.size-1-i]
    }
    if (abs(sum) == board.size) {
        return true
    }
    return false
}

