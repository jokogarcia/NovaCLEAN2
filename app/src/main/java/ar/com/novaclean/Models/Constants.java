package ar.com.novaclean.Models;

import org.jetbrains.annotations.Nullable;

public class Constants {
    public static final  String BASE_URL="https://novaclean.com.ar/api/";
    //public static final  String BASE_URL="http://192.168.1.105:8000/api/";
    public static final  String LOGIN= BASE_URL + "login";


    public static final int RQTareas = 436;
    public static final int RQTakePhoto = 437;
    public static final int RQReclamo=438;
    public static final int RQCalificar=439;
    public static final int RQReclamoPuntualidad = 440;
    public static final int RQComentario = 441;
    public static final String URL_POST_RAITING = BASE_URL + "raitings/";

    public static final String USER_HOME=BASE_URL + "home";
    public static final String FETCH_EMPLOYEE = BASE_URL + "employees/";
}
