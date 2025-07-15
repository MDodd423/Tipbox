package tech.dodd.tipbox;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotesDateSortedDESC, allNotesDateSortedASC, allNotesAmountSortedDESC, allNotesAmountSortedASC;

    NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotesDateSortedDESC = noteDao.getAllNotesDateSortedDESC();
        allNotesDateSortedASC = noteDao.getAllNotesDateSortedASC();
        allNotesAmountSortedDESC = noteDao.getAllNotesAmountSortedDESC();
        allNotesAmountSortedASC = noteDao.getAllNotesAmountSortedASC();
    }

    void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, note);
    }

    void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, note);
    }

    void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, note);
    }

    void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private final NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private final NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}