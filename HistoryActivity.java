package tech.dodd.tipbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    //public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;
    final NoteAdapter adapter = new NoteAdapter();
    private TextView titleDate, titleAmount;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        titleDate = findViewById(R.id.text_view_date);
        titleAmount = findViewById(R.id.text_view_amount);

        res = getResources();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotesDateSortedDESC().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(HistoryActivity.this, TipActivity.class);
                intent.putExtra(TipActivity.EXTRA_ID, note.getId());
                intent.putExtra(TipActivity.EXTRA_DATE, note.getDate());
                intent.putExtra(TipActivity.EXTRA_AMOUNT, note.getAmount());
                intent.putExtra(TipActivity.EXTRA_LOCATION, note.getLocation());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    private final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    builder.setTitle(R.string.alert_title_text);
                    builder.setMessage(R.string.alert_message_text);
                    builder.setPositiveButton(R.string.alert_positive_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                            Toast.makeText(HistoryActivity.this, R.string.toast_deleted_text, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton(R.string.alert_negative_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            dialog.cancel();
                        }
                    })
                            .create()
                            .show();
                    break;
                case ItemTouchHelper.RIGHT:
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(TipActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, R.string.toast_notupdated_text, Toast.LENGTH_SHORT).show();
                return;
            }

            String date = data.getStringExtra(TipActivity.EXTRA_DATE);
            String amount = data.getStringExtra(TipActivity.EXTRA_AMOUNT);
            String location = data.getStringExtra(TipActivity.EXTRA_LOCATION);

            Note note = new Note(date, amount, location);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, R.string.toast_updated_text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.toast_notsaved_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (item.getItemId() == R.id.delete_all_notes) {
            //noteViewModel.deleteAllNotes();
            Toast.makeText(this, R.string.toast_alldeleted_text, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sortAmount(View v) {
        titleDate.setText(res.getString(R.string.title_date));
        if (titleAmount.getText().toString().equals(res.getString(R.string.title_amount)) || titleAmount.getText().toString().equals(res.getString(R.string.title_amount_down))) {
            titleAmount.setText(res.getString(R.string.title_amount_up));
            noteViewModel.getAllNotesAmountSortedDESC().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(@Nullable List<Note> notes) {
                    adapter.submitList(notes);
                }
            });
            return;
        }
        if (titleAmount.getText().toString().equals(res.getString(R.string.title_amount_up))) {
            titleAmount.setText(res.getString(R.string.title_amount_down));
            noteViewModel.getAllNotesAmountSortedASC().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(@Nullable List<Note> notes) {
                    adapter.submitList(notes);
                }
            });
        }
    }

    public void sortDate(View v) {
        titleAmount.setText(res.getString(R.string.title_amount));
        if (titleDate.getText().toString().equals(res.getString(R.string.title_date)) || titleDate.getText().toString().equals(res.getString(R.string.title_date_down))) {
            titleDate.setText(res.getString(R.string.title_date_up));
            noteViewModel.getAllNotesDateSortedDESC().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(@Nullable List<Note> notes) {
                    adapter.submitList(notes);
                }
            });
            return;
        }
        if (titleDate.getText().toString().equals(res.getString(R.string.title_date_up))) {
            titleDate.setText(res.getString(R.string.title_date_down));
            noteViewModel.getAllNotesDateSortedASC().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(@Nullable List<Note> notes) {
                    adapter.submitList(notes);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}