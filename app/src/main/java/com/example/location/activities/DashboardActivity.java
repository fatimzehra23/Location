package com.example.location.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.location.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button btnGestionOffres = findViewById(R.id.btnGestionOffres);
        Button btnGestionDemandes = findViewById(R.id.btnGestionDemandes);
        Button btnDeconnexion = findViewById(R.id.btnDeconnexion);

        btnGestionOffres.setOnClickListener(v -> {
            // Rediriger vers l'activité de gestion des offres
            Intent intent = new Intent(DashboardActivity.this, Offres.class);
            startActivity(intent);
        });

        btnGestionDemandes.setOnClickListener(v -> {
            // Rediriger vers l'activité de gestion des demandes
            Intent intent = new Intent(DashboardActivity.this, Demandes.class);
            startActivity(intent);
        });

        btnDeconnexion.setOnClickListener(v -> {
            // Retour à l'écran d'authentification
            Intent intent = new Intent(DashboardActivity.this, Authentification.class);
            startActivity(intent);
            finish(); // Ferme le dashboard pour éviter un retour arrière
        });
    }
}
