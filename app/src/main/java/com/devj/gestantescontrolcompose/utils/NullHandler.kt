package com.devj.gestantescontrolcompose.utils

fun String.ifEmptyReturnNull(): String? {
    return if (this != "") {
        this
    } else {
        null
    }
}
fun String.ifNullReturnEmpty(): String{
    return if (this == "null"){
        ""
    }else{
        this
    }
}