package com.devj.gestantescontrolcompose.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


suspend fun Context.getBitmap(uri: Uri): Bitmap{
    val scope = CoroutineScope(Dispatchers.Default)
   return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
       val deferred = scope.async {
           ImageDecoder.createSource(contentResolver, uri)
       }
       val source = deferred.await()
        ImageDecoder.decodeBitmap(source)
    } else {
       val deferred = scope.async {
           MediaStore.Images.Media.getBitmap(contentResolver, uri)
       }
        deferred.await()
    }
}