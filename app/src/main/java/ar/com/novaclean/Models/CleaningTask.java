package ar.com.novaclean.Models;

import android.util.Log;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;

public class CleaningTask implements Serializable,jsonableInterface {
    public int id;
    public String descripcion;
    public Time duration;
    public int frequency;
    public int sector_id;
    //public Sector Sector;

    @Override
    public boolean fromJson(@NotNull JSONObject jsonObject) {
        try{
            this.id = jsonObject.getInt("id");
            this.descripcion = jsonObject.getString("description");
            this.duration = Time.valueOf(jsonObject.getString("duration"));
            this.frequency = jsonObject.getInt("frequency");
            this.sector_id = jsonObject.getInt("sector_id");


            //this.Sector.fromJson(jsonObject.getJSONObject("sector"));
        }catch ( JSONException ex){
            Log.d("Login", "JSON Exception in CleaningTask id "+this.id+": "+ex.toString());
            return false;
        }
        return true;
    }
}
