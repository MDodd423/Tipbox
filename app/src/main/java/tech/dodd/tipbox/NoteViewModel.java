package tech.dodd.tipbox;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotesDateSortedDESC, allNotesDateSortedASC, allNotesAmountSortedDESC, allNotesAmountSortedASC;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotesDateSortedDESC = repository.getAllNotesDateSortedDESC();
        allNotesDateSortedASC = repository.getAllNotesDateSortedASC();
        allNotesAmountSortedDESC = repository.getAllNotesAmountSortedDESC();
        allNotesAmountSortedASC = repository.getAllNotesAmountSortedASC();
    }

    void insert(Note note) {
        repository.insert(note);
    }

    void update(Note note) {
        repository.update(note);
    }

    void delete(Note note) {
        repository.delete(note);
    }

    void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    LiveData<List<Note>> getAllNotesDateSortedDESC() {
        return allNotesDateSortedDESC;
    }

    LiveData<List<Note>> getAllNotesDateSortedASC() {
        return allNotesDateSortedASC;
    }

    LiveData<List<Note>> getAllNotesAmountSortedDESC() {
        return allNotesAmountSortedDESC;
    }

    LiveData<List<Note>> getAllNotesAmountSortedASC() {
        return allNotesAmountSortedASC;
    }
}