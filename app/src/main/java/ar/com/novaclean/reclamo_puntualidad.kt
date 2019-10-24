package ar.com.novaclean

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ar.com.novaclean.Models.Complaint


class reclamo_puntualidad : AppCompatActivity() {
    lateinit var complaint: Complaint
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reclamo_puntualidad)
        complaint = intent.getSerializableExtra("Complaint") as Complaint


    }

    fun onClick(view: View) {
        when (view.id){
            R.id.button1->complaint.comment = "PUNTUAL"
            R.id.button2->complaint.comment = "IMPUNTUAL<10"
            R.id.button3->complaint.comment = "IMPUNTUAL>10"
            else -> return
        }
        var resultIntent = Intent()
        resultIntent.putExtra("Complaint",complaint);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }
}
