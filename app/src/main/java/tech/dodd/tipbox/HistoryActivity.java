package tech.dodd.tipbox;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import tech.dodd.tipbox.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding activityHistoryBinding;
    private NoteViewModel noteViewModel;
    final NoteAdapter adapter = new NoteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Get the window instance & set the status bar color
        Window currentWindow = getWindow(); // <--- Get the window object
        WindowCompat.getInsetsController(currentWindow, currentWindow.getDecorView())
                .setAppearanceLightStatusBars(false); //true for dark, false for light
        activityHistoryBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = activityHistoryBinding.getRoot();
        setContentView(view);

        //Edge to Edge Display
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.setMargins(0, insets.top, 0, insets.bottom);
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });

        ActivityResultLauncher<Intent> tipActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent TipData = result.getData();
                assert TipData != null;
                doTipResult(TipData);
            } else {
                Toast.makeText(this, "Dinner not saved", Toast.LENGTH_SHORT).show();
            }
        });

        activityHistoryBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        activityHistoryBinding.recyclerview.setHasFixedSize(true);
        activityHistoryBinding.recyclerview.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotesDateSortedDESC().observe(this, adapter::submitList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(activityHistoryBinding.recyclerview);

        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(HistoryActivity.this, TipActivity.class);
            intent.putExtra(TipActivity.EXTRA_ID, note.getId());
            intent.putExtra(TipActivity.EXTRA_DATE, note.getDate());
            intent.putExtra(TipActivity.EXTRA_AMOUNT, note.getAmount());
            intent.putExtra(TipActivity.EXTRA_LOCATION, note.getLocation());
            tipActivityResultLauncher.launch(intent);
        });

        activityHistoryBinding.dateTextView.setOnClickListener(v -> dateTextViewFunction());
        activityHistoryBinding.amountTextView.setOnClickListener(v -> amountTextViewFunction());
    }

    public void doTipResult(Intent data) {
        int id = data.getIntExtra(TipActivity.EXTRA_ID, -1);
        if (id == -1) {
            Toast.makeText(this, R.string.toast_notupdated_text, Toast.LENGTH_SHORT).show();
            return;
        }

        String date = data.getStringExtra(TipActivity.EXTRA_DATE);
        String amount = data.getStringExtra(TipActivity.EXTRA_AMOUNT);
        String location = data.getStringExtra(TipActivity.EXTRA_LOCATION);

        assert date != null;
        assert amount != null;
        assert location != null;
        Note note = new Note(date, amount, location);
        note.setId(id);
        noteViewModel.update(note);
        Toast.makeText(this, R.string.toast_updated_text, Toast.LENGTH_SHORT).show();
    }

    private final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

            switch (direction) {
                case ItemTouchHelper.LEFT -> doLeftSwipe(viewHolder);
                case ItemTouchHelper.RIGHT ->
                        adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
            }
        }
    };

    public void doLeftSwipe(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
        builder.setTitle(R.string.alert_title_text);
        builder.setMessage(R.string.alert_message_text);
        builder.setPositiveButton(R.string.alert_positive_text, (dialog, arg1) -> {
            noteViewModel.delete(adapter.getNoteAt(viewHolder.getAbsoluteAdapterPosition()));
            Toast.makeText(HistoryActivity.this, R.string.toast_deleted_text, Toast.LENGTH_SHORT).show();
            dialog.cancel();
        });
        builder.setNegativeButton(R.string.alert_negative_text, (dialog, arg1) -> {
            adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
            dialog.cancel();
        }).create().show();
    }
    

    public void amountTextViewFunction() {
        activityHistoryBinding.dateTextView.setText(getResources().getString(R.string.title_date));
        if (activityHistoryBinding.amountTextView.getText().toString().equals(getResources().getString(R.string.title_amount)) || activityHistoryBinding.amountTextView.getText().toString().equals(getResources().getString(R.string.title_amount_down))) {
            activityHistoryBinding.amountTextView.setText(getResources().getString(R.string.title_amount_up));
            noteViewModel.getAllNotesAmountSortedDESC().observe(this, adapter::submitList);
            return;
        }
        if (activityHistoryBinding.amountTextView.getText().toString().equals(getResources().getString(R.string.title_amount_up))) {
            activityHistoryBinding.amountTextView.setText(getResources().getString(R.string.title_amount_down));
            noteViewModel.getAllNotesAmountSortedASC().observe(this, adapter::submitList);
        }
    }

    public void dateTextViewFunction() {
        activityHistoryBinding.amountTextView.setText(getResources().getString(R.string.title_amount));
        if (activityHistoryBinding.dateTextView.getText().toString().equals(getResources().getString(R.string.title_date)) || activityHistoryBinding.dateTextView.getText().toString().equals(getResources().getString(R.string.title_date_down))) {
            activityHistoryBinding.dateTextView.setText(getResources().getString(R.string.title_date_up));
            noteViewModel.getAllNotesDateSortedDESC().observe(this, adapter::submitList);
            return;
        }
        if (activityHistoryBinding.dateTextView.getText().toString().equals(getResources().getString(R.string.title_date_up))) {
            activityHistoryBinding.dateTextView.setText(getResources().getString(R.string.title_date_down));
            noteViewModel.getAllNotesDateSortedASC().observe(this, adapter::submitList);
        }
    }
}