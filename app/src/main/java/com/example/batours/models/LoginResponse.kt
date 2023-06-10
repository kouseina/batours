package com.example.batours.models

data class LoginResponse(val message:String, val data: User, val token: String)