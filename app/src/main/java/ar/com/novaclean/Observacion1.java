package ar.com.novaclean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;

public class Observacion1 extends AppCompatActivity {

    private Evento EventoActual;
    boolean isBefore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion1);

        Button Button1 = findViewById(R.id.obsButton1);
        Button Button2 = findViewById(R.id.obsButton2);

        EventoActual = (Evento) getIntent().getSerializableExtra("Evento");
        Date today= new Date();
        isBefore =(EventoActual.fecha.after(today));
        if(isBefore){
            Button1.setText("Solicitar reprogramacion");
            Button2.setVisibility(View.INVISIBLE);
        }


    }
    public void buttonService(View v){
        switch (v.getId()){
            case R.id.obsButton1:
                if(isBefore){
                    //Solicitar reprogramacion
                    Toast.makeText(getApplicationContext(),"No implementado",Toast.LENGTH_LONG).show();
                }
                else{
                    //Calificar
                    Intent myIntent = new Intent(getApplicationContext(), Calificar.class);
                    myIntent.putExtra("Evento",EventoActual);
                    startActivityForResult(myIntent,Constants.RQCalificar);

                }

                break;
            case R.id.obsButton2:
                //Reclamo:
                Intent myIntent = new Intent(getApplicationContext(), Reclamo.class);
                myIntent.putExtra("Evento",EventoActual);
                startActivityForResult(myIntent, Constants.RQReclamo);

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK)
            finish();

    }


}
