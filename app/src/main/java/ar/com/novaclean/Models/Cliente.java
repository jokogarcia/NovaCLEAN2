package ar.com.novaclean.Models;
/*`id` int(11) NOT NULL,
  `creado` timestamp NOT NULL DEFAULT current_timestamp(),
  `modificado` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cuit` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `domicilio` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL*/
public class Cliente {
    public int id;
    public String nombre;
    public String apellido;
    public String cuit;
    public String telefono;
    public String email;
    public String domicilio;

}
