package tech.dodd.tipbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    private OnItemClickListener listener;

    NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.getDate().equals(newItem.getDate()) && oldItem.getAmount().equals(newItem.getAmount()) && oldItem.getLocation().equals(newItem.getLocation());
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.textViewDate.setText(currentNote.getDate());
        holder.textViewAmount.setText(currentNote.getAmount());
        holder.textViewLocation.setText(currentNote.getLocation());
    }

    Note getNoteAt(int position) {
        return getItem(position);
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDate;
        private final TextView textViewAmount;
        private final TextView textViewLocation;

        NoteHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewAmount = itemView.findViewById(R.id.text_view_amount);
            textViewLocation = itemView.findViewById(R.id.text_view_location);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}