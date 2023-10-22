package com.example.features.teams

import com.example.database.teams.Teams
import com.example.database.teams.TeamsDTO
import com.example.database.tokens.Tokens
import com.example.database.tournaments.TournamentDTO
import com.example.database.tournaments.Tournaments
import com.example.database.user.Users
import com.example.features.tournaments.TournamentsCreateReceiveRemote
import com.example.utils.TokenCheck
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class TeamsController(val call: ApplicationCall) {
    private val statusArr = arrayOf("OPENED", "ACTIVE", "FINISHED")
    suspend fun createNewTeam(){
        val token = call.request.headers["Authorization"]
        val receive = call.receive<TeamsReceiveRemote>()

        if (!Tournaments.isStatusActive(receive.tournamentId)){
            call.respond(HttpStatusCode.BadRequest, "Регистрация на турнир закончилась")
        }else if (Tournaments.isTournamentFull(receive.tournamentId)) {
            call.respond(HttpStatusCode.BadRequest, "Закончились места на турнир")
        }else if (TokenCheck.isTokenValid(token.orEmpty())){
            val tokenDTO = Tokens.fetchTokens1(token.toString())
            val userDTO = tokenDTO?.let { Users.fetchUser(it.email) }
            if (userDTO != null) {
                if (Teams.isExists(
                    receive.user1,
                    receive.user2,
                    receive.tournamentId
                )){
                    call.respond(HttpStatusCode.Conflict, "1 или несколько участников записаны на турнир")
                }else {
                    Tournaments.incTeamCount(receive.tournamentId)
                    Teams.insertTeam(
                        TeamsDTO(
                            user1 = receive.user1,
                            user2 = receive.user2,
                            tournamentId = receive.tournamentId,
                        )
                    )
                    call.respond(HttpStatusCode.OK, "Запись выполнена")
                }
            }
        }else{
            if (token == null){
                call.respond(HttpStatusCode.Forbidden, "Еблан входа")
            }else {
                Tokens.fetchOut(token)
                call.respond(HttpStatusCode.Unauthorized, "Срок действия токена истек")
            }
        }
    }
}