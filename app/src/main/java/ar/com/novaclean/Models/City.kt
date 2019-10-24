package ar.com.novaclean.Models

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException

class City:jsonableInterface{
    var name:String =""
    var Province:String =""
    override fun fromJson( jsonObject: JSONObject):Boolean{
        try{
            this.name = jsonObject.getString("name")
            val province = jsonObject.getJSONObject("Province")
            this.Province = province.getString("name")
        }catch (ex: JSONException){
            Log.d("Login", "JSON Exception: $ex")
            return false
        }catch (ex: ParseException){
            Log.d("Login", "Parse Exception: $ex")
            return false
        }
        return true
    }

}