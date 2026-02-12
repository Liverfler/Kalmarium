package com.example.kalmarium.ui.navigation

import com.example.kalmarium.data.VasarEntity

sealed class Screen {

    object Main : Screen()

    object Termekek : Screen()

    object UjVasar : Screen()

    object TetelesEladas : Screen()

    object EladasTorles : Screen()

    object KategoriaSzerkeszto : Screen()

    data class VasarReszletek(val vasar: VasarEntity) : Screen()
}
