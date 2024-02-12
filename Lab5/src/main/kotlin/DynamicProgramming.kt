import kotlin.math.cos
import kotlin.math.max

class DynamicProgramming {
    fun knapsackUnbounded(w: Int, weights: IntArray, costs: IntArray): ArrayList<Int> {
        val n = costs.size

        val optCosts = ArrayList(MutableList(n + 1) { ArrayList(MutableList(w + 1) { 0 }) })
        for (k in 0..n) {
            for (s in 0..w) {
                if (k == 0 || s == 0) {
                    optCosts[k][s] = 0
                } else {
                    if (s >= weights[k - 1]) {
                        optCosts[k][s] = max(optCosts[k - 1][s], optCosts[k - 1][s - weights[k - 1]] + costs[k - 1])
                    } else {
                        optCosts[k][s] = optCosts[k - 1][s]
                    }
                }
            }
        }
        var res = ArrayList<Int>()
        println("таблица dp")
        for (i in optCosts) {
            println(i)
        }
        traceResult(optCosts, weights, n, w, res)

        return res
    }

    fun traceResult(
        optCosts: ArrayList<ArrayList<Int>>,
        weights: IntArray,
        k: Int,
        s: Int,
        res: ArrayList<Int>
    ) {
        if (optCosts[kё][s] == 0) {
            return
        }
        if (optCosts[k - 1][s] == optCosts[k][s]) {
            traceResult(optCosts, weights, k - 1, s, res)
        } else {
            traceResult(optCosts, weights, k - 1, s - weights[k - 1], res)
            res.add(k)
        }
    }
}