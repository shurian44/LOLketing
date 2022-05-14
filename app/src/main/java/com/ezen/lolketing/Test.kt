package com.ezen.lolketing

/**
 * firestore.collection("Game").orderBy("date")
 * firestore.collection("Game").document(documentID).collection("Seat").document("seat")
 * firestore.collection("Board").whereEqualTo("email", auth.getCurrentUser().getEmail()).whereEqualTo("team", team)
 * firestore.collection("Board").document(documentID!!).collection("Comment").orderBy("timestamp", Query.Direction.DESCENDING)
 *
 * */