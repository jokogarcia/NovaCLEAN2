package ar.com.novaclean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Lugar;
import ar.com.novaclean.Models.Sector;
import ar.com.novaclean.Models.Tarea;
import ar.com.novaclean.R;

public class Calendario extends AppCompatActivity {
    private CalendarView calendarView;
    private ArrayList<Evento> Itinerario;
    private int clientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Itinerario = new ArrayList<Evento>();
        setContentView(R.layout.activity_calendario);
        calendarView= findViewById(R.id.calendarView);
        clientId=getIntent().getIntExtra("ClienteID",0);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            //Obtener fecha Seleccionada
            Calendar calendar = new GregorianCalendar(year,month,dayOfMonth);
            String[] strDays = new String[] {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves",
                        "Viernes", "Sabado" };
            String DiaDeLaSemana=strDays[calendar.get(Calendar.DAY_OF_WEEK)-1];
            ArrayList<Evento> EventosDelDia=new ArrayList<Evento>();
            for(Evento E : Itinerario){
                Boolean selectme=false;
                if(E.Repetible){
                    if(E.Dias.contains(DiaDeLaSemana)){
                        selectme=true;
                    }
                }
                else{
                    Date tmp=calendar.getTime();
                    if(E.Fecha.equals(tmp)){
                        selectme=true;
                    }
                }
                if(selectme){
                    EventosDelDia.add(E);
                }
            }
            if(EventosDelDia.size()==0){
                Toast.makeText(getApplicationContext(), "Sin eventos en el día seleccionado",
                        Toast.LENGTH_SHORT).show();
            }
            else if(EventosDelDia.size()==1){
                //Un Solo evento, ir a ese evento;
                Intent myIntent = new Intent(getApplicationContext(), DetallesEvento.class);
                myIntent.putExtra("Evento",EventosDelDia.get(0));
                startActivity(myIntent);
            }
            else{
                //Varios eventos. Ir a la lista de eventos;
                Intent myIntent = new Intent(getApplicationContext(), ListaDeEventos.class);
                myIntent.putExtra("Eventos",EventosDelDia);
                startActivity(myIntent);
            }

            }
        });
        getEventos(clientId);
    }

    private void getEventos(final int clientId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_EVENTOS_FROM_CLIENTES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        Evento[] eventos;
                        try{
                            eventos = g.fromJson(response, Evento[].class);
                            Itinerario = new ArrayList<>(Arrays.asList(eventos));
                        }catch(IllegalStateException e){
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG);
                        }
                        showProgressBar(false);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Calendario.this,error.toString(),Toast.LENGTH_LONG).show();
                        showProgressBar(false);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                SharedPreferences sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
                String tok= sharedPreferences.getString("token","");
                Map<String,String> params = new HashMap<String, String>();
                params.put("client_id",""+clientId);
                params.put("tok",tok);
                return params;
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void showProgressBar(boolean b) {
        ProgressBar PB = findViewById(R.id.progressBar);
        if(b)
            PB.setVisibility(View.VISIBLE);
        else
            PB.setVisibility(View.INVISIBLE);
    }


}
