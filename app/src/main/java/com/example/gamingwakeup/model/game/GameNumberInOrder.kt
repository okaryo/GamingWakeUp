package com.example.gamingwakeup.model.game

class GameNumberInOrder : GameBase() {
    private val _lastNumber = 9
    private val _submittedNumbers = mutableListOf<Int>()

    // TODO: 配列操作と真偽値返しどっちもやってるのが合ってなくて違和感
    fun verifySubmission(number: Int): Boolean {
        val nextCorrectNumber = _submittedNumbers.size + 1
        return if (number != nextCorrectNumber) {
            _submittedNumbers.clear()
            false
        } else {
            _submittedNumbers.add(number)
            true
        }
    }

    fun isCompleted(): Boolean {
        return _submittedNumbers.size + 1 == _lastNumber
    }
}
