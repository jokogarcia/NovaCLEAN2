package ar.com.novaclean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.novaclean.Models.Constants;
import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Lugar;
import ar.com.novaclean.Models.Sector;
import ar.com.novaclean.Models.Tarea;
import ar.com.novaclean.R;

public class Calendario extends AppCompatActivity {
    private CompactCalendarView calendarView;
    private ArrayList<Evento> Itinerario;
    ArrayList<ExtendedEvent> ExtendedEvents;
    private int clientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Itinerario = new ArrayList<Evento>();
        setContentView(R.layout.activity_calendario);
        calendarView= findViewById(R.id.compactcalendar_view);
        clientId=getIntent().getIntExtra("ClienteID",0);
        String month= new SimpleDateFormat("MMMM").format(calendarView.getFirstDayOfCurrentMonth());
        ((TextView)findViewById(R.id.mesTV)).setText(month);
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendarView.getEvents(dateClicked);
                ArrayList<Evento> EventosDelDia=new ArrayList<>();
                for(ExtendedEvent E:ExtendedEvents){
                    for(Event CE:events){
                        if(CE.equals(E.CalendarEvent))
                            EventosDelDia.add(E.NovaEvent);
                    }
                }
                if(EventosDelDia.size()==0){
                    Toast.makeText(Calendario.this,"Sin eventos",Toast.LENGTH_LONG).show();
                }
                else if(EventosDelDia.size()==1){
                    //Un Solo evento, ir a ese evento;
                    Intent myIntent = new Intent(Calendario.this, DetallesEvento.class);
                    myIntent.putExtra("Evento",EventosDelDia.get(0));
                    startActivity(myIntent);
                }
                else{
                    Intent myIntent = new Intent(Calendario.this, ListaDeEventos.class);
                    myIntent.putExtra("Eventos",EventosDelDia);
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

    private void populateCalendario(ArrayList<Evento> itinerario) {
        String alldays = "DLMXJVS";
        ExtendedEvents = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (Evento E : itinerario) {
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            if (E.repetible == 1) {
                int CurrentMonth = cal.get(Calendar.MONTH);
                char thisDay;
                cal.setTime(calendarView.getFirstDayOfCurrentMonth());
                while (cal.get(Calendar.MONTH) == CurrentMonth) {
                    thisDay = alldays.charAt(cal.get(Calendar.DAY_OF_WEEK)-1);
                    if (E.dias.contains(String.valueOf(thisDay))
                            && E.fecha_inicio.before(cal.getTime())) {
                        //Do Stuff;
                        //Event(new Event(Color.CYAN, cal.getTimeInMillis()));
                        ExtendedEvent EV= new ExtendedEvent(E,Color.CYAN,cal.getTimeInMillis());
                        EV.NovaEvent.fecha = cal.getTime();
                        ExtendedEvents.add(EV);
                    }
                    cal.add(Calendar.DATE, 1);
                }
            }
            else{
                ExtendedEvents.add(new ExtendedEvent(E,Color.MAGENTA,E.fecha.getTime()));
            }
        }
        ArrayList<Event> EVS = new ArrayList<>();
        for(ExtendedEvent E : ExtendedEvents){
            EVS.add(E.CalendarEvent);
        }
        calendarView.removeAllEvents();
        calendarView.addEvents(EVS);
    }


    private void showProgressBar(boolean b) {
        ProgressBar PB = findViewById(R.id.progressBar);
        if(b)
            PB.setVisibility(View.VISIBLE);
        else
            PB.setVisibility(View.INVISIBLE);
    }
    private class ExtendedEvent{
        public Event CalendarEvent;
        public Evento NovaEvent;
        public ExtendedEvent(Evento E, int Color, long t){
            this.NovaEvent=E;
            this.CalendarEvent = new Event(Color,t);
        }
    }

}
