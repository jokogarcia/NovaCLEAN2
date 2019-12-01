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


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import ar.com.novaclean.Models.Location;
import ar.com.novaclean.Models.User;
import ar.com.novaclean.Models.VisitEvent;


public class Calendario extends AppCompatActivity {
    private CompactCalendarView calendarView;
    private ArrayList<VisitEvent> visitEvents;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitEvents = new ArrayList<VisitEvent>();
        setContentView(R.layout.activity_calendario);
        calendarView= findViewById(R.id.compactcalendar_view);
        Gson gson = new Gson();

        user =gson.fromJson(getIntent().getStringExtra("user"),User.class);
        if(user == null)
            finish();
        String month= new SimpleDateFormat("MMMM").format(calendarView.getFirstDayOfCurrentMonth());

        ((TextView)findViewById(R.id.mesTV)).setText(month);
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> clickedDayEvents = calendarView.getEvents(dateClicked);
                ArrayList<VisitEvent> thisDaysVisitEvents =new ArrayList<>();
                for(Event E:clickedDayEvents){
                    VisitEvent EV = (VisitEvent) E.getData();
                    EV.date = dateClicked;
                    thisDaysVisitEvents.add(EV);
                    }
                if(thisDaysVisitEvents.size()==0 && false){
                    Toast.makeText(Calendario.this,"Sin eventos",Toast.LENGTH_LONG).show();
                }
                else if(thisDaysVisitEvents.size()==1 && false){
                    //Un Solo evento, ir a ese evento;
                    Intent myIntent = new Intent(Calendario.this, DetallesEvento.class);
                    myIntent.putExtra("VisitEvent", thisDaysVisitEvents.get(0));
                    myIntent.putExtra("Usuario", user);
                    startActivity(myIntent);
                }
                else{
                    Intent myIntent = new Intent(Calendario.this, ListaDeEventos.class);
                    myIntent.putExtra("apiToken", user.getApi_token());
                    myIntent.putExtra("Date",dateClicked);

                    startActivity(myIntent);
                }
            }


            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String month= new SimpleDateFormat("MMMM").format(firstDayOfNewMonth);
                ((TextView)findViewById(R.id.mesTV)).setText(month);
                populateCalendario(visitEvents);
            }


        });

        getEventos();
        showProgressBar(false);
    }

    private void getEventos() {
        visitEvents.clear();
        for(Location location : user.getLocations()){
            for(VisitEvent visitEvent : location.visitEvents){
                visitEvents.add(visitEvent);
            }
        }
        populateCalendario(visitEvents);
    }

    private void populateCalendario(ArrayList<VisitEvent> itinerario) {
        ArrayList<Event> CalendarEvents= new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(calendarView.getFirstDayOfCurrentMonth());
        int currentMonth = calendar.get(Calendar.MONTH);
        while(calendar.get(Calendar.MONTH) == currentMonth){

            for(VisitEvent novaEvent:itinerario){
                if(novaEvent.isOnDate(calendar.getTime())) {
                    int color = novaEvent.repeats ? Color.MAGENTA : Color.CYAN;
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
