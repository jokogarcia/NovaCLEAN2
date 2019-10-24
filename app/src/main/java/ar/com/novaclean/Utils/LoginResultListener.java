package ar.com.novaclean.Utils;

import com.android.volley.VolleyError;

import java.io.Serializable;

import ar.com.novaclean.Models.User;

public interface LoginResultListener {
    public void OnLoginResult(LoginResult loginResult);

    class LoginResult {
        public boolean success;
        public User user;
        public VolleyError error;
        public LoginResult(boolean success, User user){
            this.success=success;
            this.user = user;
        }
        public LoginResult(VolleyError error){
            this.success=false;
            this.user = null;
            this.error = error;
        }
        public LoginResult(){
            this.success=false;
            this.user = null;
            this.error = null;
        }
    }
}
