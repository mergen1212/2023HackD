package com.example.database.teams

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object Teams: Table() {
    private val user1 = Teams.integer("user1")
    private val user2 = Teams.integer("user2").nullable()
    private val tournamentId = Teams.integer("tournament_id")

    fun insertTeam(teamsDTO: TeamsDTO){
        transaction {
            Teams.insert {
                it[user1] = teamsDTO.user1
                it[user2] = teamsDTO.user2
                it[tournamentId] = teamsDTO.tournamentId
            }
        }
    }
    fun isExists(user1: Int, user2: Int? = null, tournamentId:Int): Boolean{
        return try {

            val user2n:Int?
            if (user2 == null) {
                user2n = 0
            } else {user2n = user2}
            transaction {
                val u1 = select {
                    Teams.user1.eq(user1) and Teams.tournamentId.eq(tournamentId) or
                            Teams.user2.eq(user2) and Teams.tournamentId.eq(tournamentId) or
                            Teams.user1.eq(user2n) and Teams.tournamentId.eq(tournamentId) or
                            Teams.user2.eq(user1) and Teams.tournamentId.eq(tournamentId)
                }.singleOrNull()

                u1 != null
            }
        }catch (e: PSQLException){
            false
        }
    }
}