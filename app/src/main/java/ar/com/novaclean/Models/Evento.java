package ar.com.novaclean.Models;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Evento implements Serializable {
    public int id;
    public String lugar;
    public int repetible;
    public String dias;
    public Date fecha;
    public Date fecha_inicio;
    public int hora; //x60;
    public int duracion;
    public String tareas_ids;
    public ArrayList<Tarea> Tareas;
    public String empleados_ids;
    public int lugar_id;
    public ArrayList<Empleado> EmpleadosDesignados;

    public String getDias(){
        String retval="";
        if(this.dias == "LMXJV")
            retval="Lunes a Viernes";
        else if(this.dias == "LMXJVS")
            retval="Lunes a Sábado";
        else if(this.dias == "LMXJVSD")
            retval="Todos los días";
        else if(this.dias == "MXJVS")
            retval="Martes a Sábado";
        else if(this.dias == "MXJVSD")
            retval="Martes a Domingo";
        else{
            ArrayList<String> DaysArray = new ArrayList<>();
            if(this.dias.contains("L"))
                DaysArray.add("Lunes");
            if(this.dias.contains("M"))
                DaysArray.add("Martes");
            if(this.dias.contains("X"))
                DaysArray.add("Miércoles");
            if(this.dias.contains("J"))
                DaysArray.add("Jueves");
            if(this.dias.contains("V"))
                DaysArray.add("Viernes");
            if(this.dias.contains("S"))
                DaysArray.add("Sábado");
            if(this.dias.contains("D"))
                DaysArray.add("Domingo");
            if(DaysArray.size() == 0){
                retval="nunca";
            }
            else if(DaysArray.size() == 1){
                retval=DaysArray.get(0);
            }
            else{
                int i;
                for(i=0;i<DaysArray.size()-1;i++){
                    if(i!=0)
                        retval += ", ";
                    retval+= DaysArray.get(i);
                }
                retval +=" y "+DaysArray.get(i);
            }
        }
        return retval;





    }

    public String getHora(){
        return String.format("%02d:%02d",this.hora/60,this.hora%60);
    }

    public boolean isOnDate(Date time){
        Calendar dateToCheck = Calendar.getInstance();
        Calendar EventsDate = Calendar.getInstance();
        String week="DLMXJVS";
        String TodaysLetter=String.valueOf(week.charAt(dateToCheck.get(Calendar.DAY_OF_WEEK)-1));

        dateToCheck.setTime(time);
        EventsDate.setTime(this.fecha);
        if(this.repetible==0){
            return dateToCheck.get(Calendar.DAY_OF_YEAR) == EventsDate.get(Calendar.DAY_OF_YEAR) &&
                    dateToCheck.get(Calendar.YEAR) == EventsDate.get(Calendar.YEAR);
        }
        else
        {
            return (this.dias.contains(TodaysLetter) && this.fecha_inicio.before(time));
        }
    }


}