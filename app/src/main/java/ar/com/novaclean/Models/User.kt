package ar.com.novaclean.Models

import android.util.Log
import ar.com.novaclean.Utils.LoginResultListener
import ar.com.novaclean.Utils.RequestCallbackInterface
import ar.com.novaclean.Utils.Requester
import com.android.volley.VolleyError
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import com.google.gson.Gson
import org.json.JSONException
import java.sql.Time
import java.text.ParseException
import java.text.SimpleDateFormat


/*  "data": {
    "id": 1,
    "city_id": 1438,
    "name": "Admin",
    "email": "admin@example.com",
    "email_verified_at": "2019-10-20 22:28:45",
    "api_token": "3a282b0c6e3b73bcdc2e6950bafee55e1fc5d68623abf0fa354da73fd279",
    "user_role_id": 5,
    "last_name": "Administrador",
    "dni": "30026473",
    "phone": "(509) 815-6745 x19485",
    "cuit": "20-30026473-7",
    "employee_start_date": "2012-07-02",
    "birth_date": "1972-05-03",
    "tcn_state": null,
    "photo_url": "/images/users/User1.png",
    "created_at": "2019-10-20 22:28:45",
    "updated_at": "2019-10-22 18:08:05"
  }*/
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
    var user_role_id=0
    private lateinit var loginResultListener:LoginResultListener



    override fun Callback(requestCode: Int, JSONObject: JSONObject) {
        when(requestCode){
            0->{
                try{
                    this.name = JSONObject.getString("name")
                    this.id= JSONObject.getInt("id");
                    this.city_id=JSONObject.getInt("city_id");
                    this.last_name=JSONObject.getString("last_name")
                    this.dni=JSONObject.getString("dni")
                    this.phone=JSONObject.getString("phone")
                    this.cuit=JSONObject.getString("cuit")
                    this.photo_url=JSONObject.getString("photo_url")

                    this.birth_date = SimpleDateFormat("yyyy-MM-dd").parse(JSONObject.getString("birth_date"))
                    this.employee_start_date = SimpleDateFormat("yyyy-MM-dd").parse(JSONObject.getString("birth_date"))
                    this.created_at=SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(JSONObject.getString("created_at"))
                    this.updated_at=SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(JSONObject.getString("updated_at"))

                    this.email =JSONObject.getString("email")
                    this.api_token=JSONObject.getString("api_token")
                    this.user_role_id=JSONObject.getInt("user_role_id")
                    loginResultListener.OnLoginResult(LoginResultListener.LoginResult(true,this))
                }
                catch (ex:JSONException){
                    ErrorCallback(requestCode,null)
                }catch (ex:ParseException){
                    Log.d("Login", "Parse Exception: $ex")
                    ErrorCallback(requestCode,null)
                }

            }
        }
        Log.d("USERModel", JSONObject.toString())
    }

    override fun ErrorCallback(requestCode: Int, error: VolleyError?) {
        val LoginResult = LoginResultListener.LoginResult(error)
        loginResultListener.OnLoginResult(LoginResult)
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

}