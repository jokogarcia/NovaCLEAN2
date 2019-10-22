package ar.com.novaclean.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*`id` int(11) NOT NULL,
cue cue cue
  `creado` timestamp NOT NULL DEFAULT current_timestamp(),
  `modificado` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `nombre` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cuit` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `domicilio` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL*/
public class Usuario implements Serializable {
    public int id;
    public String nombre;
    public String apellido;
    public boolean isEmpleado;
    public String email;
    public String token;
    public String error;
    public Map<String,String> getLoginParams(){
        Map<String,String> params = new HashMap<String, String>();
        params.put("client_id",String.valueOf(this.id));
        params.put("tok",this.token);
        return params;
    }

}
