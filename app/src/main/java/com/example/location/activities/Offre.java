package com.example.location.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.location.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Offre extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etTitre, etPrix;
    private Button btnAjouter, btnChoisirImage;
    private ImageView imgPreview;
    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);

        etTitre = findViewById(R.id.etTitre);
        etPrix = findViewById(R.id.etPrix);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnChoisirImage = findViewById(R.id.btnChoisirImage);
        imgPreview = findViewById(R.id.imgPreview);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("Offres");

        btnChoisirImage.setOnClickListener(view -> ouvrirGalerie());
        btnAjouter.setOnClickListener(view -> ajouterOffre());
    }

    private void ouvrirGalerie() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgPreview.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ajouterOffre() {
        String titre = etTitre.getText().toString().trim();
        String prix = etPrix.getText().toString().trim();

        if (titre.isEmpty() || prix.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Veuillez remplir tous les champs et ajouter une image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Générer un nom unique pour l'image
        String imageFileName = "offre_" + UUID.randomUUID().toString() + ".jpg";
        StorageReference fileRef = storageRef.child(imageFileName);

        // Upload de l'image sur Firebase Storage
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    enregistrerOffreFirestore(titre, prix, imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(Offre.this, "Échec du téléversement de l'image.", Toast.LENGTH_SHORT).show());
    }

    private void enregistrerOffreFirestore(String titre, String prix, String imageUrl) {
        Map<String, Object> offre = new HashMap<>();
        offre.put("titre", titre);
        offre.put("prix", prix);
        offre.put("imageUrl", imageUrl);

        db.collection("Offres")
                .add(offre)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Offre.this, "Offre ajoutée avec succès !", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(Offre.this, "Erreur lors de l'ajout.", Toast.LENGTH_SHORT).show());
    }
}
