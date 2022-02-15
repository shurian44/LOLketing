package com.ezen.lolketing

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ezen.lolketing.model.PurchaseDTO
import com.ezen.lolketing.model.SeatDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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
import java.text.SimpleDateFormat
import java.util.*

class TicketingActivity : AppCompatActivity() {

    private var storage = FirebaseStorage.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    lateinit var time : String
    lateinit var team : String
    lateinit var seat : String
    var image : String ?= null
    var ticketCount = 0
    var pay = 0
    var refundPay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticketing)

        time = intent?.getStringExtra("time") ?: ""
        team = intent?.getStringExtra("team") ?: ""
        seat = intent?.getStringExtra("seat") ?: ""
        ticketCount = intent.getIntExtra("ticketCount", 0)
        pay = intent.getIntExtra("pay", 0)
        image = intent.getStringExtra("image")

        ticketing_match.text = time
        ticketing_seat.text = seat
        match.text = team

        // QR 코드 생성
        if(image == null)
            generateQRCode("${auth.currentUser?.email}_${time}_${team}_${seat.replace("/", ",")}")
        else
            Glide.with(this).load(image).into(iv_generated_qrcode)

        setRefund()

        // 환불처리
        btn_refund.setOnClickListener {
            var seats = seat.split("/")
            // 환불 금액에 맞게 유저 캐시 증가
            firestore.collection("Users").document(auth.currentUser?.email!!).update("cache", FieldValue.increment(refundPay.toDouble()))
            // 좌석 되돌리기
            for(i in seats.indices){
                firestore.collection("Game").document(time).collection("Seat").document("seat").update("seats.${seats[i]}", false)
            }
            // 구매 내역 삭제
            firestore.collection("Purchase").document("${time},${seat.replace("/", ",")}").delete()
            // QR 코드 삭제
            storage.reference.child("ticket/${auth.currentUser?.email}_${time}_${team}_${seat.replace("/", ",")}.jpg").delete()
            toast("환불 완료")
            finish()
        }
    } // onCreate()

    // QR 코드 생성
    private fun generateQRCode(content: String) {
        var qrCodeWriter = QRCodeWriter()
        try{
            var bitmap = toBitmap(qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200))
            iv_generated_qrcode.setImageBitmap(bitmap)

            var pathReference = storage.reference.child("ticket/${content}.jpg")
            var baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            var data = baos.toByteArray()
            // 구매 내역에 구매한 티켓 정보 추가
            pathReference.putBytes(data).addOnSuccessListener {
                pathReference.downloadUrl.addOnCompleteListener {
                    var purchaseDTO = PurchaseDTO()
                    purchaseDTO.amount = ticketCount
                    purchaseDTO.group = "티켓"
                    purchaseDTO.id = auth.currentUser?.email
                    purchaseDTO.image = it.result.toString()
                    purchaseDTO.name = team
                    purchaseDTO.price = pay
                    purchaseDTO.status = "usable"
                    purchaseDTO.timestamp = System.currentTimeMillis()
                    purchaseDTO.information = "${time},${seat.replace("/", ",")}"

                    firestore.collection("Purchase").document("${time},${seat.replace("/", ",")}").set(purchaseDTO)
                }
            }
        }catch (e : WriterException){
            e.printStackTrace()
        }
    } // generateQRCode()

    // QR 코드 그리기
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
    } // toBitmap()

    // 환불 상태 세팅
    private fun setRefund(){
        var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
        var date = dateFormat.parse(time)
        date.hours = date.hours - 4
        var mDate = Date()

        // 환불 상태 : 수수료 면제, 수수료 10%, 환불 불가
        // 수수료 면제 : 당일 구매의 경우 수수료 면제
        // 수수료 10% : 구매 다음 날 부터 게임 시작 4시간 이전의 경우 취소 수수료 10% 발생
        // 환불 불가 : 게임 시작 4시간 전 이후에는 환불 불가
        when {
            mDate > date -> { // 환불 불가
                btn_refund.isEnabled = false
                btn_refund.setBackgroundColor(Color.parseColor("#777777"))
                btn_refund.text = "환불 불가능"
            }
            mDate == date -> { // 수수료 면제
                refundPay = pay
            }
            else -> { // 수수료 10%
                refundPay = pay - (pay / 10)
            }
        }
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

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

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}