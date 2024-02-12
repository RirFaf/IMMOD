import kotlin.math.cos

fun main(args: Array<String>) {
    val dynamicProg = DynamicProgramming()
    val w = 13
    val weights = intArrayOf(3,4,5,8,9)
    val costs = intArrayOf(1,6,6,7,6)
    var coefs = ArrayList<Int>()
    println("коэффициенты:")
    for (i in costs.indices){
        coefs.add(costs[i]/weights[i])
        print("${coefs.last()} ")
    }
    println("\nмакс. вес рюкзака = $w\nвеса:")
    for (i in weights){
        print("$i ")
    }
    println("\nцены:")
    for (i in costs){
        print("$i ")
    }
    println()
    var res = dynamicProg.knapsackUnbounded(w,weights,costs)
    var finalPrice = 0
    for (i in res) {
        finalPrice += costs[i]
    }
    println("оптимальный набор ")
    for (i in res) {
        println("Вещь № ${i }, цена: ${costs[i]}")
    }
    println("финальная цена ${finalPrice*1000}")
    res = dynamicProg.knapsackUnbounded(w,weights,coefs.toIntArray())
    finalPrice = 0
    for (i in res) {
        finalPrice += costs[i]
    }
    println("оптимальный набор ")
    for (i in res) {
        println("Вещь № ${i }, коэф.: ${coefs[i]}")
    }
    println("финальная цена ${finalPrice*1000}")

}
