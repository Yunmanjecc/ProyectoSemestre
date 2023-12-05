package twin.developers.projectmqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import java.util.ArrayList;

public class colores_guardados extends AppCompatActivity implements ValueEventListener{
    TableLayout tableColores;
    DatabaseReference coloresRef;
    List<Color> colores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colores_guardados);

        tableColores = findViewById(R.id.tlColores);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        coloresRef = database.getReference("listaColores"); // Obtener la referencia a la lista de tareas

        // Inicializar la lista de tareas
        colores = new ArrayList<>();

        // Configurar el ValueEventListener
        coloresRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Limpiar la lista de tareas
        colores.clear();

        // Añadir las tareas a la lista
        for (DataSnapshot tareaSnapshot : dataSnapshot.getChildren()) {
            String nombreColor = tareaSnapshot.child("nombreColor").getValue(String.class);
            String codigoColor = tareaSnapshot.child("codigoColor").getValue(String.class);

            // Crear una nueva instancia de Color y agregarla a la lista
            Color colorObj = new Color(nombreColor, codigoColor);
            colores.add(colorObj);
        }

        // Referencia a TableLayout
        TableLayout tableColores = findViewById(R.id.tlColores);

        // Agregar las filas a la tabla
        for (Color color : colores) {
            // Crea una nueva fila para cada campo de la tarea y lo añade a la tabla
            TableRow filaColor = new TableRow(this);
            TextView colorText = new TextView(this);
            colorText.setText("Color: " + color.getNombreColor());
            filaColor.addView(colorText);
            tableColores.addView(filaColor);

            TableRow filaCodigo = new TableRow(this);
            TextView codigoText = new TextView(this);
            codigoText.setText("Codigo color: " + color.getCodigoColor());
            filaCodigo.addView(codigoText);
            tableColores.addView(filaCodigo);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Manejar el error
    }
}