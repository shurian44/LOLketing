package com.ezen.lolketing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.adapter.CouponAdapter
import com.ezen.lolketing.model.CouponDTO
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CouponFragment(status : String) : Fragment() {

    var status = status
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private lateinit var adapter : CouponAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_coupon, container, false)

        var query = firestore.collection("Coupon").whereEqualTo("id", auth.currentUser?.email!!).whereEqualTo("use", status)
        var options = FirestoreRecyclerOptions.Builder<CouponDTO>()
                .setQuery(query, CouponDTO::class.java).build()

        adapter = CouponAdapter(options)
        var coupon_recycler = view.findViewById<RecyclerView>(R.id.coupon_recycler)
        coupon_recycler.setHasFixedSize(true)
        coupon_recycler.adapter = adapter
        coupon_recycler.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}