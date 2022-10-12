package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView

class pdfReader : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_reader)

        var pdfView = findViewById<PDFView>(R.id.pdfView)

        var it : Intent = intent
        var pdfName = it.getStringExtra("pdfName")

        pdfView.fromAsset(pdfName).load()
    }
}