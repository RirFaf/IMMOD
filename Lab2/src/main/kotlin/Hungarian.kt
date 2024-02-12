class Hungarian() {
    //Works OK
    private fun firstMatrixMod(matrix: ArrayList<IntArray>): ArrayList<IntArray> {
        var temp = ArrayList<IntArray>()
        for (i in matrix.indices) {
            temp.add(matrix[i])
        }

        for (i in temp.indices) {
            temp[i] = modRow(temp[i])
        }

        temp = flipMatrix(temp)
        for (i in temp.indices) {
            if (!temp[i].contains(0)) {
                temp[i] = modRow(temp[i])
            }
        }
        temp = flipMatrix(temp)
        return temp
    }

    //Works OK
    private fun findMinBiggerThan(vector: IntArray, biggerThan: Int = 0): Int {
        var res = Int.MAX_VALUE
        for (i in vector.indices) {
            if (vector[i] > biggerThan) {
                if (vector[i] < res) {
                    res = vector[i]
                }
            }
        }
        return res
    }

    //Works OK
    private fun modRow(vector: IntArray): IntArray {
        val res = IntArray(vector.size)
        val min = vector.min()
        for (i in vector.indices) {
            res[i] = vector[i] - min
        }
        return res
    }

    //Works OK
    private fun flipMatrix(matrix: ArrayList<IntArray>): ArrayList<IntArray> {
        val res = arrayListOf(IntArray(matrix[0].size))
        for (i in matrix.indices) {
            res.add(IntArray(matrix[0].size))
            for (j in matrix[i].indices) {
                res[i][j] = (matrix[j][i])
            }
        }
        res.removeAt(res.size - 1)
        return res
    }

    //Works OK
    private fun findRowWithSubZero(costs: ArrayList<IntArray>): Int? {
        var res: Int? = null
        for (i in costs.indices) {
            for (j in costs[i].indices) {
                if (costs[i][j] < 0) {
                    res = i
                    break
                }
            }
        }
        return res
    }

    //Works OK
    private fun iterMatrixMod(costs: ArrayList<IntArray>, row: Int): ArrayList<IntArray> {
        var res = costs
        val min = findMinBiggerThan(res[row], 0)
        for (i in res[row].indices) {
            res[row][i] = res[row][i] - min
        }
        res = flipMatrix(res)
        while (findRowWithSubZero(res) != null) {
            val i = findRowWithSubZero(res)
            for (j in res[i!!].indices) {
                res[i][j] += min
            }
        }
        res = flipMatrix(res)
        return res
    }

    //Works OK
    private fun findZerosCoordinates(costs: ArrayList<IntArray>): ArrayList<Pair<Int, Int>> {
        val res: ArrayList<Pair<Int, Int>> = ArrayList()
        for (i in costs.indices) {
            for (j in costs[i].indices) {
                if (costs[i][j] == 0) {
                    res.add(Pair(i, j))
                }
            }
        }
        println()
        return res
    }

    fun allocate(
        costs: ArrayList<IntArray>,
        demand: IntArray,
        supply: IntArray
    ): ArrayList<Pair<Int, Pair<Int, Int>>> {
        var tempCosts = firstMatrixMod(costs)
        var tempDemand = demand.clone()
        var tempSupply = supply.clone()

        var zerosCoordinates = findZerosCoordinates(tempCosts)

        var tempMin = Int.MAX_VALUE
        //Для временных приоритетов с суммами
        var tempRes = ArrayList<Pair<Int, Pair<Int, Int>>>()
        //Для приоритетов с отправленными единицами
        val res = ArrayList<Pair<Int, Pair<Int, Int>>>()
        var retry = true
        var p: List<Pair<Int, Pair<Int, Int>>>
        var i = 0
        while (retry) {
            i++
            println("+++Iteration $i+++")
            println("\n+++Changed prices matrix+++")
            for (j in tempCosts.indices) {
                for (k in tempCosts[j].indices) {
                    print("${tempCosts[j][k]} ")
                }
                println()
            }
            println("\n+++Priorities+++")
            for (i in zerosCoordinates.indices) {
                if (
                    tempDemand[zerosCoordinates[i].first] > 0 &&
                    tempSupply[zerosCoordinates[i].second] > 0
                ) {
                    tempMin = tempDemand[zerosCoordinates[i].first] + tempSupply[zerosCoordinates[i].second]
                    tempRes.add(tempMin to zerosCoordinates[i])
                }
            }
            p = tempRes.sortedBy { it.first }
            for (i in p.indices) {
                println("${p[i].first} (${p[i].second.first}, ${p[i].second.second})")
            }
            for (i in p.indices) {
                if (tempDemand[zerosCoordinates[i].first] < tempSupply[zerosCoordinates[i].second]) {
                    if (tempDemand[zerosCoordinates[i].first] != 0) {
                        res.add(tempDemand[zerosCoordinates[i].first] to zerosCoordinates[i])
                    }
                    tempSupply[zerosCoordinates[i].second] -= tempDemand[zerosCoordinates[i].first]
                    tempDemand[zerosCoordinates[i].first] = 0
                } else {
                    if (tempSupply[zerosCoordinates[i].second] != 0) {
                        res.add(tempSupply[zerosCoordinates[i].second] to zerosCoordinates[i])
                    }
                    tempDemand[zerosCoordinates[i].first] -= tempSupply[zerosCoordinates[i].second]
                    tempSupply[zerosCoordinates[i].second] = 0
                }
            }
            println("\n+++Allocation+++")
            for (i in res.indices){
                println("${res[i].first} (${res[i].second.first}, ${res[i].second.second})")
            }
            println("------------------------\n")
            outerLoop@ for (i in tempDemand.indices) {
                if (tempDemand[i] != 0) {
                    retry = true
                    tempCosts = iterMatrixMod(tempCosts, i)
                    zerosCoordinates = findZerosCoordinates(tempCosts)
                    tempSupply = supply.clone()
                    tempDemand = demand.clone()
                    tempRes.removeAll(tempRes.toSet())
                    res.removeAll(res.toSet())
                    break@outerLoop
                } else {
                    retry=false
                }
            }

        }
        return res
    }

    fun solveProblem(
        costs: ArrayList<IntArray>,
        demand: IntArray,
        supply: IntArray
    ):Int {
        var res = 0
        val allocation = allocate(costs, demand, supply)
        for (i in allocation.indices){
            res += allocation[i].first*costs[allocation[i].second.first][allocation[i].second.second]
        }
        return res
    }
}