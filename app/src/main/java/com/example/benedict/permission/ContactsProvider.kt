package com.example.benedict.permission

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log

class ContactsProvider {

    companion object {
        private final val TAG = ContactsProvider::class.java.simpleName
        private final const val ContactID = ContactsContract.Contacts._ID
        private final const val MimeType = ContactsContract.Data.MIMETYPE
        private final const val DisplayName = ContactsContract.Contacts.DISPLAY_NAME
        private final const val ContactsPhoto = ContactsContract.Contacts.PHOTO_URI
        private final val ContactsContentUri = ContactsContract.Contacts.CONTENT_URI
        private final const val SortName = ContactsContract.Contacts.SORT_KEY_PRIMARY
        private final const val EmailData = ContactsContract.CommonDataKinds.Email.DATA
        private final const val PhoneType = ContactsContract.CommonDataKinds.Phone.TYPE
        private final const val EmailType = ContactsContract.CommonDataKinds.Email.TYPE
        private final const val EmailLabel = ContactsContract.CommonDataKinds.Email.LABEL
        private final const val PhoneLabel = ContactsContract.CommonDataKinds.Phone.LABEL
        private final const val PhonePhoto = ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        private final const val PhoneNumber = ContactsContract.CommonDataKinds.Phone.NUMBER
        private final const val EmailAddress = ContactsContract.CommonDataKinds.Email.ADDRESS
        private final val EmailContentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        private final val PhoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        private final const val EmailName = ContactsContract.CommonDataKinds.Email.DISPLAY_NAME
        private final const val SortId = ContactsContract.Contacts.Entity.RAW_CONTACT_ID + " ASC"
        private final const val EmailContactID = ContactsContract.CommonDataKinds.Email.CONTACT_ID
        private final const val PhoneContactID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        private final const val PhoneContentItemType = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        private final const val EmailContentItemType = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
        private final val Projection : Array<String> = arrayOf(DisplayName, PhoneNumber, PhonePhoto, MimeType, PhoneType, PhoneLabel, PhoneContactID)
    }

    private fun getPhoto(cursor : Cursor) : String? {
        return try {
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsPhoto))
        } catch (ex : Exception) { ex.printStackTrace()
            Log.e(TAG, "getPhoto() Exception : ${ex.message}")
            null
        }
    }

    private fun getPhoneLabel(context : Context, cursor : Cursor, mimeType : String, phoneType : Int) : String? {
        val typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), phoneType, "")
        Log.d(TAG,"typeLabel $typeLabel")
        return when {
            mimeType.equals(PhoneContentItemType) && typeLabel.isNotBlank() && phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM -> {
                cursor.getString(cursor.getColumnIndex(PhoneLabel))
            }
            mimeType.equals(PhoneContentItemType) && typeLabel.isNotBlank() && phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                "Mobile"
            }
            mimeType.equals(PhoneContentItemType) && typeLabel.isNotBlank() -> {
                typeLabel.toString()
            }
            else -> {
                null
            }
        }
    }

    private fun getPhoneNumber(cursor : Cursor, mimeType : String ) : String? {
        return if (mimeType.equals(PhoneContentItemType)) {
            cursor.getString(cursor.getColumnIndex(PhoneNumber))
        } else {
            null
        }
    }

    private fun getEmailLabel(context : Context, cursor : Cursor, mimeType : String, emailType : Int) : String? {
        val typeLabel = ContactsContract.CommonDataKinds.Email.getTypeLabel(context.getResources(), emailType, "")
        Log.d(TAG,"typeLabel $typeLabel")
        return when {
            mimeType.equals(EmailContentItemType) && typeLabel.isNotBlank() && emailType == ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM -> {
                cursor.getString(cursor.getColumnIndex(EmailLabel))
            }
            mimeType.equals(EmailContentItemType) && typeLabel.isNotBlank() -> {
                typeLabel.toString()
            } else -> {
                null
            }
        }

    }

    public fun getContactsList(context : Context) : List<ContactModel> {
        val contactsList : MutableList<ContactModel> = mutableListOf()
        val contentResolver : ContentResolver
        var cursor : Cursor? = null
        try {
            contentResolver = context.getContentResolver()
            cursor = contentResolver.query(ContactsContentUri, null, null, null, SortName)
            while (cursor?.moveToNext() == true) {
                val id : Long = cursor.getLong(cursor.getColumnIndex(ContactID))
                val name : String = cursor.getString(cursor.getColumnIndex(DisplayName))
                val photo : String = getPhoto(cursor)?:""
                val numbers : MutableMap<String,String> = mutableMapOf<String, String>()
                val emails : MutableMap<String,String> = mutableMapOf<String, String>()
                Log.d(TAG, "ID $id Name $name Photo $photo")
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val numberCursor : Cursor? = contentResolver.query(PhoneContentUri, null, "$PhoneContactID = ?", arrayOf(id.toString()), SortName)
                    while (numberCursor?.moveToNext() == true) {
                        val mimeType : String = numberCursor.getString(numberCursor.getColumnIndex(MimeType))
                        val phoneNumber : String? = getPhoneNumber(numberCursor, mimeType)
                        val phoneType : Int = numberCursor.getInt(numberCursor.getColumnIndex(PhoneType))
                        val phoneLabel : String? = getPhoneLabel(context, numberCursor, mimeType, phoneType)
                        Log.d(TAG, "ID $id Name $name Phone $phoneType $phoneLabel: $phoneNumber")
                        phoneNumber?.let { number ->
                            numbers.set(number.getNumbersOnly(), phoneLabel?:"Nil")
                        }
                    }
                    if (numberCursor?.moveToNext() == false) {
                        numberCursor.close()
                    }
                }
                val emailCursor : Cursor? = contentResolver.query(EmailContentUri, null, "$EmailContactID = ?", arrayOf(id.toString()), null)
                while (emailCursor?.moveToNext() == true) {
                    val mimeType : String = emailCursor.getString(emailCursor.getColumnIndex(MimeType))
                    val emailValue : String = emailCursor.getString(emailCursor.getColumnIndex(EmailData))
                    val emailType : Int = emailCursor.getInt(emailCursor.getColumnIndex(EmailType))
                    val emailLabel : String? = getEmailLabel(context, emailCursor, mimeType, emailType)
                    Log.d(TAG, "ID $id Name $name E-mail $emailType $emailLabel: $emailValue")
                    emails.set(emailValue.remove_(), emailLabel?:"Nil")
                }
                if (emailCursor?.moveToNext() == false) {
                    emailCursor.close()
                }
                contactsList.add(ContactModel(id, photo, name, numbers, emails))
            }
        } catch (ex : Exception) { ex.printStackTrace()
            Log.e(TAG, "getContactsList() Exception : ${ex.message}")
        } catch (ex : IllegalArgumentException) { ex.printStackTrace()
            Log.e(TAG, "getContactsList() IllegalArgumentException : ${ex.message}")
        } finally {
            cursor?.close()
        }
        contactsList.map {
            Log.i(TAG, "ID ${it.id} Name ${it.name} Photo ${it.photo} Numbers ${it.numbers} Emails ${it.emails}")
        }
        return when {
            contactsList.isEmpty() -> {
                emptyList()
            }
            contactsList.isNotEmpty() -> {
                contactsList
            }
            else -> {
                emptyList()
            }
        }
    }
}