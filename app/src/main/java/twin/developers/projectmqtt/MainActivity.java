package twin.developers.projectmqtt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    //Pantalla principal que derivará al usuario a las distintas pantallas
    Button escuchar, asignar, ubicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        escuchar = findViewById(R.id.bttnEscucharColor);
        asignar = findViewById(R.id.bttnAsignar);
        ubicacion = findViewById(R.id.bttnMapa);

        escuchar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), escuchar_color.class
                );
                startActivity(intent);
            }
        });

        asignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), asignar.class
                );
                startActivity(intent);
            }
        });

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), ubicacion.class
                );
                startActivity(intent);
            }
        });
    }
}