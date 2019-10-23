package ar.com.novaclean.Models;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Utils.LoginResultListener;
import ar.com.novaclean.Utils.RequestCallbackInterface;
import ar.com.novaclean.Utils.Requester;


public class Usuario implements Serializable, RequestCallbackInterface {
    public int id;
    public String nombre;
    public String apellido;
    public boolean isEmpleado;
    public String email;
    public String token;
    public String error;
    private LoginResultListener loginResultListener;
    private boolean isLoggedIn;
    public Map<String,String> getLoginParams(){
        Map<String,String> params = new HashMap<String, String>();
        params.put("client_id",String.valueOf(this.id));
        params.put("tok",this.token);
        return params;
    }
    public void RequestLogin(String password, LoginResultListener loginResultListener){
        HashMap<String,String> Credentials = new HashMap<String,String>();
        Credentials.put("email",this.email);
        Credentials.put("password",password);
        JSONObject CredentialsJSON=new JSONObject(Credentials);
        this.loginResultListener=loginResultListener;

        Requester requester = new Requester(this,CredentialsJSON,Constants.LOGIN, 0);
        requester.queueMe();
    }
    @Override
    public void Callback(int requestCode, JSONObject JSONObject) {
        Log.d("USERModel",JSONObject.toString());
    }

    @Override
    public void ErrorCallback(int requestCode,VolleyError error) {
        LoginResultListener.LoginResult LoginResult = new LoginResultListener.LoginResult(error);
        loginResultListener.OnLoginResult(LoginResult);
    }

}
