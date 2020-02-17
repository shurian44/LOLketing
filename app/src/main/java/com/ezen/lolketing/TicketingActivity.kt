package com.ezen.lolketing

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_ticketing.*
import org.jetbrains.anko.toast

class TicketingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticketing)

        generateQRCode(intent.getStringExtra("code"))

    }

    private fun generateQRCode(content: String) {
        var qrCodeWriter = QRCodeWriter()
        try{
            var bitmap = toBitmap(qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200))
            iv_generated_qrcode.setImageBitmap(bitmap)
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
}