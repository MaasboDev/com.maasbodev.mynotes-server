package com.maasbodev.plugins

import com.maasbodev.repositories.NotesRepository
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {

    routing {
        route("/notes") {
            get {
                call.respond(NotesRepository.getAll())
            }
        }
    }
}