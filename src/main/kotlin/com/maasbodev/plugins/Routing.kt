package com.maasbodev.plugins

import com.maasbodev.models.Note
import com.maasbodev.repositories.NotesRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {

    routing {
        route("/notes") {

            // CREATE

            post {
                try {
                    val note = call.receive<Note>()
                    val savedNote = NotesRepository.save(note)
                    call.respond(HttpStatusCode.Created, savedNote)
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Bad JSON Data Body: ${e.message}"
                    )
                }
            }

            // READ

            get {
                call.respond(NotesRepository.getAll())
            }

            get("{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing or malformed id"
                )
                val note = NotesRepository.getById(id.toLong()) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "No note with id $id"
                )
                call.respond(note)
            }

            // UPDATE

            put {
                try {
                    val note = call.receive<Note>()
                    if (NotesRepository.update(note)) {
                        call.respond(note)
                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            "No note with id $note.id"
                        )
                    }
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Bad JSON Data Body: ${e.message}"
                    )
                }
            }

            // DELETE

            delete("{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing or malformed id"
                )
                if (NotesRepository.delete(id.toLong())) {
                    call.respond(HttpStatusCode.Accepted)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "No note with id $id"
                    )
                }
            }
        }
    }
}