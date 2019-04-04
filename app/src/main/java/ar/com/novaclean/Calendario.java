package ar.com.novaclean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ar.com.novaclean.Models.Evento;
import ar.com.novaclean.Models.Tarea;
import ar.com.novaclean.R;

public class Calendario extends AppCompatActivity {
    private CalendarView calendarView;
    private ArrayList<Evento> Itinerario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Itinerario = new ArrayList<Evento>();
        Evento Ev1 = new Evento();
        Ev1.Tareas=new ArrayList<Tarea>();
        Tarea T1 = new Tarea();
        T1.id=10;
        T1.Descripcion="Barrer el piso";
        T1.Minutos=3;
        Tarea T2 = new Tarea();
        T2.id=11;
        T2.Descripcion="Barrer el techo";
        T2.Minutos=3;
        Ev1.Repetible=true;
        Ev1.Tareas.add(T1);
        Ev1.Tareas.add(T2);
        Ev1.Dias= "Lunes,Miercoles,Viernes";
        Ev1.Hora=18*60+30;
        Itinerario.add(Ev1);
        setContentView(R.layout.activity_calendario);
        calendarView= findViewById(R.id.calendarView);
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
    }
}
