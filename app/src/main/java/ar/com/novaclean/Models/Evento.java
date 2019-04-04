package ar.com.novaclean.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Evento implements Serializable {
    public String Lugar;
    public Boolean Repetible;
    public String Dias;
    public Date Fecha;
    public Date Fecha_inicio;
    public int Hora; //x60;
    public int Duracion;
    public ArrayList<Tarea> Tareas;
    public ArrayList<Empleado> EmpleadosDesignados;
}
