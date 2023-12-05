package twin.developers.projectmqtt;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import androidx.annotation.NonNull;
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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class escuchar_color extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech mTts;
    String codHex;
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri photoUri2;
    TextView nombreColor;
    ImageView vistaColor;
    Button capturarColor, escuchar;
    DatabaseReference nombreYcodigo;

    // Método onCreate se llama cuando se crea la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escuchar_color);
        mTts = new TextToSpeech(this, this);
        vistaColor = findViewById(R.id.ivColorTomado);
        capturarColor = findViewById(R.id.bttnAbrirCamara);
        escuchar = findViewById(R.id.bttnEscuchar);
        nombreColor = findViewById(R.id.txtViewColor);

        // Obtención de la referencia a la lista de tareas en Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        nombreYcodigo = database.getReference("listaColores");

        // Solicitar permiso para la cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }

        capturarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        escuchar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = "";
                String codigo = codHex.toString();

                // Crear un nuevo objeto Color
                Color nuevoColor = new Color(nombre, codigo);

                nombreYcodigo.orderByChild("codigoColor").equalTo(codigo).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // El código hexadecimal existe en la base de datos
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Color color = snapshot.getValue(Color.class);
                                nombreColor.setText(color.getNombreColor());
                            }
                        } else {
                            Toast.makeText(escuchar_color.this, "El código hexadecimal no existe en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                // Obtener el texto del TextView "nombreColor"
                String texto = nombreColor.getText().toString();

                // Convertir el texto a voz utilizando el motor TextToSpeech
                speak(texto);
            }
        });
    }

    private void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoUri2 = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        camera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri2);
        startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);
    }

    // Método onInit se llama cuando se inicializa el motor TextToSpeech
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) { // Si la inicialización fue exitosa
            // Crea un objeto Locale para español
            Locale locSpanish = new Locale("spa", "ESP");
            // Verifica si el idioma español está disponible en el dispositivo
            int result = mTts.isLanguageAvailable(locSpanish);

            if (result == TextToSpeech.LANG_COUNTRY_AVAILABLE) { // Si el idioma español está disponible
                mTts.setLanguage(locSpanish); // Establece el idioma del motor TextToSpeech a español
                speak("¡Hola, bienvenido a la aplicación Escuchando los colores!"); // Convierte el texto a voz
            } else {
                Log.e(TAG, "Lenguaje no encontrado"); // Si el idioma español no está disponible, registra un error
            }
        } else {
            Log.e(TAG, "Initialization failed"); // Si la inicialización del motor TextToSpeech falla, registra un error
        }
    }

    // Método speak convierte el texto a voz usando el objeto TextToSpeech
    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // Si la versión de Android es LOLLIPOP o superior
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null); // Convierte el texto a voz
        } else { // Si la versión de Android es inferior a LOLLIPOP
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null); // Convierte el texto a voz
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            try {
                // Obtener un ContentResolver
                ContentResolver resolver = getContentResolver();

                // Usar el ContentResolver para obtener un Bitmap de la imagen
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, photoUri2);

                // Cargar el Bitmap en el ImageView
                vistaColor.setImageBitmap(bitmap);

                // Crear una instancia de Palette
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        // Obtener el color dominante
                        int dominantColor = palette.getDominantColor(0x000000);

                        // Convertir el color a formato hexadecimal
                        String hexColor = String.format("#%06X", (0xFFFFFF & dominantColor));

                        // Guardar el color en una variable
                        codHex = hexColor;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}