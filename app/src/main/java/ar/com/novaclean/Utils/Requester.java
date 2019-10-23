package ar.com.novaclean.Utils;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import ar.com.novaclean.RequestQueueSingleton;

public class Requester {
    JSONObject jsonBody;
    String url;
    RequestCallbackInterface requestCallbackInterface;
    JsonObjectRequest jsonObjectRequest;
    int requestCode;


    public Requester( final RequestCallbackInterface client, JSONObject jsonBody, String url, final int requestCode){
        this.url =url;
        this.requestCallbackInterface = client;
        this.requestCode=requestCode;
        jsonObjectRequest = new JsonObjectRequest(this.url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        client.Callback(requestCode, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        client.ErrorCallback(requestCode, error);
                    }
                });
    }


    public void queueMe(){
        RequestQueueSingleton.getContextlessInstance().addToRequestQueue(jsonObjectRequest);
    }
}
