package com.example.kalmarium.data

data class AppBackup(
    val vasarok: List<VasarEntity>,
    val termekek: List<TermekEntity>,
    val eladasok: List<EladasEntity>
)
