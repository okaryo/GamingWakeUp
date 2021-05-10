package com.example.gamingwakeup.model.game

import kotlin.random.Random

abstract class GameBase {
    enum class GameType {
        NUMBER_IN_ORDER
    }

    companion object {
        fun selectGame(): GameBase {
            val gameTypeCount = GameType.values().size
            val randomNumber = Random.nextInt(gameTypeCount)
            val selectedGameType = GameType.values().find { it.ordinal == randomNumber }
            return when(selectedGameType) {
                GameType.NUMBER_IN_ORDER -> GameNumberInOrder()
                else -> GameNumberInOrder()
            }
        }
    }
}
