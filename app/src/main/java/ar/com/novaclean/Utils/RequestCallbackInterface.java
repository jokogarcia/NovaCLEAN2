package ar.com.novaclean.Utils;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RequestCallbackInterface {
    public void Callback(int requestCode, JSONObject JSONObject);
    public void ErrorCallback(int requestCode, VolleyError error);
}
