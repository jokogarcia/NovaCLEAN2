package ar.com.novaclean

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import ar.com.novaclean.Models.Constants
import ar.com.novaclean.Models.Complaint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ReclamoComentarioYfoto : AppCompatActivity() {

    lateinit var complaint: Complaint
    private var currentPhotoPath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reclamo_comentario_yfoto)
        complaint = intent.getSerializableExtra("Complaint") as Complaint

    }

    fun onClick(view: View) {
        when(view.id){
            R.id.buttonPhoto-> dispatchTakePictureIntent()
            R.id.buttonSend->{
                val ET = findViewById<EditText>(R.id.ETReclamoComentario)
                complaint.comment = ET.text.toString()
                val ResultIntent = Intent()
                ResultIntent.putExtra("Complaint",complaint)
                ResultIntent.putExtra("CurrentPhotoPath",currentPhotoPath)
                setResult(Activity.RESULT_OK, ResultIntent)
                finish()
            }
        }
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Log.d("JOKOGARCIA", "Error occured while creating the file.$ex")
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this,
                        "ar.com.novaclean.android.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, Constants.RQTakePhoto)
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.RQTakePhoto){
            if(resultCode == Activity.RESULT_OK){
                var PhotoView = findViewById<ImageView>(R.id.PhotoView);
                // Get the dimensions of the View
                val targetW = PhotoView.getWidth()
                val targetH = PhotoView.getHeight()

                // Get the dimensions of the bitmap
                val bmOptions = BitmapFactory.Options()
                bmOptions.inJustDecodeBounds = true
                BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
                val photoW = bmOptions.outWidth
                val photoH = bmOptions.outHeight

                // Determine how much to scale down the image
                val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false
                bmOptions.inSampleSize = scaleFactor
                bmOptions.inPurgeable = true

                //scale the image and put it in the view
                val bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
                PhotoView.setImageBitmap(bitmap)

                //Save bitmap to a temporary file
                try {
                    val out = FileOutputStream(currentPhotoPath)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out) // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }
}
