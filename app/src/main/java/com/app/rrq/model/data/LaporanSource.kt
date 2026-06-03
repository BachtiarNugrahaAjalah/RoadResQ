package com.app.rrq.model.data
import android.content.Context

object LaporanSource {
    fun getResourceId(context: Context, imageName: String): Int{
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}