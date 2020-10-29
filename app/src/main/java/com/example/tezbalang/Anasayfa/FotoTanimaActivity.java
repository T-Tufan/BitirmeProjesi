package com.example.tezbalang.Anasayfa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tezbalang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerRemoteModel;

import java.util.List;

public class FotoTanimaActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_tanima);

        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Resim çekme isteği ve activity başlatılıp id'si tanımlandı

                startActivityForResult(kamera, 33);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 33) {

            ImageView resim = (ImageView) findViewById(R.id.imageView);
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");//Çekilen resim id olarak bitmap şeklinde alındı ve imageview'e atandı

            resim.setImageBitmap(bitmap);

            final AutoMLImageLabelerLocalModel localModel =
                    new AutoMLImageLabelerLocalModel.Builder()
                            .setAssetFilePath("manifest.json")
                            // or .setAbsoluteFilePath(absolute file path to manifest file)
                            .build();

            final AutoMLImageLabelerRemoteModel remoteModel =
                    new AutoMLImageLabelerRemoteModel.Builder("MarketProducts").build();

            DownloadConditions downloadConditions = new DownloadConditions.Builder()
                    .requireWifi()
                    .build();
            RemoteModelManager.getInstance().download(remoteModel, downloadConditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Model indirme başarılı", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Model indirme başarısız", Toast.LENGTH_SHORT).show();
                }
            });

            RemoteModelManager.getInstance().isModelDownloaded(remoteModel)
                    .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                        @Override
                        public void onSuccess(Boolean isDownloaded) {
                            AutoMLImageLabelerOptions.Builder optionsBuilder;
                            if (isDownloaded) {
                                optionsBuilder = new AutoMLImageLabelerOptions.Builder(remoteModel);
                            } else {
                                optionsBuilder = new AutoMLImageLabelerOptions.Builder(localModel);
                            }
                            AutoMLImageLabelerOptions options = optionsBuilder
                                    .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                                    // to determine an appropriate threshold.
                                    .build();

                            ImageLabeler labeler = ImageLabeling.getClient(options);
                            InputImage image = InputImage.fromBitmap(bitmap, 0);
                            labeler.process(image)
                                    .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                                        @Override
                                        public void onSuccess(List<ImageLabel> labels) {
                                            // Task completed successfully
                                            // ...
                                            for (int i = 0; i < labels.size(); i++) {
                                                Log.d("labels elemanlanı " + (i + 1), labels.get(i).getText()+" "+labels.get(i).getIndex()+" "+labels.get(i).getConfidence());
                                            }
                                            for (ImageLabel label : labels) {
                                                String text = label.getText();
                                                int index = label.getIndex();
                                                float confidence = label.getConfidence();
                                                Log.d("Labeller ", text);
                                            }
                                            TextView textView = findViewById(R.id.textView);
                                            textView.setText(labels.get(0).getText()+" "+labels.get(0).getConfidence());

                                            Intent ıntent=new Intent(getApplicationContext(), UrunDetayActivity.class);
                                            ıntent.putExtra("Foto_urun",labels.get(0).getText());
                                            startActivity(ıntent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Log.d("Sonuc : ", "Başarısız.Tekrar dene");
                                        }
                                    });
                        }
                    });
        }
            super.onActivityResult(requestCode, resultCode, data);
    }
}