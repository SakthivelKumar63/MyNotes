package com.example.mynotes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEditNoteActivity extends AppCompatActivity {
    EditText etTitle, etContent;
    Button btnSave;
    DatabaseReference notesRef;
    String noteId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_note);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSave = findViewById(R.id.btnSave);

        notesRef = FirebaseDatabase.getInstance().getReference("Notes");

        if (getIntent().hasExtra("noteId")) {
            noteId = getIntent().getStringExtra("noteId");
            etTitle.setText(getIntent().getStringExtra("noteTitle"));
            etContent.setText(getIntent().getStringExtra("noteContent"));
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteId == null) {
                String id = notesRef.push().getKey();
                Note note = new Note(id, title, content);
                notesRef.child(id).setValue(note);
            } else {
                Note updatedNote = new Note(noteId, title, content);
                notesRef.child(noteId).setValue(updatedNote);
            }

            finish();
        });
    }
}