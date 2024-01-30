package com.example.notesnap

import android.service.quicksettings.Tile
import java.io.FileDescriptor

data class NoteItem(val title: String, val description: String, val noteId: String) {
    constructor() : this("", "", "")
}

