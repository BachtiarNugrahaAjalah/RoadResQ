package com.app.rrq.data.model
import android.content.Context

object LaporanSource {
    fun getResourceId(context: Context, imageName: String): Int{
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}