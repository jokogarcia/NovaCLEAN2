package ar.com.novaclean.Models;

import java.util.ArrayList;

/*`id` int(11) NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `foto_url` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tareas` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL*/
class Sector {
    public int id;
    public String Nombre,FotoURL;
    public ArrayList<Tarea> Tareas;
}
