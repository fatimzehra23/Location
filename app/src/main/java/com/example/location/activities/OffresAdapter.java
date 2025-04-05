package com.example.location.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.location.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OffresAdapter extends BaseAdapter {

    private Context context;
    private List<QueryDocumentSnapshot> offres;
    private FirebaseFirestore db;

    public OffresAdapter(Context context, List<QueryDocumentSnapshot> offres) {
        this.context = context;
        this.offres = offres;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return offres.size();
    }

    @Override
    public Object getItem(int position) {
        return offres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_offre, parent, false);
        }

        // Récupérer les éléments de la vue
        TextView titreOffre = convertView.findViewById(R.id.txtOffreTitre);
        TextView prixOffre = convertView.findViewById(R.id.txtOffrePrix);
        TextView descriptionOffre = convertView.findViewById(R.id.txtOffreDescription);
        ImageView imageOffre = convertView.findViewById(R.id.imgOffre);
        ImageButton btnSupprimerOffre = convertView.findViewById(R.id.btnSupprimerOffre);

        // Récupérer les données de l'offre
        QueryDocumentSnapshot offre = offres.get(position);
        String titre = offre.getString("titre");
        String prix = offre.getString("prix");
        String description = offre.getString("description");
        String imageUrl = offre.getString("imageUrl"); // URL de l'image stockée dans Firestore

        // Mettre à jour les TextViews avec les données de l'offre
        titreOffre.setText(titre);
        prixOffre.setText("Prix par nuit: " + prix + " MAD");
        descriptionOffre.setText(description);

        // Charger l'image sans Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            new DownloadImageTask(imageOffre).execute(imageUrl);
        } else {
            imageOffre.setImageResource(R.drawable.default_image); // Image par défaut si pas d'URL
        }

        // Gérer la suppression de l'offre
        btnSupprimerOffre.setOnClickListener(v -> {
            db.collection("Offres").document(offre.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        offres.remove(position);
                        notifyDataSetChanged(); // Rafraîchir l'adapter pour refléter les changements
                    })
                    .addOnFailureListener(e -> {
                        // Gérer l'échec de la suppression
                    });
        });

        return convertView;
    }

    // Classe interne pour charger les images en arrière-plan
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(R.drawable.default_image); // Si l'image ne se charge pas
            }
        }
    }
}
