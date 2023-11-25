package com.devj.gestantescontrolcompose.framework

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

class ContactManager ( private val context: Context) {
    fun getContactFromUri(uri: Uri):String? {
        try {
            var id: String? = null
            val projection = arrayOf(ContactsContract.Contacts._ID)
            val cursor = context
                .contentResolver
                .query(uri, projection, null, null, null)
            cursor?.let {
                if (cursor.moveToFirst()) {
                    id = cursor.getString(0)
                    cursor.close()
                }else{
                    return null
                }
            }
            if(id != null){
                var phone: String? = null
                val contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                val columnsProjection =
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val filterSelection =
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + "= " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                val cursorPhone = context.contentResolver
                    .query(contentUri,columnsProjection,filterSelection,arrayOf(id), null)

               cursorPhone?.let {
                   if (cursorPhone.moveToFirst()) {
                       phone = cursorPhone.getString(0)
                       cursorPhone.close()
                   }
               }
                return phone
            }

            return null
        } catch (e: Exception) {
            return null
        }
    }
}