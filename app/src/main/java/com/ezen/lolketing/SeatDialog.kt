package com.ezen.lolketing

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import com.ezen.lolketing.model.SeatDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SeatDialog(context : Context, listener : onSelectSeatListener, documentID : String, count : Int) : Dialog(context), CompoundButton.OnCheckedChangeListener {
    private var listener = listener
    private var firestore = FirebaseFirestore.getInstance()
    private var documentID = documentID
    lateinit var dialogView : View
    var count = count   // 인원 수
    var selectCount = 0     // 선택한 체크박스 수
    var checkBoxes = ArrayList<CheckBox>()
    var ck_id = ArrayList<Int>()
    var ck_map = mapOf<Int, String>(R.id.checkA_1a to "A_1a", R.id.checkA_1b to "A_1b", R.id.checkA_1c to "A_1c", R.id.checkA_1d to "A_1d",
            R.id.checkA_1e to "A_1e", R.id.checkA_1f to "A_1f", R.id.checkA_1g to "A_1g", R.id.checkA_1h to "A_1h",
            R.id.checkA_2a to "A_2a", R.id.checkA_2b to "A_2b", R.id.checkA_2c to "A_2c", R.id.checkA_2d to "A_2d",
            R.id.checkA_2e to "A_2e", R.id.checkA_2f to "A_2f", R.id.checkA_2g to "A_2g", R.id.checkA_2h to "A_2h",
            R.id.checkA_3a to "A_3a", R.id.checkA_3b to "A_3b", R.id.checkA_3c to "A_3c", R.id.checkA_3d to "A_3d",
            R.id.checkA_3e to "A_3e", R.id.checkA_3f to "A_3f", R.id.checkA_3g to "A_3g", R.id.checkA_3h to "A_3h",
            R.id.checkA_4a to "A_4a", R.id.checkA_4b to "A_4b", R.id.checkA_4c to "A_4c", R.id.checkA_4d to "A_4d",
            R.id.checkA_4e to "A_4e", R.id.checkA_4f to "A_4f", R.id.checkA_4g to "A_4g", R.id.checkA_4h to "A_4h",
            R.id.checkA_5a to "A_5a", R.id.checkA_5b to "A_5b", R.id.checkA_5c to "A_5c", R.id.checkA_5d to "A_5d",
            R.id.checkA_5e to "A_5e", R.id.checkA_5f to "A_5f", R.id.checkA_5g to "A_5g", R.id.checkA_5h to "A_5h",
            R.id.checkA_6a to "A_6a", R.id.checkA_6b to "A_6b", R.id.checkA_6c to "A_6c", R.id.checkA_6d to "A_6d",
            R.id.checkA_6e to "A_6e", R.id.checkA_6f to "A_6f", R.id.checkA_6g to "A_6g", R.id.checkA_6h to "A_6h",
            R.id.checkA_7a to "A_7a", R.id.checkA_7b to "A_7b", R.id.checkA_7c to "A_7c", R.id.checkA_7d to "A_7d",
            R.id.checkA_7e to "A_7e", R.id.checkA_7f to "A_7f", R.id.checkA_7g to "A_7g", R.id.checkA_7h to "A_7h",
            R.id.checkA_8a to "A_8a", R.id.checkA_8b to "A_8b", R.id.checkA_8c to "A_8c", R.id.checkA_8d to "A_8d",
            R.id.checkA_8e to "A_8e", R.id.checkA_8f to "A_8f", R.id.checkA_8g to "A_8g", R.id.checkA_8h to "A_8h")
    var selectSeat = ""

    fun createDialog(){
        // 커스텀 다이얼로그 생성
        val dialogBuilder = AlertDialog.Builder(context)
        dialogView = layoutInflater.inflate(R.layout.dialog_seat, null)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.show()

        // 체크박스 불러오기
        loadCheckBox()

        dialogView.findViewById<Button>(R.id.reserve_complete).setOnClickListener {
            // 인원수와 체크박스 선택의 수가 동일한 경우
            if(selectCount == count){
                // 마지막 '/'제거
                selectSeat = selectSeat.substring(0, selectSeat.length-1)
                // 선택한 좌석을 액티비티로 전달
                listener.selectSeat(selectSeat)
                dialog.dismiss()
            }else{ // 인원수와 체크박스 선택의 수가 동일하지 않은 경우
                Toast.makeText(context, "좌석을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        } // setOnClickListener
    } // createDialog()

    // 체크박스 불러오기
    private fun loadCheckBox(){
        // map 의 키들을 리스트에 저장
        ck_map.keys.forEach {
            ck_id.add(it)
        }
        // 체크박스 찾기
        for(i in ck_id.indices){
            checkBoxes.add(dialogView.findViewById(ck_id[i]))
            checkBoxes[i].setOnCheckedChangeListener(this)  // 각 체크박스에 리스너 등록
        }
        // 데이터베이스에서 이미 선택된 좌석 불러오기
        setCheckBox()
    } // loadCheckBox()

    // 데이터베이스에서 이미 선택된 좌석 불러오기
    private fun setCheckBox(){
        firestore.collection("Game").document(documentID).collection("Seat").document("seat").get().addOnCompleteListener {
            var seats = it.result?.toObject(SeatDTO::class.java)
            // 이미 선택한 좌석의 경우 enabled 처리
            for(i in ck_id.indices){
                if(seats?.seats?.get(ck_map[ck_id[i]]) == true){
                    checkBoxes[i].isEnabled = false
                }
            }
        }
    } // setCheckBox()

    // 체크 박스 상태 변화 리스너
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        // 체크박스를 체크했을 경우
        if(isChecked){
            selectCount++
            // 선택한 좌석이 인원 수 보다 많을 경우
            if(selectCount > count){
                selectCount--
                buttonView?.isChecked = false
                Toast.makeText(context, "선택할 수 있는 좌석수를 초과하였습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            // 선택한 내용을 String 에 추가
            selectSeat += "${ck_map.getValue(buttonView!!.id)}/"
        }else{ // 체크박스를 체크 해제했을 경우
            selectCount--
            // 취소한 내용을 String 에서 제거
            selectSeat = selectSeat.replace("${ck_map.getValue(buttonView!!.id)}/", "")
        }
    } // onCheckedChanged()

    // 선택한 좌석을 액티비티로 전달
    interface onSelectSeatListener{
        fun selectSeat(seat : String)
    }
}