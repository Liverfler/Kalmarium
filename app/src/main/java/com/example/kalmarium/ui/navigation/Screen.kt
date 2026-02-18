package com.example.kalmarium.ui.navigation

import com.example.kalmarium.data.VasarEntity

sealed class Screen {

    object Main : Screen()

    object Termekek : Screen()

    object UjVasar : Screen()

    data class TetelesEladas(val vasar: VasarEntity) : Screen()


    data class EladasTorles(val vasar: VasarEntity) : Screen()

    object KategoriaSzerkeszto : Screen()

    data class VasarReszletek(val vasar: VasarEntity) : Screen()

    object CsoportosTermek : Screen()

    object VasarSzerkesztes : Screen()

    data class PdfExport(val vasar: VasarEntity) : Screen()


}
