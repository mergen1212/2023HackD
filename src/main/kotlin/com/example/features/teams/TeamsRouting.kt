package com.example.features.teams

import com.example.features.register.RegisterController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureTeamsRouting() {
    routing {
        post("/teams/create") {
            val teamsController = TeamsController(call)
            teamsController.createNewTeam()
        }
    }
}