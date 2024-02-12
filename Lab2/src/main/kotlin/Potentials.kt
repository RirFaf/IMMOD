import kotlin.math.absoluteValue

class Potentials() {
    fun findPotentials(
        costs: ArrayList<IntArray>,
        simplifiedPlan: ArrayList<Pair<Int, Int>>
    ): Pair<Int, Pair<Int, Int>> {
        var res = Pair(Int.MAX_VALUE, Pair(-1, -1))
        var u = Array<Int?>(costs.size) { _ -> null }
        var v = Array<Int?>(costs.size) { _ -> null }
        u[0] = 0
        for (item in simplifiedPlan) {
            if (u[item.first] == null) {
                u[item.first] = costs[item.first][item.second] - v[item.second]!!
            } else {
                v[item.second] = costs[item.first][item.second] - u[item.first]!!
            }
        }
        println("+++U+++")
        for (i in u.indices) {
            println("U [$i] = ${u[i]}")
        }
        println("+++V+++")
        for (i in v.indices) {
            println("V [$i] = ${v[i]}")
        }
        println("+++Unallocated points' potentials+++")
        var unplannedPoints = ArrayList<Pair<Int, Pair<Int, Int>>>()
        for (i in costs.indices) {
            for (j in costs[i].indices) {
                if (!simplifiedPlan.contains(Pair(i, j))) {
                    unplannedPoints.add(Pair((costs[i][j] - (u[i]!! + v[j]!!)), Pair(i, j)))
                    println("${unplannedPoints.last().first} ${unplannedPoints.last().second}")
                }
            }
        }
        for (i in unplannedPoints){
            if (i.first<res.first){
                res = i
            }
        }
        return res
    }

    private fun findCycle(
        plan: ArrayList<Pair<Int, Int>>,
        start: Pair<Int, Int>,
        size: Int
    ): ArrayList<Pair<Int, Int>> {
        var cycle = ArrayList<Pair<Int, Int>>()
        var currentPoint = start.copy()
        var currentRad = ArrayList<Pair<Int, Int>>()
        var workingPlan = ArrayList<Pair<Int, Int>>()
        for (i in plan) {
            workingPlan.add(i)
        }
        var visitedPoints = ArrayList<Pair<Int, Int>>()
        var retry = true
        var i = 0
        cycle.add(start)
        outerLoop@ while (retry) {
            i++
            currentRad.removeAll(currentRad.toSet())
            //Устанавливаем радиус по вертикали и горизонтали
            if (currentPoint.first > 0) {
                currentRad.add(Pair(currentPoint.first - 1, currentPoint.second))
            }
            if (currentPoint.first < size - 1) {
                currentRad.add(Pair(currentPoint.first + 1, currentPoint.second))
            }
            if (currentPoint.second > 0) {
                currentRad.add(Pair(currentPoint.first, currentPoint.second - 1))
            }
            if (currentPoint.second < size - 1) {
                currentRad.add(Pair(currentPoint.first, currentPoint.second + 1))
            }
            //Выводим точку и ее радиус
//            println("(${currentPoint.first}, ${currentPoint.second}) current Point")
//            for (i in currentRad) {
//                println("(${i.first}, ${i.second}) Rad")
//            }

            var r = true
            thisLoop@ for (i in currentRad) {
                if (workingPlan.contains(i)) {
                    r = true
                    break@thisLoop
                } else {
                    r = false
                }
            }
            if (!r) {
                workingPlan = plan
                workingPlan.add(start)
                cycle = workingPlan
                break@outerLoop
            }

            if (currentPoint != plan.last()) {
                innerLoop@ for (point in currentRad) {
                    if (workingPlan.contains(point) && !visitedPoints.contains(point)) {
                        cycle.add(point)
                        visitedPoints.add(currentPoint)
                        currentPoint = point
                        currentRad.removeAll(currentRad.toSet())
                        break@innerLoop
                    }
                }
            } else {
                cycle.add(currentPoint)
                break@outerLoop
            }
        }
        cycle.remove(cycle.last())
        return cycle
    }

    private fun findCorners(
        cycle: ArrayList<Pair<Int, Int>>
    ): ArrayList<Pair<Boolean, Pair<Int, Int>>> {
        val corners = ArrayList<Pair<Int, Int>>()
        corners.add(cycle[0])
        for (i in 1..<cycle.size - 1) {
            if (cycle[i - 1].first != cycle[i + 1].first && cycle[i - 1].second != cycle[i + 1].second) {
                corners.add(cycle[i])
            }
        }

        var res = ArrayList<Pair<Boolean, Pair<Int, Int>>>()
        for (i in corners.indices) {
            if (i % 2 == 1) {
                res.add(Pair(false, corners[i]))
            } else {
                res.add(Pair(true, corners[i]))
            }
        }

        if (
            (cycle[0].second - cycle.last().second).absoluteValue > 1 ||
            (cycle[0].first - cycle.last().first).absoluteValue > 1
        ) {
            res.add(Pair(!res.last().first, cycle.last()))
        }
        return res
    }

    private fun optimizePlan(
        plan: ArrayList<Pair<Int, Pair<Int, Int>>>,
        corners: ArrayList<Pair<Boolean, Pair<Int, Int>>>,
        start: Pair<Int, Int>,
    ): ArrayList<Pair<Int, Pair<Int, Int>>> {
        var b = ArrayList<Int>()
        var bb = ArrayList<Int>()
        for (i in corners) {
            for (j in plan) {
                if (i.second == j.second) {
                    b.add(j.first)
                    bb.add(j.first)
                }
            }
        }
        for (i in b.indices) {
            if (i % 2 == 1) {
                bb.remove(b[i])
            }
        }
        var min = bb.min()
        var changedPlan = ArrayList<Pair<Int, Pair<Int, Int>>>()
        changedPlan.add(Pair(min, start))
        for (i in plan) {
            changedPlan.add(i)
        }

        for (i in corners.indices) {
            for (j in 1..<changedPlan.size) {
                if (changedPlan[j].second == corners[i].second) {
                    if (corners[i].first) {
                        changedPlan[j] = Pair(changedPlan[j].first + min, changedPlan[j].second)
                    } else {
                        changedPlan[j] = Pair(changedPlan[j].first - min, changedPlan[j].second)
                    }
                }
            }
        }
        var t = ArrayList<Pair<Int, Pair<Int, Int>>>()
        for (i in changedPlan) {
            if (i.first == 0) {
                t.add(i)
            }
        }
        changedPlan.removeAll(t.toSet())
        return changedPlan
    }

    fun solveProblem(
        costs: ArrayList<IntArray>,
        simplifiedPlan: ArrayList<Pair<Int, Int>>,
        plan: ArrayList<Pair<Int, Pair<Int, Int>>>,
    ): Int {
        var res = 0
        val start = findPotentials(costs, simplifiedPlan)
        if (start.second != Pair(-1,-1)){
            val cycle = findCycle(simplifiedPlan, start.second, 3)
            println("+++Potentials cycle+++")
            for (i in cycle) {
                println(i)
            }
            println("+++Corners+++")
            val corners = findCorners(cycle)
            for (i in corners) {
                println("${i.first} ${i.second}")
            }
            println("++Optimized Plan+++")
            val optimizedPlan = optimizePlan(plan, corners, start.second)
            for (i in optimizedPlan.indices) {
                println("${optimizedPlan[i].first} ${optimizedPlan[i].second}")

            }
            for (i in optimizedPlan.indices) {
                res += optimizedPlan[i].first * costs[optimizedPlan[i].second.first][optimizedPlan[i].second.second]
            }
        }
        return res
    }
}