package ar.com.novaclean;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;

public class Reclamo extends AppCompatActivity {
    enum _State{
        INITIAL,
        PUNTUALIDAD,
        CALIDAD,
        CUMPLIMIENTO,
        COMENTARIO

    }
    _State State = _State.INITIAL;
    TextView Pregunta;
    EditText RespuestaET;
    Button Button1;
    Button Button2;
    Button Button3;
    ReclamoData ReclamoData;
    Evento EventoActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventoActual = (Evento) getIntent().getSerializableExtra("Evento");
        setContentView(R.layout.activity_reclamo);
        Pregunta = findViewById(R.id.PreguntaTV);
        RespuestaET = findViewById(R.id.RespuestaET);
        Button1 = findViewById(R.id.button1);
        Button2 = findViewById(R.id.button2);
        Button3 = findViewById(R.id.button3);
        ReclamoData = new ReclamoData();
        ReclamoData.EventoId=EventoActual.id;
        ReclamoData.FechaTimestamp=EventoActual.fecha.getTime()/1000;
        updateGUI();
    }
    void updateGUI(){
        switch (State){
            case INITIAL:
                Pregunta.setText("¿Qué tipo de reclamo desea realizar?");
                Button1.setText("Puntualidad");
                Button2.setText("Calidad de servicio");
                Button2.setText("Cumplimiento de objetivos");
                RespuestaET.setVisibility(View.GONE);
                break;
            case PUNTUALIDAD:
                //Puntualidad A
                Pregunta.setText("En esta ocasión, nuestros representantes fueron");
                Button1.setText("Puntuales");
                Button2.setText("Impuntuales (hasta 10'')");
                Button3.setText("Muy impuntuales (más de 10 minutos)");
                RespuestaET.setVisibility(View.GONE);
                break;
            case CALIDAD://Calidad de servicio
                Pregunta.setText("Elija una opción");
                Button1.setText("Todas las tareas se realizaron adecuadamente");
                Button2.setText("Estoy insatisfecho con una o mas tareas realizadas");
                Button3.setVisibility(View.GONE);
                RespuestaET.setVisibility(View.GONE);
                break;
            case CUMPLIMIENTO://Cumplimiento de objetivos
                Pregunta.setText("Elija una opción");
                Button1.setText("Todas los objetivos fueorn cumplidos");
                Button2.setText("Quedaron tareas sin realizar");
                Button3.setVisibility(View.GONE);
                RespuestaET.setVisibility(View.GONE);
                break;
            case COMENTARIO: //Comentario con foto
                Pregunta.setText("Puede escribir un comentario con foto");
                RespuestaET.setVisibility(View.VISIBLE);
                RespuestaET.setHint("Comentario");
                Button1.setText("Tomar una foto");
                Button2.setText("Enviar");
                Button3.setVisibility(View.GONE);
                break;


        }
    }
    void onClick(View v){
        switch (State){
            case INITIAL:
                switch (v.getId()){
                    case R.id.button1:
                        ReclamoData.Tipo=1;
                        State=State.PUNTUALIDAD;
                        break;
                    case R.id.button2:
                        ReclamoData.Tipo=2;
                        State=State.CALIDAD;
                        break;
                    case R.id.button3:
                        ReclamoData.Tipo=3;
                        State= State.CUMPLIMIENTO;
                        break;
                }
                break;
            case PUNTUALIDAD:
                switch (v.getId()){
                    case R.id.button1:
                        ReclamoData.Detalles="PUNTUAL";
                        State=State.COMENTARIO;
                    break;
                    case R.id.button2:
                        ReclamoData.Detalles="IMPUNTUAL<10";
                        State=State.COMENTARIO;
                    break;
                    case R.id.button3:
                        ReclamoData.Detalles="IMPUNTUAL>10";
                        State=State.COMENTARIO;
                    break;
                }
            break;
            case CALIDAD:
            case CUMPLIMIENTO:
                switch (v.getId()) {
                    case R.id.button1:
                        ReclamoData.Detalles = "OK";
                        State = State.COMENTARIO;
                        break;
                    case R.id.button2:
                        Intent getTareasIntent = new Intent(this, ReclamoPickTarea.class);
                        getTareasIntent.putExtra("ReclamoData",ReclamoData);
                        getTareasIntent.putExtra("Evento",EventoActual);
                        startActivityForResult(getTareasIntent, Constants.RQTareas);
                        break;
                }
            break;
            case COMENTARIO:
                switch (v.getId()){
                    case R.id.button1:
                        Toast.makeText(this,"Tomar foto no implementado",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.button2:
                        ReclamoData.Comentario=RespuestaET.getText().toString();
                        Toast.makeText(this,"Enviar no implementado",Toast.LENGTH_LONG).show();
                        break;
                }
            break;
        }
        updateGUI();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.RQTareas) {
            if(resultCode == Activity.RESULT_OK){
                ReclamoData= (ReclamoData) data.getSerializableExtra("ReclamoData");
                State=State.COMENTARIO;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
            updateGUI();

        }
    }//onActivityResult
    public class ReclamoData implements Serializable {
        public int Tipo=0;
        public long FechaTimestamp;
        public int EventoId;
        public String Detalles;
        public String foto_url;
        public String Comentario;
    }
}
