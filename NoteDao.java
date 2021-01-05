package tech.dodd.tipbox;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    //ROUND(amount*.01,2)
    //round(amount*.011, 2, 1)
    //cast(cast(amount *.01 as decimal(10, 2)) as varchar(255))
    //Cast(ROUND(amount*.01,2) as decimal(10,2))
    //printf("%.2f", amount * .01)
    //cast((amount) as int) / 100.0
    //"."

    @Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY substr(date,7,4)||substr(date,1,2)||substr(date,4,2)  DESC, id DESC")
    LiveData<List<Note>> getAllNotesDateSortedDESC();

    @Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY substr(date,7,4)||substr(date,1,2)||substr(date,4,2)  ASC, id DESC")
    LiveData<List<Note>> getAllNotesDateSortedASC();

    @Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY cast((amount) as int) DESC")
    LiveData<List<Note>> getAllNotesAmountSortedDESC();

    @Query("SELECT id, date, location, substr(amount,1,length(amount)-2)||\".\"||substr(amount,-2) as amount FROM note_table ORDER BY cast((amount) as int) ASC")
    LiveData<List<Note>> getAllNotesAmountSortedASC();
}
