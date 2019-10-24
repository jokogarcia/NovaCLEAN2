package ar.com.novaclean.Models

import org.json.JSONObject

interface jsonableInterface{
    fun fromJson(jsonObject: JSONObject):Boolean
    /*
     try{

        }catch (ex: JSONException){
            Log.d("Login", "JSON Exception: $ex")
            return false
        }catch (ex: ParseException){
            Log.d("Login", "Parse Exception: $ex")
            return false
        }
        return true
    }*/
}