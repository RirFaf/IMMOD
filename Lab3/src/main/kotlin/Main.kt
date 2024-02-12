import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val h = Homori()

    val matrix = arrayListOf(
        arrayListOf(2, 0),
        arrayListOf(2, 0),
        arrayListOf(1, 2),
        arrayListOf(2, -1)
    )
    val signs = arrayListOf(
        ">=",
        "<=",
        "<=",
        "<="
    )
    val b = arrayListOf(4, 6, 7, 5)
    val z = arrayListOf(0, -3)
    h.homoriMethod(
        matrix, signs, b, z
    )
}

