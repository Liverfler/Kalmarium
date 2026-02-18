package com.example.kalmarium.ui.theme

enum class AppThemeType(
    val primaryColor: Int,
    val displayName: String
) {

    BLUE(
        primaryColor = 0xFF2962FF.toInt(),
        displayName = "Kék"
    ),

    GREEN(
        primaryColor = 0xFF2E7D32.toInt(),
        displayName = "Zöld"
    ),

    PURPLE(
        primaryColor = 0xFF6A1B9A.toInt(),
        displayName = "Lila"
    ),

    ORANGE(
        primaryColor = 0xFFF57C00.toInt(),
        displayName = "Narancs"
    ),

    RED(
        primaryColor = 0xFFC62828.toInt(),
        displayName = "Piros"
    );

    /**
     * Visszaadja az enum típust a mentett szín alapján.
     * Ha nem talál egyezést, BLUE lesz az alapértelmezett.
     */
    companion object {
        fun fromColor(color: Int?): AppThemeType {
            return AppThemeType.entries.firstOrNull { it.primaryColor == color } ?: BLUE
        }
    }

}
