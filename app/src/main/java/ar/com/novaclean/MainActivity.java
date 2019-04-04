package ar.com.novaclean;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import ar.com.novaclean.Models.Evento;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Evento> Eventos;
    private int ClienteID;
    private Button LogInButton;
    private Context ThisActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThisActivity=this;
        setContentView(R.layout.activity_main);
        LogInButton = findViewById(R.id.loginButton);
        ClienteID=0;

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ThisActivity, Calendario.class);
                myIntent.putExtra("ClienteID",ClienteID);
                startActivity(myIntent);
            }
        });
    }
}
