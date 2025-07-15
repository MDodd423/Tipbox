package tech.dodd.tipbox

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
class Note(val date: String, val amount: String, val location: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}