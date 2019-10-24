package ar.com.novaclean.Models;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Complaint implements Serializable,jsonableInterface {
    public int complaint_type =0;
    public Date referenceDate;
    public String comment;
    public String photo_url;
    public int visit_event_id;

    @Override
    public boolean fromJson(@NotNull JSONObject jsonObject) {
        try{
            this.complaint_type = jsonObject.getInt("complaint_type");

            this.referenceDate =  (new SimpleDateFormat("yyyy-MM-dd")).parse(jsonObject.getString("reference_date"));
            this.comment = jsonObject.getString("comment");
            this.photo_url = jsonObject.getString("photo_url");
        }catch ( JSONException ex){
            Log.d("Login", "JSON Exception: "+ex.toString());
            return false;
        }
        catch ( ParseException ex){
            Log.d("Login", "Parse Exception: "+ex.toString());
            return false;
        }
        return true;
    }
}
