package ar.com.novaclean.Models;

import android.util.Log;

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
    public Sector Sector;

    @Override
    public boolean fromJson(@NotNull JSONObject jsonObject) {
        try{
            this.id = jsonObject.getInt("id");
            this.descripcion = jsonObject.getString("description");
            this.duration = Time.valueOf(jsonObject.getString("duration"));
            this.frequency = jsonObject.getInt("frequency");
            this.Sector = new Sector();
            this.Sector.fromJson(jsonObject.getJSONObject("Sector"));
        }catch ( JSONException ex){
            Log.d("Login", "JSON Exception: "+ex.toString());
            return false;
        }
        return true;
    }
}
