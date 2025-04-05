package com.example.location.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.location.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Offres extends AppCompatActivity {

    private ListView listView;
    private List<QueryDocumentSnapshot> documents = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offres);

        // Initialiser les composants
        listView = findViewById(R.id.listViewOffres);
        db = FirebaseFirestore.getInstance();

        // Charger les offres depuis Firestore
        chargerOffres();

        // Bouton pour ajouter une nouvelle offre
        findViewById(R.id.fabAjouterOffre).setOnClickListener(view -> {
            Intent intent = new Intent(Offres.this, Offre.class);
            startActivity(intent);
        });
    }

    // Méthode pour charger les offres depuis Firestore
    private void chargerOffres() {
        db.collection("Offres")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Vider les anciennes données
                        documents.clear();
                        List<String> offreList = new ArrayList<>();

                        // Remplir la liste des offres
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String titre = document.getString("titre");
                            String prix = document.getString("prix");
                            if (titre != null && prix != null) {
                                offreList.add(titre + " - " + prix + " MAD");
                                documents.add(document); // Stocker les documents Firestore pour utilisation dans l'adaptateur
                            }
                        }

                        // Utiliser l'adaptateur personnalisé
                        OffresAdapter offresAdapter = new OffresAdapter(Offres.this, documents);
                        listView.setAdapter(offresAdapter);

                    } else {
                        // Gérer les erreurs
                        task.getException().printStackTrace();
                    }
                });
    }
}
