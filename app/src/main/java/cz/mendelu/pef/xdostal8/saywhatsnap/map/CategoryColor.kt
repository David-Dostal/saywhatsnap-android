package cz.mendelu.pef.xdostal8.saywhatsnap.map

import android.graphics.Color

enum class CategoryColor(val color: Int) {
    A(Color.BLUE),
    B(Color.GREEN),
    C(Color.rgb(255, 140, 0)), // Dark Orange
    D(Color.MAGENTA),
    E(Color.rgb(139, 0, 139)), // Dark Magenta
    F(Color.rgb(85, 107, 47)), // Dark Olive Green
    G(Color.RED),
    H(Color.rgb(0, 100, 0)), // Dark Green
    I(Color.rgb(255, 215, 0)), // Gold
    J(Color.rgb(75, 0, 130)), // Indigo
    K(Color.rgb(255, 69, 0)), // Red Orange
    L(Color.rgb(0, 191, 255)), // Deep Sky Blue
    M(Color.rgb(128, 0, 128)), // Purple
    N(Color.rgb(255, 140, 0)), // Dark Orange
    O(Color.rgb(34, 139, 34)), // Forest Green
    P(Color.rgb(255, 20, 147)), // Deep Pink
    Q(Color.rgb(0, 206, 209)), // Dark Turquoise
    R(Color.rgb(165, 42, 42)), // Brown
    S(Color.rgb(106, 90, 205)), // Slate Blue
    T(Color.rgb(0, 128, 128)), // Teal
    U(Color.rgb(255, 165, 0)), // Orange
    V(Color.rgb(199, 21, 133)), // Medium Violet Red
    W(Color.rgb(72, 61, 139)), // Dark Slate Blue
    X(Color.rgb(47, 79, 79)), // Dark Slate Gray
    Y(Color.rgb(188, 143, 143)), // Rosy Brown
    Z(Color.rgb(25, 25, 112)), // Midnight Blue
    UNKNOWN(Color.GRAY); // Default color for unknown categories

    companion object {
        fun from(letter: Char): CategoryColor {
            return values().find { it.name[0] == letter.uppercaseChar() } ?: UNKNOWN
        }
    }
}
