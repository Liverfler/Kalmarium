package com.example.kalmarium.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.example.kalmarium.data.EladasEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.kalmarium.data.TermekStat


object PdfExportManager {

    fun exportVasarToPdf(
        context: Context,
        vasarNev: String,
        datum: String,
        hely: String,
        bevetel: Int,
        koltseg: Int,
        eladasLista: List<EladasEntity>
    )
            : Uri? {

        val pdfDocument = PdfDocument()
        val paint = Paint().apply {
            textSize = 14f
        }

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

        eladasLista
            .sortedBy { it.timestamp }
            .forEach { eladas ->

                val ido = dateFormat.format(Date(eladas.timestamp))
                val ar = eladas.eladasiAr
                val osszeg = ar * eladas.mennyiseg

                canvas.drawText(
                    "$ido - ${eladas.termekNev} - ${eladas.mennyiseg} db - $osszeg Ft",
                    40f,
                    y,
                    paint
                )
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

    fun exportSimpleListPdf(
        context: Context,
        title: String,
        lines: List<String>
    ): Uri? {

        val pdfDocument = PdfDocument()
        val paint = Paint().apply {
            textSize = 14f
        }

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40f

        canvas.drawText(title, 40f, y, paint)
        y += 30

        lines.forEach { line ->
            canvas.drawText(line, 40f, y, paint)
            y += 20
        }

        pdfDocument.finishPage(page)

        val fileName = "${title.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"

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

    fun exportTermekStatisztikaPdf(
        context: Context,
        statLista: List<TermekStat>
    ): Uri? {

        val pdfDocument = PdfDocument()
        val paint = Paint().apply { textSize = 14f }

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40f

        canvas.drawText("Globális termék statisztika", 40f, y, paint)
        y += 40

        if (statLista.isEmpty()) {
            canvas.drawText("Nincs adat.", 40f, y, paint)
        } else {

            statLista.sortedByDescending { it.osszesBevetel }
                .forEach { stat ->

                    canvas.drawText(
                        "${stat.termekNev} – ${stat.osszesDarab} db – ${stat.osszesBevetel} Ft",
                        40f,
                        y,
                        paint
                    )
                    y += 20
                }
        }

        pdfDocument.finishPage(page)

        val fileName = "GlobalisTermekStatisztika_${System.currentTimeMillis()}.pdf"

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



    fun exportMultHonapPdf(
        context: Context,
        eladasLista: List<EladasEntity>,
        vasarNevMap: Map<Int, String>
    ): Uri?
    {

        val pdfDocument = PdfDocument()
        val paint = Paint().apply { textSize = 14f }

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40f

        canvas.drawText("Eladások – múlt hónap", 40f, y, paint)
        y += 40

        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())

        eladasLista.forEach { eladas ->
            val ido = dateFormat.format(Date(eladas.timestamp))
            val osszeg = eladas.eladasiAr * eladas.mennyiseg

            canvas.drawText(
                "${vasarNevMap[eladas.vasarId] ?: "Ismeretlen vásár"} – $ido – ${eladas.termekNev} – $osszeg Ft",
                40f,
                y,
                paint
            )
            y += 20
        }

        pdfDocument.finishPage(page)

        val fileName = "MultHonap_${System.currentTimeMillis()}.pdf"

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