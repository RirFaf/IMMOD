class MinimalCost() {
    fun createPlan(
        costs: ArrayList<IntArray>,
        demand: IntArray,
        supply: IntArray
    ): ArrayList<Pair<Int, Pair<Int, Int>>> {
        var res = ArrayList<Pair<Int, Pair<Int, Int>>>()
        var tempDemand = demand.clone()
        var tempSupply = supply.clone()
        var f = ArrayList<Pair<Int, Pair<Int, Int>>>()
        for (i in costs.indices) {
            for (j in costs[i].indices) {
                f.add(costs[i][j] to (i to j))
            }
        }
        val priorities = f.sortedBy { it.first }


        for (i in priorities.indices) {
            if (tempDemand[priorities[i].second.first] <= tempSupply[priorities[i].second.second]) {
                if (tempDemand[priorities[i].second.first] > 0) {
                    res.add(tempDemand[priorities[i].second.first] to priorities[i].second)
                }
                tempSupply[priorities[i].second.second] -= tempDemand[priorities[i].second.first]
                tempDemand[priorities[i].second.first] = 0

            } else {
                if (tempSupply[priorities[i].second.second] > 0) {
                    res.add(tempSupply[priorities[i].second.second] to priorities[i].second)
                }
                tempDemand[priorities[i].second.first] -= tempSupply[priorities[i].second.second]
                tempSupply[priorities[i].second.second] = 0

            }
        }
        return res
    }
}