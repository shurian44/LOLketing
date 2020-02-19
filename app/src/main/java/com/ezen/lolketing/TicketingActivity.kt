package com.ezen.lolketing

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_ticketing.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class TicketingActivity : AppCompatActivity() {

    lateinit var code : String
    private var storage = FirebaseStorage.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticketing)
        code = intent.getStringExtra("code")
        generateQRCode(code)
    }

    private fun generateQRCode(content: String) {
        var qrCodeWriter = QRCodeWriter()
        try{
            var bitmap = toBitmap(qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200))
            iv_generated_qrcode.setImageBitmap(bitmap)

            var pathReference = storage.reference.child("ticket/${code}.jpg")
            var baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            var data = baos.toByteArray()
            pathReference.putBytes(data).addOnSuccessListener {
                pathReference.downloadUrl.addOnCompleteListener {
                    //com.google.android.gms.tasks.zzu@5ab1910
                    Log.e("test", "${it.result.toString()}")
//                    var map = mapOf<String, String>("test" to it.toString())
//                    firestore.collection("Ticket").document().set(map)
                }
            }
//            var uploadTask = pathReference.putBytes(data)
//            uploadTask.addOnCompleteListener{
//                if(it.isSuccessful){
//                    val downloadUri = it.result
//                    Log.e("다운로드 uri", "$downloadUri")
//                }
//            }

        }catch (e : WriterException){
            e.printStackTrace()
        }
    }

    private fun toBitmap(matrix: BitMatrix): Bitmap {
        var width = matrix?.width ?: 0
        var height = matrix?.height ?: 0
        var bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for(x in 0 until width){
            for(y in 0 until height){
                if(matrix.get(x, y))
                    bmp.setPixel(x, y, Color.BLACK )
                else
                    bmp.setPixel(x, y, Color.WHITE )
            }
        }
        return bmp
    }

    fun logout(view: View) {}

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event?.action == KeyEvent.ACTION_DOWN){
            if(keyCode == KeyEvent.KEYCODE_BACK ){
                var intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}