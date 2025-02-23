package com.example.location.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.location.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Offre extends AppCompatActivity {
    private EditText etTitre, etPrix;
    private Button btnAjouter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);

        etTitre = findViewById(R.id.etTitre);
        etPrix = findViewById(R.id.etPrix);
        btnAjouter = findViewById(R.id.btnAjouter);
        db = FirebaseFirestore.getInstance();

        btnAjouter.setOnClickListener(view -> ajouterOffre());
    }

    private void ajouterOffre() {
        String titre = etTitre.getText().toString().trim();
        String prix = etPrix.getText().toString().trim();

        if (titre.isEmpty() || prix.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> offre = new HashMap<>();
        offre.put("titre", titre);
        offre.put("prix", prix);

        db.collection("Offres").add(offre)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Offre.this, "Offre ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // Retourner à la liste des offres
                })
                .addOnFailureListener(e ->
                        Toast.makeText(Offre.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show()
                );
    }
}

