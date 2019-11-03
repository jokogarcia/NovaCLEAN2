package ar.com.novaclean.Models;



import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;


public class VisitEvent implements Serializable,jsonableInterface {
    public int id;
    public boolean repeats;

    public Date starting_date;
    public Time starts_at;
    public Time duration;
    public Date date;
    public ArrayList<CleaningTask> cleaningTasks;
    public ArrayList<_user> asignedEmployees;
    public boolean monday;
    public boolean tuesday;
    public boolean wednesday;
    public boolean thursday;
    public boolean friday;
    public boolean saturday;
    public boolean sunday;

    @Override
    public boolean fromJson(@NotNull JSONObject jsonObject) {
        try{
            this.id = jsonObject.getInt("id");
            this.repeats  = jsonObject.getInt("repeats")!=0;
            this.starting_date =  new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("starting_date"));
            this.duration = Time.valueOf(jsonObject.getString("duration"));
            this.starts_at = Time.valueOf(jsonObject.getString("starts_at"));
            this.date =  new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("date"));
            this.monday  = jsonObject.getInt("monday")!=0;
            this.tuesday  = jsonObject.getInt("tuesday")!=0;
            this.wednesday  = jsonObject.getInt("wednesday")!=0;
            this.thursday  = jsonObject.getInt("thursday")!=0;
            this.friday  = jsonObject.getInt("friday")!=0;
            this.saturday  = jsonObject.getInt("saturday")!=0;
            this.sunday  = jsonObject.getInt("sunday")!=0;



            JSONArray jarray = jsonObject.getJSONArray("tasks");
            cleaningTasks = new ArrayList<>();
            for(int i=0;i<jarray.length();i++){
                JSONObject item = jarray.getJSONObject(i);
                CleaningTask cleaningTask = new CleaningTask();
                cleaningTask.fromJson(item);
                cleaningTasks.add(cleaningTask);
            }
            jarray = jsonObject.getJSONArray("assigned_employees");

            this.asignedEmployees = new ArrayList<>();
            for(int i=0;i<jarray.length();i++){
                JSONObject item = jarray.getJSONObject(i);
                _user user = new _user();
                user.fromJson(item);
                asignedEmployees.add(user);
            }





        }catch ( JSONException ex){
            Log.d("Login", "JSON Exception: "+ex.toString());
            return false;
        }catch ( ParseException ex){
            Log.d("Login", "Parse Exception: "+ex.toString());
            return false;
        }

        return true;
    }

    public String getDias(){
        String retval="";
        if(!repeats){
            return this.date.toString();
        }
        if(monday &&
                tuesday &&
                wednesday &&
                thursday &&
                friday &&
                saturday &&
                sunday)
            retval = "Lunes a Domingo";
        else if(monday &&
                tuesday &&
                wednesday &&
                thursday &&
                friday &&
                saturday){
            retval="Lunes a Sábado";
        }
        else if(monday &&
                tuesday &&
                wednesday &&
                thursday &&
                friday ){
            retval="Lunes a Viernes";
        }
        else if(
                tuesday &&
                wednesday &&
                thursday &&
                friday &&
                saturday &&
                sunday){
            retval="Martes a Domingo";
        }
        else if( tuesday &&
                wednesday &&
                thursday &&
                friday &&
                saturday ){
            retval="Martes a Sábado";
        }
        else{
            ArrayList<String> sArray = new ArrayList<>();
            if(monday)
                sArray.add("Lunes");
            if(tuesday)
                sArray.add("Martes");
            if(wednesday)
                sArray.add("Miércoles");
            if(thursday )
                sArray.add("Jueves");
            if(friday)
                sArray.add("Viernes");
            if(saturday)
                sArray.add("Sábado");
            if(sunday)
                sArray.add("Domingo");
            if(sArray.size()==0){
                return "";
            }
            StringBuilder builder = new StringBuilder();
            int i;
            for (i=0;i<sArray.size()-2;i++) {
                if(i!=0)
                    builder.append(", ");
                builder.append(sArray.get(i));

            }
            builder.append(" y ");
            builder.append(sArray.get(i));
            retval=builder.toString();

        }


        return retval;





    }

    public String getHora(){
        return starts_at.toString();
    }

    public boolean isOnDate(Date time){
        if(!this.repeats){
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            return (fmt.format(this.date).equals(fmt.format(time)));
            /*return dateToCheck.get(Calendar.DAY_OF_YEAR) == EventsDate.get(Calendar.DAY_OF_YEAR) &&
                    dateToCheck.get(Calendar.YEAR) == EventsDate.get(Calendar.YEAR);*/
        }
        else if(this.starting_date.before(time))
        {
            Calendar dateToCheck = Calendar.getInstance();
            dateToCheck.setTime(time);
            switch(dateToCheck.get(Calendar.DAY_OF_WEEK)){
                case 1: return sunday;
                case 2: return monday;
                case 3: return tuesday;
                case 4: return wednesday;
                case 5: return thursday;
                case 6: return friday;
                case 7: return saturday;
                default:return false;

            }
        }
        return false;
    }



}