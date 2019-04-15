package ar.com.novaclean;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Calificar extends AppCompatActivity {
    int calificacion=5;
    int [] stars={R.id.star1,R.id.star2,R.id.star3,R.id.star4,R.id.star5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);
        updateStars();
    }
    public void Buttons(View v){

        switch (v.getId()){
            case R.id.star1: calificacion=1; updateStars();break;
            case R.id.star2: calificacion=2; updateStars();break;
            case R.id.star3: calificacion=3; updateStars();break;
            case R.id.star4: calificacion=4; updateStars();break;
            case R.id.star5: calificacion=5; updateStars();break;
            case R.id.listo_btn:


                break;
        }
    }

    private void updateStars() {
        int i;
        for(i=0;i<calificacion;i++){
            ((ImageButton)findViewById(stars[i])).setColorFilter(Color.YELLOW);
        }
        while(i<5){
            ((ImageButton)findViewById(stars[i++])).setColorFilter(Color.GRAY);
        }

    }
}
