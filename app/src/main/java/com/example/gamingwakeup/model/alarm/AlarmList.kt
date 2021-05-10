package com.example.gamingwakeup.model.alarm

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
