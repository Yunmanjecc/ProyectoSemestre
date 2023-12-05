package twin.developers.projectmqtt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class nuevo_color extends AppCompatActivity {
    String codigoHex;
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri photoUri;
    EditText nombreColor;
    ImageView vista;
    Button capturar, anadir;
    DatabaseReference nombreYcodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_color);

        vista = findViewById(R.id.ivImagen);

        capturar = findViewById(R.id.bttnCapturar);

        nombreColor = findViewById(R.id.txtNuevoColor);

        anadir = findViewById(R.id.bttnAnadir);

        // Obtención de la referencia a la lista de tareas en Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        nombreYcodigo = database.getReference("listaColores");

        // Solicitar permiso para la cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }

        capturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recolección de los datos de entrada y creación de un nuevo objeto 'Color'
                String nombre = nombreColor.getText().toString();

                if (nombre.isEmpty()) {
                    // Mostrar un mensaje de error si el nombre está vacío
                    Toast.makeText(nuevo_color.this, "Por favor, ingrese un nombre para el color", Toast.LENGTH_SHORT).show();
                    return;
                }

                String codigo = codigoHex.toString();

                // Crear un nuevo objeto Color
                Color nuevoColor = new Color(nombre, codigo);

                nombreYcodigo.push().setValue(nuevoColor).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Mostrar un mensaje de éxito
                            Toast.makeText(nuevo_color.this, "Color agregado", Toast.LENGTH_SHORT).show();
                        } else {
                            // Mostrar un mensaje de error
                            Toast.makeText(nuevo_color.this, "Error al agregar el color", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            try {
                // Obtener un ContentResolver
                ContentResolver resolver = getContentResolver();

                // Usar el ContentResolver para obtener un Bitmap de la imagen
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, photoUri);

                // Cargar el Bitmap en el ImageView
                vista.setImageBitmap(bitmap);

                // Crear una instancia de Palette
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        // Obtener el color dominante
                        int dominantColor = palette.getDominantColor(0x000000);

                        // Convertir el color a formato hexadecimal
                        String hexColor = String.format("#%06X", (0xFFFFFF & dominantColor));

                        // Guardar el color en una variable
                        codigoHex = hexColor;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}