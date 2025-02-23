package com.example.location.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.location.R;

public class DetailsOffre extends AppCompatActivity {
    private TextView tvTitre, tvPrix, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_offre);

        tvTitre = findViewById(R.id.tvTitre);
        tvPrix = findViewById(R.id.tvPrix);
        tvDescription = findViewById(R.id.tvDescription);

        // Récupérer les données passées via l'intent
        String titre = getIntent().getStringExtra("titre");
        String prix = getIntent().getStringExtra("prix");
        String description = getIntent().getStringExtra("description");

        // Vérifier si les valeurs sont null et éviter un affichage vide
        tvTitre.setText(titre != null ? titre : "Titre inconnu");
        tvPrix.setText(prix != null ? prix + " MAD" : "Prix non disponible");
        tvDescription.setText(description != null ? description : "Pas de description disponible");
    }
}
