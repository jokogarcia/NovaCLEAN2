package ar.com.novaclean;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ar.com.novaclean.Models.Evento;

public class ListaDeEventos extends AppCompatActivity {

    ArrayList<Evento> Eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_eventos);
        //        restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        Eventos = (ArrayList<Evento>) getIntent().getSerializableExtra("Eventos");

    }
}
