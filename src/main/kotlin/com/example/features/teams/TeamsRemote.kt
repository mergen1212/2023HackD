package com.example.features.teams

import kotlinx.serialization.Serializable

@Serializable
data class TeamsReceiveRemote(
    val user1: Int,
    val user2: Int?,
    val tournamentId: Int,
)
