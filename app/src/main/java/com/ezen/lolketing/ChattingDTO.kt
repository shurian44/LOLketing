package com.ezen.lolketing

data class ChattingDTO(var users : Map<String, Boolean> = HashMap(),
                       var comments : Map<String, Comment> = HashMap()){
    data class Comment(var id : String? = null, var message : String ?= null, var timestamp : Long = 0)
}