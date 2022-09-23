package com.example.studiest

import android.net.Uri
import java.sql.Blob

data class Anexo(
    var id: Int,
    var titulo: String?,
    var arquivo: Uri?
) {
    constructor(id: Int, titulo: String?) : this(id,titulo, null)
}
