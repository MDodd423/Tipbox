package tech.dodd.tipbox;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String date;
    private final String amount;
    private final String location;

    public Note(String date, String amount, String location) {
        this.date = date;
        this.amount = amount;
        this.location = location;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    String getDate() {
        return date;
    }

    String getAmount() {
        return amount;
    }

    String getLocation() {
        return location;
    }


}
