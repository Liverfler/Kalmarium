package com.example.kalmarium.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.data.TermekEntity
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build

object PdfExportManager {

    fun exportVasarToPdf(
        context: Context,
        vasarNev: String,
        datum: String,
        hely: String,
        bevetel: Int,
        koltseg: Int,
        eladasLista: List<EladasEntity>,
        termekLista: List<TermekEntity>
    ): Uri? {

        val pdfDocument = PdfDocument()
        val paint = Paint()
        paint.textSize = 14f

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40f

        canvas.drawText("Vásár: $vasarNev", 40f, y, paint); y += 20
        canvas.drawText("Dátum: $datum", 40f, y, paint); y += 20
        canvas.drawText("Helyszín: $hely", 40f, y, paint); y += 30
        canvas.drawText("Bevétel: $bevetel Ft", 40f, y, paint); y += 20
        canvas.drawText("Költség: $koltseg Ft", 40f, y, paint); y += 20
        canvas.drawText("Profit: ${bevetel - koltseg} Ft", 40f, y, paint); y += 40
        canvas.drawText("Eladások:", 40f, y, paint); y += 20

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        eladasLista.sortedBy { it.timestamp }.forEach {
            val ido = dateFormat.format(Date(it.timestamp))
            val ar = termekLista.firstOrNull { t -> t.nev == it.termekNev }?.ar ?: 0
            canvas.drawText("$ido - ${it.termekNev} - $ar Ft", 40f, y, paint)
            y += 20
        }

        pdfDocument.finishPage(page)

        val fileName = "Vasar_${vasarNev}_${System.currentTimeMillis()}.pdf"

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)

        uri?.let {
            resolver.openOutputStream(it)?.use { stream ->
                pdfDocument.writeTo(stream)
            }
        }

        pdfDocument.close()

        return uri
    }

}
