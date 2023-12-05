package twin.developers.projectmqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class asignar extends AppCompatActivity {

    Button nuevoColor, verColores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar);

        nuevoColor = findViewById(R.id.bttnNuevoColor);
        verColores = findViewById(R.id.bttnColoresGuardados);

        nuevoColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), nuevo_color.class
                );
                startActivity(intent);
            }
        });

        verColores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), colores_guardados.class
                );
                startActivity(intent);
            }
        });

    }
}