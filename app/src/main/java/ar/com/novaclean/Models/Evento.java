package ar.com.novaclean.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Evento implements Serializable {
    public String lugar;
    public Boolean repetible;
    public String dias;
    public Date fecha;
    public Date fecha_inicio;
    public int hora; //x60;
    public int duracion;
    public String tareas;
    public ArrayList<Tarea> Tareas;
    public String empleados;
    public ArrayList<Empleado> EmpleadosDesignados;
}
