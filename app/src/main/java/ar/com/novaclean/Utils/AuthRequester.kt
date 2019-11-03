package ar.com.novaclean.Utils

import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import java.util.HashMap

class AuthRequester{
    lateinit var apiToken: String
    var url:String=""
    lateinit var requestCallbackInterface: RequestCallbackInterface
    lateinit var stringRequest:StringRequest
    var requestCode:Int =0

    fun AuthRequester(url:String,apiToken:String,requestCallbackInterface: RequestCallbackInterface, requestCode:Int){
        this.requestCallbackInterface=requestCallbackInterface;
        this.url=url
        this.apiToken=apiToken
        this.requestCode = requestCode
        stringRequest=object :StringRequest(Method.GET,url,
                object: Response.Listener<String>{
                    override fun onResponse(response: String?) {
                        requestCallbackInterface.Callback(requestCode,response)
                    }

                },
                object: Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        requestCallbackInterface.ErrorCallback(requestCode,error)
                    }

                }){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                headers.put("Accept", "application/json")
                headers.put("Authorization", "Bearer $apiToken")

                return headers
            }
        }
    }
}