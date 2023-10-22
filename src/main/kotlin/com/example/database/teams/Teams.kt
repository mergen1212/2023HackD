package com.example.database.teams

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

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
    fun isExists(user1: Int, tournamentId:Int): Boolean{
        return try {
            transaction {
                 (select { Teams.user1.eq(user1) }.single())
                (select { Teams.tournamentId.eq(tournamentId) }.single())
            }
            false
        }catch (e: Exception){
            true
        }

    }
}