package ar.com.novaclean.Models;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/*`id` int(11) NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `photo_url` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tareas_ids` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL*/
public class Sector implements Serializable,jsonableInterface {
    public int id;
    public String name, photo_url, description;
    public ArrayList<CleaningTask> cleaningTasks;

    @Override
    public boolean fromJson(@NotNull JSONObject jsonObject) {
        try{
            this.id = jsonObject.getInt("id");

            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");

            this.photo_url = jsonObject.getString("photo_url");

            JSONArray jarray = jsonObject.getJSONArray("tasks");
            cleaningTasks = new ArrayList<>();
            for(int i=0;i<jarray.length();i++){
                JSONObject item = jarray.getJSONObject(i);
                CleaningTask cleaningTask = new CleaningTask();
                cleaningTask.fromJson(item);
                //cleaningTask.sector_id=this.id;
                cleaningTasks.add(cleaningTask);
            }


        }catch ( JSONException ex){
            Log.d("Login", "JSON Exception in Sector: "+ex.toString());
            return false;
        }

        return true;
    }
}
