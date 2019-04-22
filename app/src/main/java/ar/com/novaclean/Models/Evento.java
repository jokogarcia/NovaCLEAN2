package ar.com.novaclean.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.com.novaclean.Calendario;
import ar.com.novaclean.MySingleton;

import static android.content.Context.MODE_PRIVATE;

public class Evento implements Serializable {
    public int id;
    public String lugar;
    public int repetible;
    public String dias;
    public Date fecha;
    public Date fecha_inicio;
    public int hora; //x60;
    public int duracion;
    public String tareas_ids;
    public ArrayList<Tarea> Tareas;
    public String empleados_ids;
    public int lugar_id;
    public ArrayList<Empleado> EmpleadosDesignados;


}
