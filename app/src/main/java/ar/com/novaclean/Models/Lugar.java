package ar.com.novaclean.Models;

import java.util.ArrayList;

/*`id` int(11) NOT NULL,
  `creado` timestamp NOT NULL DEFAULT current_timestamp(),
  `modificado` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `domicilio` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `contacto_local` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrato_numero` int(11) NOT NULL,
  `foto_url` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sectores` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;*/
public class Lugar {
    public int id;
    public String Nombre;
    public String Domicilio;
    public String Telefono;
    public String Email;
    public String ContactoLocal;
    public String FotoURL;
    public ArrayList<Sector> Sectores;
    public double Latitud;
    public double Longitud;
}