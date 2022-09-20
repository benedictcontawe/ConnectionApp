package com.example.connectionapp

enum class NFCStatus {
    NoOperation,
    Tap,
    Process,
    Confirmation,
    Read,
    Write,
    NotSupported,
    NotEnabled,
}