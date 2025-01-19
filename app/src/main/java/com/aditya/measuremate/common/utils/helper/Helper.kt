package com.example.udemycourseshoppingapp.common.utils.helper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import java.text.SimpleDateFormat
import java.util.Locale

object Helper {
    private var toast : Toast? = null

    fun showToast(context : Context, message : String, duration : Int = Toast.LENGTH_SHORT){
        toast?.cancel()
        toast  = Toast.makeText(context, message, duration)
        toast?.show()
    }

    fun formatDate(timeStamp : Long) : String{
        return SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).format(timeStamp*1000)
    }

    fun openLink(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun isAllPermissionGranted(context : Context , vararg permissions : String) : Boolean{
       return  permissions.all {
            ContextCompat.checkSelfPermission(context , it) == PackageManager.PERMISSION_GRANTED
        }
    }
}