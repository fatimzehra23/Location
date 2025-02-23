package com.example.location.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.location.R;
import com.google.firebase.auth.FirebaseAuth;

public class Authentification extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layoutLogin, layoutRegister;
    private Button btnAuth, btnCreateAccount, btnConfirm, btnCancel;
    private EditText etLogin, etPassword, etNom, etPrenom, etPays, etAdresse, etPhone, etEmail, etPwd;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authentification);

        mAuth = FirebaseAuth.getInstance();

        // Initialisation des vues
        layoutLogin = findViewById(R.id.Layout1);
        layoutRegister = findViewById(R.id.Layout2);
        btnAuth = findViewById(R.id.authButton);
        btnCreateAccount = findViewById(R.id.acchButton);
        btnConfirm = findViewById(R.id.confirmeButton);
        btnCancel = findViewById(R.id.annulerButton);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        etNom = findViewById(R.id.nomEditText);
        etPrenom = findViewById(R.id.prenomEditText);
        etPays = findViewById(R.id.nysEditText);
        etAdresse = findViewById(R.id.villeEditText);
        etPhone = findViewById(R.id.phoneEditText);
        etEmail = findViewById(R.id.nemsEditText);  // Correction de l'ID
        etPwd = findViewById(R.id.pwdEditText);  // Correction de l'ID

        // Afficher le login par défaut
        layoutLogin.setVisibility(View.VISIBLE);
        layoutRegister.setVisibility(View.GONE);

        btnAuth.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.authButton) {
            String email = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Authentifier l'utilisateur avec Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Authentification.this, "Authentification réussie !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Authentification.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Authentification.this, "Erreur d'authentification.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else if (view.getId() == R.id.acchButton) {
            layoutLogin.setVisibility(View.GONE);
            layoutRegister.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.confirmeButton) {
            Toast.makeText(this, "Utilisateur créé avec succès !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Authentification.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else if (view.getId() == R.id.annulerButton) {
            layoutLogin.setVisibility(View.VISIBLE);
            layoutRegister.setVisibility(View.GONE);
        }
    }
}
