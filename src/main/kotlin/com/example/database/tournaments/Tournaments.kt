package com.example.database.tournaments

import com.example.database.teams.Teams
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Tournaments: Table() {
    private val id = Tournaments.integer("id")
    private val name = Tournaments.varchar("name", 100)
    private val size = Tournaments.integer("size")
    private val status = Tournaments.varchar("status", 10)
    private val creatorId = Tournaments.integer("creator_id")
    private val teamCount = Tournaments.integer("team_count")

    fun insertTournament(tournamentDTO: TournamentDTO){
        transaction {
            Tournaments.insert{
                it[name] = tournamentDTO.name
                it[size] = tournamentDTO.size
                it[status] = tournamentDTO.status
                it[creatorId] = tournamentDTO.creatorId
            }
        }
    }
    fun selectByStatus(status: String):List<TournamentDTO>{
        if (status != ""){
            return transaction {
                Tournaments.select{Tournaments.status.eq(status)}
                    .map { row ->
                        TournamentDTO(
                            id = row[Tournaments.id],
                            name = row[Tournaments.name],
                            size = row[Tournaments.size],
                            status = row[Tournaments.status],
                            creatorId = row[Tournaments.creatorId],
                            teamCount = row[Tournaments.teamCount],
                        )
                    }
            }
        } else{
            return transaction {
                Tournaments.selectAll()
                    .map { row ->
                        TournamentDTO(
                            id = row[Tournaments.id],
                            name = row[Tournaments.name],
                            size = row[Tournaments.size],
                            status = row[Tournaments.status],
                            creatorId = row[Tournaments.creatorId],
                            teamCount = row[Tournaments.teamCount],
                        )
                    }
            }
        }
    }
    fun isStatusActive(tournamentId:Int): Boolean{
        return transaction {
            (select { Tournaments.id.eq(tournamentId) }.single())[Tournaments.status].equals("OPENED")
        }
    }
    fun incTeamCount(tournamentId:Int){
        transaction {
            val teamCount = (select { Tournaments.id.eq(tournamentId) }.single())[Tournaments.teamCount] +1
            Tournaments.update({Tournaments.id.eq(tournamentId)}){
                it[Tournaments.teamCount] = teamCount
            }
        }
    }

    fun isTournamentFull(tournamentId:Int):Boolean{
        return transaction {
            //val selectedSize = (select { Tournaments.id.eq(tournamentId) }.single())[Tournaments.size]
            //val selectedCount = (select { Tournaments.id.eq(tournamentId) }.single())[Tournaments.size]
            (select { Tournaments.id.eq(tournamentId) }.single())[Tournaments.size] ==
                    (select { Tournaments.id.eq(tournamentId) }.single())[Tournaments.teamCount]
        }
    }
    fun changeTournamentStatus(userId: Int, tournamentId:Int){
        transaction {
            val u1 = select {
                //Teams.user1.eq(user1) and Teams.tournamentId.eq(tournamentId)
                Tournaments.creatorId.eq(userId)
            }.singleOrNull()
            if (u1 != null)
        }
    }
}