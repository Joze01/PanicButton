package com.applaudostudio.panicbutton.model

data class ContactDBModel(val COLUMN_NAME:String, val COLUMN_PHONE: String) {
    companion object {
        val COLUMN_ID="ID"
        val TABLENAME="contact"
        val COLUMN_NAME="name"
        val COLUMN_PHONE="phone"
    }



}