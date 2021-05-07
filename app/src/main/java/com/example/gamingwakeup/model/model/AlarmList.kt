package com.example.gamingwakeup.model.model

data class AlarmList(
    val values: List<Alarm>
) {
    fun sort(): AlarmList {
        val sortedValues = values.toMutableList().apply {
            sortBy { it.hour * 60 + it.minute }
        }.toList()
        return AlarmList(values = sortedValues)
    }
}
