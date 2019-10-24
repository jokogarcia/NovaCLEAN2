package ar.com.novaclean.Models

import android.util.Log
import ar.com.novaclean.Utils.*
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import org.json.JSONException
import java.text.ParseException
import java.text.SimpleDateFormat


class User:Serializable,RequestCallbackInterface{


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
    val Locations = mutableListOf<Location>()
    val City = City()
    var UserRole = ""

    private lateinit var loginResultListener:LoginResultListener
    private lateinit var requestResultListener: RequestResultListener

    fun fromJSON(jsonObject:JSONObject):Boolean{

        try{
            val data = jsonObject.getJSONObject("data")
            this.name = data.getString("name")
            this.id= data.getInt("id")
            this.city_id=data.getInt("city_id")
            this.last_name=data.getString("last_name")
            this.dni=data.getString("dni")
            this.phone=data.getString("phone")
            this.cuit=data.getString("cuit")
            this.photo_url=data.getString("photo_url")

            this.birth_date = SimpleDateFormat("yyyy-MM-dd").parse(data.getString("birth_date"))
            if(data.getString("employee_start_date")!== "null")
                this.employee_start_date = SimpleDateFormat("yyyy-MM-dd").parse(data.getString("employee_start_date"))
            this.created_at=SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(data.getString("created_at"))
            this.updated_at=SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(data.getString("updated_at"))

            this.email =data.getString("email")
            this.api_token=data.getString("api_token")

            val _UserRole = data.getJSONObject("user_role")
            this.UserRole=_UserRole.getString("role")

            this.City.fromJson(data.getJSONObject("city"))
            if(data.has("Locations")){
                Locations.clear()
                val jarray = jsonObject.getJSONArray("Locations")

                for (i in 0 until jarray.length()) {
                    val item = jarray.getJSONObject(i)
                    val location = Location()
                    location.fromJson(item)
                    Locations.add(location)
                }

            }



        }
        catch (ex:JSONException){
            Log.d("Login", "JSON Exception: $ex")
            return false
        }catch (ex:ParseException){
            Log.d("Login", "Parse Exception: $ex")
            return false
        }
        return true
    }

    override fun Callback(requestCode: Int, JSONObject: JSONObject) {
        when(requestCode){
            0->{
                if(!this.fromJSON(JSONObject))
                    this.ErrorCallback(requestCode,null)
                else
                    loginResultListener.OnLoginResult(LoginResultListener.LoginResult(true,this))
            }
            1->{
                if(!this.fromJSON(JSONObject))
                    requestResultListener.OnRequestResult(RequestResult(null,false))
                requestResultListener.OnRequestResult(RequestResult(null,true))
            }
            2->{
                if(!this.fromJSON(JSONObject)){
                    requestResultListener.OnRequestResult(RequestResult(null,false))
                }
                requestResultListener.OnRequestResult(RequestResult(null,true))
            }
        }
    }

    override fun ErrorCallback(requestCode: Int, error: VolleyError?) {
        when(requestCode){
            0->{
                val LoginResult = LoginResultListener.LoginResult(error)
                loginResultListener.OnLoginResult(LoginResult)
            }



        }
    }
    fun RequestLogin(password: String, loginResultListener: LoginResultListener) {
        val Credentials = HashMap<String, String>()
        Credentials["email"] = this.email
        Credentials["password"] = password
        val CredentialsJSON = JSONObject(Credentials)
        this.loginResultListener = loginResultListener

        val requester = Requester(this, CredentialsJSON, Constants.LOGIN, 0)
        requester.queueMe()
    }
    fun Home(apiToken:String, requestResultListener: RequestResultListener){
        val Credentials = HashMap<String, String>()
        Credentials["api_token"] = apiToken

        val CredentialsJSON = JSONObject(Credentials)
        this.requestResultListener = requestResultListener
        val requester = Requester(Request.Method.GET,this, CredentialsJSON, Constants.USER_HOME, 1)
        requester.queueMe()
    }

    fun hasVisitEvents(date: Date): Boolean {
        for(location:Location in this.Locations){
            if(!location.visitEvents.isEmpty())
                return true
        }
        return false

    }
    fun fetchAsEmployee(id:Int, apiToken:String, requestResultListener: RequestResultListener){
        val Credentials = HashMap<String, String>()
        Credentials["api_token"] = apiToken
        val CredentialsJSON = JSONObject(Credentials)
        this.requestResultListener = requestResultListener
        val requester = Requester(Request.Method.GET,this, CredentialsJSON, Constants.FETCH_EMPLOYEE, 2)
        requester.queueMe()
    }

}