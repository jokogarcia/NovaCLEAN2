package ar.com.novaclean.Models

import android.util.Log
import ar.com.novaclean.RequestQueueSingleton
import ar.com.novaclean.Utils.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import org.json.JSONException
import java.text.ParseException
import java.text.SimpleDateFormat




open class _user:Serializable,jsonableInterface{





    var id=0
    var city_id=0
    var name=""
    var last_name=""
    var dni=""
    var phone=""
    var cuit=""
    var photo_url=""

    var birth_date:Date? = null
    var employee_start_date: Date? = null
    var created_at: Date? = null
    var updated_at: Date? = null

    var email =""
    var api_token=""
    val City = City()
    var UserRole = ""

    protected lateinit var loginResultListener:LoginResultListener
    protected lateinit var requestResultListener: RequestResultListener

    open override fun fromJson(data:JSONObject):Boolean{

        try{

            this.name = data.getString("name")
            this.id= data.getInt("id")
            this.city_id=data.getInt("city_id")
            this.last_name=data.getString("last_name")
            this.dni=data.getString("dni")
            this.phone=data.getString("phone")
            this.cuit=data.getString("cuit")
            this.photo_url=data.getString("photo_url")


            this.email =data.getString("email")
            this.api_token=data.getString("api_token")

            val _UserRole = data.getJSONObject("user_role")
            this.UserRole=_UserRole.getString("role")

            this.City.fromJson(data.getJSONObject("city"))


            this.created_at=SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(data.getString("created_at"))
            this.updated_at=SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(data.getString("updated_at"))
            if(data.getString("birth_date")!== "null")
                this.birth_date = SimpleDateFormat("yyyy-MM-dd").parse(data.getString("birth_date"))
            if(data.getString("employee_start_date")!== "null")
                this.employee_start_date = SimpleDateFormat("yyyy-MM-dd").parse(data.getString("employee_start_date"))




        }
        catch (ex:JSONException){
            Log.d("Login", "JSON Exception in User: $ex")
            return false
        }catch (ex:ParseException){
            Log.d("Login", "Parse Exception: $ex")
            return false
        }
        return true
    }



}