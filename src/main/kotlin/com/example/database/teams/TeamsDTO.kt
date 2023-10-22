package com.example.database.teams

import kotlinx.serialization.Serializable

@Serializable
data class TeamsDTO(
    val user1:Int,
    val user2:Int?,
    val tournamentId: Int,
)
