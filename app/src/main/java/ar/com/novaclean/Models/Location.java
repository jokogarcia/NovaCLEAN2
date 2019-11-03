package ar.com.novaclean.Models;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/*`id` int(11) NOT NULL,
  `creado` timestamp NOT NULL DEFAULT current_timestamp(),
  `modificado` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `domicilio` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `contacto_local` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrato_numero` int(11) NOT NULL,
  `photo_url` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sectores` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;*/
public class Location implements Serializable,jsonableInterface {
    public int id;
    public String name;
    public String address_street_name;
    public String address_street_number;
    public String address_floor;
    public String address_appartment;
    public String phone_number;
    public String local_contact_name;
    public String local_contact_email;
    public String local_contact_phone;
    public String photo_url;
    public double latitude;
    public double longitude;
    public String contract_number;
    public _user Supervisor;

    public ArrayList<VisitEvent> visitEvents;
    public ArrayList<Sector> sectors;

    @Override
    public boolean fromJson(@NotNull JSONObject jsonObject) {
        try{
            this.id = jsonObject.getInt("id");
            this.address_street_name = jsonObject.getString("address_street_name");
            this.address_street_number = jsonObject.getString("address_street_number");
            this.address_floor = jsonObject.getString("address_floor");
            this.address_appartment = jsonObject.getString("address_appartment");
            this.phone_number = jsonObject.getString("phone_number");
            this.local_contact_name = jsonObject.getString("local_contact_name");
            this.local_contact_email = jsonObject.getString("local_contact_email");
            this.local_contact_phone = jsonObject.getString("local_contact_phone");
            this.contract_number = jsonObject.getString("contract_number");
            this.name = jsonObject.getString("name");
            this.latitude = jsonObject.getDouble("latitude");
            this.longitude = jsonObject.getDouble("longitude");
            this.Supervisor = new _user();
            this.Supervisor.fromJson(jsonObject.getJSONObject("supervisor"));
            this.photo_url = jsonObject.getString("photo_url");
            this.visitEvents = new ArrayList<>();
            sectors = new ArrayList<>();
            JSONArray jarray = jsonObject.getJSONArray("visit_events");
            for(int i=0;i<jarray.length();i++){
                JSONObject item = jarray.getJSONObject(i);
                VisitEvent VE = new VisitEvent();
                VE.fromJson(item);
                visitEvents.add(VE);
            }
            jarray = jsonObject.getJSONArray("sectors");
            for(int i=0;i<jarray.length();i++){
                JSONObject item = jarray.getJSONObject(i);
                Sector SE = new Sector();
                SE.fromJson(item);
                sectors.add(SE);
            }
        }catch ( JSONException ex){
            Log.d("Login", "JSON Exception in Location id "+this.id+": "+ex.toString());
            return false;
        }

        return true;
    }
}
