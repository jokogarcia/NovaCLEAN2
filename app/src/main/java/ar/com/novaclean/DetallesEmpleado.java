package ar.com.novaclean;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ar.com.novaclean.Models.Empleado;
import ar.com.novaclean.Utils.DownloadImageTask;

public class DetallesEmpleado extends AppCompatActivity {
    Empleado CurrentEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_empleado);
        CurrentEmpleado = (Empleado) getIntent().getSerializableExtra("Empleado");
        //Toast.makeText(this,CurrentEmpleado.nombre,Toast.LENGTH_LONG).show();
        ((TextView)findViewById(R.id.nombretv)).setText(CurrentEmpleado.apellido + ", "+ CurrentEmpleado.nombre);
        ((TextView)findViewById(R.id.puesto_areaTV)).setText(CurrentEmpleado.puesto + " en sector "+CurrentEmpleado.area);
        new DownloadImageTask((ImageView) findViewById(R.id.empleadoPhotoIV))
                .execute("https://novaclean.com.ar/_privado/imagenes/empleados/"+CurrentEmpleado.foto_url);

    }
}
