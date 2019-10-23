package ar.com.novaclean.Models;

public class Constants {
    public static final  String BASE_URL="https://novaclean.com.ar/api/";
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
    public static final String URL_POST_CALIFICACION = BASE_URL + "/postCalificacion.php";
}
