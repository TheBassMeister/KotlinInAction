package com.bassmeister.burgercloud.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User(val password:String, val firstName:String, val lastName:String, val street:String, val city:String,
           val state:String, val zip:String, val phoneNumber:String)
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long=0L

}
