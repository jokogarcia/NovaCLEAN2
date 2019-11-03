package ar.com.novaclean.Utils;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.RequestQueueSingleton;

public class Requester {
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
    public Requester(int Method, final RequestCallbackInterface client, JSONObject jsonBody, String url, final int requestCode){
        this.url =url;
        this.requestCallbackInterface = client;
        this.requestCode=requestCode;
        jsonObjectRequest = new JsonObjectRequest(Method, this.url, jsonBody,
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
    public Requester(int Method, final RequestCallbackInterface client, JSONObject jsonBody, String url, final int requestCode, final String apiToken){
        this.url =url;
        this.requestCallbackInterface = client;
        this.requestCode=requestCode;
        jsonObjectRequest = new JsonObjectRequest(Method, this.url, jsonBody,
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
                }){
            @Override
            public Map<String,String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/json");
                params.put("Accept","application/json");
                params.put("Authorization","Bearer "+apiToken);
                return params;
            }
        };
    }


    public void queueMe(){
        RequestQueueSingleton.getContextlessInstance().addToRequestQueue(jsonObjectRequest);
    }
}
