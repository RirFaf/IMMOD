class NorthWestCorner() {
    //Works OK
    fun createPlan(
        costs: ArrayList<IntArray>,
        demand: IntArray,
        supply: IntArray
    ): ArrayList<Pair<Int, Pair<Int, Int>>> {
        var tempDemand = demand.clone()
        var tempSupply = supply.clone()

        var res = ArrayList<Pair<Int, Pair<Int, Int>>>()

        var i = 0
        var j = 0
        while (i < costs.size - 1) {
            while (j <= costs[i].size - 1) {
                if (tempDemand[i] != 0 && tempSupply[j] != 0) {
                    if (tempDemand[i] < tempSupply[j]) {
                        res.add(tempDemand[i] to (i to j))
                        tempSupply[j] -= tempDemand[i]
                        tempDemand[i] = 0
                    } else {
                        res.add(tempSupply[j] to (i to j))
                        tempDemand[i] -= tempSupply[j]
                        tempSupply[j] = 0
                    }
                } else if (tempDemand[i] == 0 && i < costs.size - 1 && j > 0) {
                    i++
                    j--
                } else if (tempDemand[i] == 0 && i < costs.size - 1) {
                    i++
                } else {
                    j++
                }
            }
        }
        return res
    }

}
