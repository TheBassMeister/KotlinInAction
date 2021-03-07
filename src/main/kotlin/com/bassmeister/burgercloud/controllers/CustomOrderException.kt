package com.bassmeister.burgercloud.controllers

import java.lang.RuntimeException


const val CANNOT_CREATE_NEW_CUSTOMER="Cannot create orders for unknown customer"

class CustomOrderException(override val message:String) : RuntimeException(message){

}