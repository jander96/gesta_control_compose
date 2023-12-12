package com.devj.gestantescontrolcompose.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun Bitmap.convertToString(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)

}

fun String.convertToBitmap(): Bitmap {
    val byteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun View.hideKeyboard(){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken,0)
}

fun View.setGoneView(isGone: Boolean){
    if(!isGone){
        this.visibility = View.VISIBLE
    }else{
        this.visibility = View.GONE
    }
}
fun View.setViewVisibility(isVisible: Boolean){
    if(isVisible){
        this.visibility = View.VISIBLE
    }else{
        this.visibility = View.INVISIBLE
    }
}

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
fun Int?.ifNullReturnEmpty(): String{
    return if (this == null){
        ""
    }else{
        "$this"
    }
}

fun Context.createInternalFileFromImageUri(uri: Uri): Boolean {

    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

    val isFileSuccess =
        openFileOutput("$uri.jpeg", Context.MODE_PRIVATE).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, it)
        }
    return isFileSuccess
}
fun Context.getBitmapFromFile(photoFileName:String):Bitmap{
    val bytes = openFileInput(photoFileName).use { fileInputStream ->
        fileInputStream.readBytes()
    }
    return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
}


suspend fun Bitmap.compressQuality(quality: Int): Bitmap {
    val scope = CoroutineScope(Dispatchers.Default)
    val byte = ByteArrayOutputStream()
    val deferred =  scope.async {
        compress(Bitmap.CompressFormat.PNG, quality, byte)
        BitmapFactory.decodeStream(ByteArrayInputStream(byte.toByteArray()))
    }
    return deferred.await()
}

fun Bitmap.getResizeBitmap(with: Int, heigth: Int, filter: Boolean): Bitmap {
    return Bitmap.createScaledBitmap(this, with, heigth, filter)
}

fun getDate(year: Int, month: Int, day: Int): String = "$day/${month+1}/$year"
//****

fun Double.getIMClassification(): String{
    if(this !in 0.0..100.0) return ""
    return when(this){
        in 0.0 ..18.0 -> "deficiente"
        in 18.0..24.9 -> "normopeso"
        in 25.0..29.9 -> "sobrepeso"
        in 30.0..34.9 -> "obesidad I"
        in 35.0..39.9 -> "obesidad II"
        else ->"obesidad III"
    }
}