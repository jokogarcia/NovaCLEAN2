package ar.com.novaclean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;

import ar.com.novaclean.Models.Usuario;

public class Calendario extends AppCompatActivity {
    private CompactCalendarView calendarView;
    private ArrayList<Evento> Itinerario;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Itinerario = new ArrayList<Evento>();
        setContentView(R.layout.activity_calendario);
        calendarView= findViewById(R.id.compactcalendar_view);
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        if(usuario == null)
            finish();
        String month= new SimpleDateFormat("MMMM").format(calendarView.getFirstDayOfCurrentMonth());
        ((TextView)findViewById(R.id.mesTV)).setText(month);
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> clickedDayEvents = calendarView.getEvents(dateClicked);
                ArrayList<Evento> EventosDelDia=new ArrayList<>();
                for(Event E:clickedDayEvents){
                    Evento EV = (Evento) E.getData();
                    EV.fecha = dateClicked;
                    EventosDelDia.add(EV);
                    }
                if(EventosDelDia.size()==0 && false){
                    Toast.makeText(Calendario.this,"Sin eventos",Toast.LENGTH_LONG).show();
                }
                else if(EventosDelDia.size()==1 && false){
                    //Un Solo evento, ir a ese evento;
                    Intent myIntent = new Intent(Calendario.this, DetallesEvento.class);
                    myIntent.putExtra("Evento",EventosDelDia.get(0));
                    myIntent.putExtra("Usuario",usuario);
                    startActivity(myIntent);
                }
                else{
                    Intent myIntent = new Intent(Calendario.this, ListaDeEventos.class);
                    myIntent.putExtra("Eventos",EventosDelDia);
                    myIntent.putExtra("Usuario",usuario);
                    myIntent.putExtra("Date",dateClicked);

                    startActivity(myIntent);
                }
            }


            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String month= new SimpleDateFormat("MMMM").format(firstDayOfNewMonth);
                ((TextView)findViewById(R.id.mesTV)).setText(month);
                populateCalendario(Itinerario);
            }


        });

        getEventos();
    }

    private void getEventos() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_EVENTOS_FROM_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Gson g = new Gson();
                        Evento[] eventos;
                        try{
                            eventos = g.fromJson(response, Evento[].class);
                            Itinerario = new ArrayList<>(Arrays.asList(eventos));
                            populateCalendario(Itinerario);
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
                return usuario.getLoginParams();
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void populateCalendario(ArrayList<Evento> itinerario) {
        ArrayList<Event> CalendarEvents= new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(calendarView.getFirstDayOfCurrentMonth());
        int currentMonth = calendar.get(Calendar.MONTH);
        while(calendar.get(Calendar.MONTH) == currentMonth){

            for(Evento novaEvent:itinerario){
                if(novaEvent.isOnDate(calendar.getTime())) {
                    int color = novaEvent.repetible == 0 ? Color.MAGENTA : Color.CYAN;
                    Event E = new Event(color, calendar.getTimeInMillis(), novaEvent);
                    CalendarEvents.add(E);
                }
            }
            
            calendar.add(Calendar.DATE,1);
        }
//
        calendarView.removeAllEvents();
        calendarView.addEvents(CalendarEvents);
    }


    private void showProgressBar(boolean b) {
        ProgressBar PB = findViewById(R.id.progressBar);
        if(b)
            PB.setVisibility(View.VISIBLE);
        else
            PB.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hamburger, menu);
        return true;
    }
    public void MenuClickEvents(MenuItem menuItem){
        boolean doFinish=false;
        switch (menuItem.getItemId()){
            case R.id.mi_cerrarSesion:
                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("iniciarAutomatico", false);
                if(!editor.commit())
                    Toast.makeText(getApplicationContext(),"Not commited!",Toast.LENGTH_LONG);
                doFinish=true;
                break;

        }
        if(doFinish)
            finish();

    }

}
