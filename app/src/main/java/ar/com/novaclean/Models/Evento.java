package ar.com.novaclean.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;



import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Calendario;
import ar.com.novaclean.MySingleton;

import static android.content.Context.MODE_PRIVATE;

public class Evento implements Serializable {
    public int id;
    public String lugar;
    public int repetible;
    public String dias;
    public Date fecha;
    public Date fecha_inicio;
    public int hora_inicio; //x60;
    public int duracion;
    public String tareas_ids;
    public ArrayList<Tarea> Tareas;
    public String empleados_ids;
    public int lugar_id;
    public ArrayList<Empleado> EmpleadosDesignados;
    public String getHora(){
        int horas = this.hora_inicio/60;
        int minutos = this.hora_inicio % 60;
        return String.format("%02d:%02d",horas,minutos);
    }

    public String getDias() {
        if (this.repetible == 1) {
            ArrayList<String> dias = new ArrayList<>();
            for (int i = 0; i < this.dias.length(); i++) {
                char c = this.dias.charAt(i);
                switch (c) {
                    case 'L':
                        dias.add("Lunes");
                        break;
                    case 'M':
                        dias.add("Martes");
                        break;
                    case 'X':
                        dias.add("Miércoles");
                        break;
                    case 'J':
                        dias.add("Jueves");
                        break;
                    case 'V':
                        dias.add("Viernes");
                        break;
                    case 'S':
                        dias.add("Sábado");
                        break;
                    case 'D':
                        dias.add("Domingo");
                        break;
                }
            }
            if (dias.size() == 1)
                return dias.get(0);
            String titulo = "";
            for (int i = 0; i < dias.size(); i++) {
                titulo += dias.get(i);
                if(i == dias.size()-2)
                    titulo += " y ";
                else if (i != dias.size() - 1)
                    titulo += ", ";
            }


            return titulo;
        } else
            return (this.fecha.toString());

    }
    public boolean isOnDate(Date date){
        boolean response=false;

        if(this.repetible != 0){
            if(this.dias.contains(dateToL(date)) && this.fecha_inicio.before(date))
                response = true;
        }
        else{
            response = this.fecha == date;
        }
        return response;
    }
    private String dateToL(Date date){
        String alldays = " DLMXJVS";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(alldays.charAt(calendar.get(Calendar.DAY_OF_WEEK)));
    }

}
