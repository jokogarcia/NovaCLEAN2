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




class User: _user(),RequestCallbackInterface{





    val Locations = mutableListOf<Location>()
    private lateinit var loginResultListener:LoginResultListener
    private lateinit var requestResultListener: RequestResultListener

    override fun fromJson(data:JSONObject):Boolean{
        super.fromJson(data)
        try{

            if(data.has("locations")){
                Locations.clear()
                val jarray = data.getJSONArray("locations")

                for (i in 0 until jarray.length()) {
                    val item = jarray.getJSONObject(i)
                    val location = Location()
                    location.fromJson(item)
                    Locations.add(location)
                }

            }



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

    override fun Callback(requestCode: Int, JSONObject: JSONObject) {
        when(requestCode){
            0->{
                try{
                    val data = JSONObject.getJSONObject("data")
                    if(!this.fromJson(data))
                        this.ErrorCallback(requestCode,null)
                    else
                        loginResultListener.OnLoginResult(LoginResultListener.LoginResult(true,this))
                } catch (ex:JSONException){
                    Log.d("Login", "JSON Exception in User Login: $ex")
                    this.ErrorCallback(requestCode,null)
                }

            }
            1->{
                if(!this.fromJson(JSONObject))
                    requestResultListener.OnRequestResult(RequestResult(null,false))
                requestResultListener.OnRequestResult(RequestResult(null,true))
            }
            2->{
                if(!this.fromJson(JSONObject)){
                    requestResultListener.OnRequestResult(RequestResult(null,false))
                }
                requestResultListener.OnRequestResult(RequestResult(null,true))
            }
        }
    }
    override fun Callback(requestCode: Int, string: String?) {
        val jo = JSONObject(string);
        Callback(requestCode,jo);
    }

    override fun ErrorCallback(requestCode: Int, error: VolleyError?) {
        when(requestCode){
            0->{
                val LoginResult = LoginResultListener.LoginResult(error)
                loginResultListener.OnLoginResult(LoginResult)
            }
            1->{
                Log.d("HOME","Volley error: $error")
                requestResultListener.OnRequestResult(RequestResult(error,false))
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
        if(true)
        {

            this.requestResultListener = requestResultListener
            //val requester = Requester(Request.Method.GET,this, CredentialsJSON, Constants.USER_HOME, 1)
            val requester = Requester(Request.Method.GET,this, CredentialsJSON, Constants.USER_HOME, 1,apiToken)
            requester.queueMe()
        }
        else{
            val stringRequest = object: StringRequest(Request.Method.GET,Constants.USER_HOME,
                  object: Response.Listener<String>{
                      override fun onResponse(response: String?) {
                          Log.d("Response",response)
                      }

                  },
                    object : Response.ErrorListener{
                        override fun onErrorResponse(error: VolleyError?) {
                            Log.d("Error",error.toString())
                        }
                    }){
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Content-Type", "application/json")
                    headers.put("Accept","application/json")
                    headers.put("Authorization","Bearer $apiToken")

                    return headers
                }


            }


            RequestQueueSingleton.getContextlessInstance().addToRequestQueue<String>(stringRequest)

        }


    }

    fun hasVisitEvents(date: Date): Boolean {
        for(location:Location in this.Locations){
            if(location.visitEvents!= null && !location.visitEvents.isEmpty())
                for(visitEvent in location.visitEvents){
                    if(visitEvent.isOnDate(date))
                        return true;
                }
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