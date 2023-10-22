package com.example.plugins

import com.example.database.teams.Teams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    //install(StatusPages){
    //    exception<Throwable> { call, cause ->
    //        call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
    //    }
    //}
    install(Routing) {
        post ("/registerfff") {
            val iii = Teams.isExists(1000077, 1000002, 1000010)
            call.respondText(iii.toString())
            //call.respond("Referrer-Policy: no-referrer-when-downgrade")
        }
    }
}
