package com.maasbodev.repositories

import com.maasbodev.models.Note
import com.maasbodev.models.Note.Type.AUDIO
import com.maasbodev.models.Note.Type.TEXT

object NotesRepository {

    fun getAll(): List<Note> {
        val notes = (0..10).map {
            Note(
                "Title $it",
                "Description $it",
                if (it % 3 == 0) AUDIO else TEXT
            )
        }
        return notes
    }
}