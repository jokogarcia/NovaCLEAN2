package ar.com.novaclean.Models;

import org.jetbrains.annotations.Nullable;

public class Constants {
    public static final  String BASE_URL="http://192.168.1.105:8000/api/";
    public static final  String GET= BASE_URL + "/get.php";
    public static final  String LOGIN= BASE_URL + "login";
    public static final  String GET_EVENTOS_FROM_LUGARES= BASE_URL + "/getEventosByLugares.php";

    public static final String GET_EVENTOS_FROM_USER = BASE_URL + "/getEventosByUser.php";
    public static final String GET_LUGARES_FROM_CLIENTES = BASE_URL + "/getLugaresByCliente.php";

    public static final int RQTareas = 436;
    public static final int RQTakePhoto = 437;
    public static final String URL_POST_RECLAMO = BASE_URL + "/postReclamo.php";
    public static final int RQReclamo=438;
    public static final int RQCalificar=439;
    public static final String GET_SECTORES_BY_TAREA = BASE_URL + "/getSectoresByTarea.php";
    public static final int RQReclamoPuntualidad = 440;
    public static final int RQComentario = 441;
    public static final String URL_POST_RAITING = BASE_URL + "raitings/";

    public static final String USER_HOME=BASE_URL + "home";
    public static final String FETCH_EMPLOYEE = BASE_URL + "employees/";
}
