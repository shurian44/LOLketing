package com.ezen.lolketing.model

data class ChattingDTO(var chattingTitle : String ?= null,
                       var users : Map<String, String> = HashMap(),
                       var comments : Map<String, Comment> = HashMap()){
    data class Comment(var id : String? = null, var message : String ?= null, var timestamp : Long = 0, var team : String ?= null)
}