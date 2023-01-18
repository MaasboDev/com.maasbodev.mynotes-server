package com.maasbodev.repositories

import com.maasbodev.database.AppDatabase
import com.maasbodev.database.DbNote
import com.maasbodev.models.Note
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

private const val DATABASE_NAME = "database.db"

object NotesRepository {

    private val notesDb = JdbcSqliteDriver(url = "jdbc:sqlite:$DATABASE_NAME").let {
        if (!File(DATABASE_NAME).exists()) {
            AppDatabase.Schema.create(it)
        }
        AppDatabase(it)
    }.noteQueries

    fun save(note: Note): Note {
        notesDb.insert(note.title, note.description, note.type.name)
        return notesDb.selectLastInsertedNote().executeAsOne().toNote()
    }

    fun getAll(): List<Note> = notesDb.select().executeAsList().map { it.toNote() }

    fun getById(id: Long): Note? = notesDb.select().executeAsOneOrNull()?.toNote()

    fun update(note: Note): Boolean {
        if (getById(note.id) == null) return false
        notesDb.update(note.title, note.description, note.type.name, note.id)
        return true
    }

    fun delete(id: Long): Boolean {
        if (getById(id) == null) return false
        notesDb.delete(id)
        return true
    }
}

private fun DbNote.toNote(): Note = Note(
    id = id,
    title = title,
    description = description,
    type = Note.Type.valueOf(type)
)