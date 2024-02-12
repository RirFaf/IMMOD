import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    val hungarian = Hungarian()
    val nwc = NorthWestCorner()
    val minCost = MinimalCost()
    val checker = PlanChecker()

    val costs = arrayListOf(
        intArrayOf(3926, 3314, 4765),
        intArrayOf(4395, 3540, 5109),
        intArrayOf(4800, 3989, 5874)
    )
    val demand = intArrayOf(1000, 200, 600)
    val supply = intArrayOf(700, 600, 500)

    var nwcPlan = nwc.createPlan(costs, demand, supply)
    var nwcCost = 0
    var minCostPlan = minCost.createPlan(costs, demand, supply)
    var mcpCost = 0


    println("///NWC///")
    for (i in nwcPlan.indices) {
        nwcCost += costs[nwcPlan[i].second.first][nwcPlan[i].second.second] * nwcPlan[i].first
        println("${nwcPlan[i].first}, (${nwcPlan[i].second.first}, ${nwcPlan[i].second.second})")
    }
    println(if (checker.checkPlan(nwcPlan, demand, supply)) "PLan ok ${nwcCost}" else "Plan  not ok")
    println("")

    println("///Min cost///")
    for (i in minCostPlan.indices) {
        mcpCost += costs[minCostPlan[i].second.first][minCostPlan[i].second.second] * minCostPlan[i].first
        println("${minCostPlan[i].first}, (${minCostPlan[i].second.first}, ${minCostPlan[i].second.second})")
    }
    println(if (checker.checkPlan(minCostPlan, demand, supply)) "Plan ok ${mcpCost}" else "Plan not ok")
    println("")

    var resH = hungarian.solveProblem(costs, demand, supply)

    println("///Transportation prices///")
    for (row in costs) {
        println(row.joinToString())
    }
    println()


    println("///Potentials prices///")
    val potentials = Potentials()
    var f = ArrayList<Pair<Int,Int>>()
    for (i in nwcPlan){
        f.add(i.second)
    }

    val r = potentials.solveProblem(costs,f,nwcPlan)

    println("\nTotal price (potentials) $r")
    println("Total price (hungarian) $resH")
    println("NWC ${nwcCost}")
    println("Minimal cost ${mcpCost}")

}
