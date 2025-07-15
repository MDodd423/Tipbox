package tech.dodd.tipbox

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @get:Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY substr(date,7,4)||substr(date,1,2)||substr(date,4,2)  DESC, id DESC")
    val allNotesDateSortedDESC: LiveData<List<Note>>

    @get:Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY substr(date,7,4)||substr(date,1,2)||substr(date,4,2)  ASC, id DESC")
    val allNotesDateSortedASC: LiveData<List<Note>>

    @get:Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY cast((amount) as int) DESC")
    val allNotesAmountSortedDESC: LiveData<List<Note>>

    @get:Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY cast((amount) as int) ASC")
    val allNotesAmountSortedASC: LiveData<List<Note>>
}