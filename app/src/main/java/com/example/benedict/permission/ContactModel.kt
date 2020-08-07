package com.example.benedict.permission

data class ContactModel (

    val id : Long,
    val name : String,
    val photo : String,
    val numbers : MutableMap<String,String>,
    val emails : MutableMap<String,String>
)