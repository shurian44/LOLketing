package com.ezen.lolketing.model

data class TeamDTO(var team_name : String ?= null, var team_name_k : String ?= null,
                   var team_color : String ?= null, var foundation : String ?= null,
                   var head_coach : String ?= null, var coach : ArrayList<String> ?= null,
                   var captain : String ?= null){
    data class PlayerDTO(var name : String ?= null, var nickname : String ?= null,
                         var position : String ?= null, var img : String ?= null)
}