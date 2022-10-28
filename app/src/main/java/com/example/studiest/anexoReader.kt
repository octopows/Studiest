package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.github.barteksc.pdfviewer.PDFView

class anexoReader : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anexo_reader)


        var pdfViewer = findViewById<PDFView>(R.id.pdfViewer)

        var it : Intent = intent
        var pdf = it.getStringExtra("pdfURI")
        var pdfURI = pdf!!.toUri()
        pdfViewer.fromUri(pdfURI).load()
    }
}