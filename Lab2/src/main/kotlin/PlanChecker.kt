class PlanChecker() {
    fun checkPlan(plan: ArrayList<Pair<Int, Pair<Int, Int>>>,demand: IntArray, supply: IntArray): Boolean{
        return plan.size == (demand.size+supply.size-1)
    }
    fun checkSolvable(supply: IntArray, demand: IntArray): Boolean {
        var a = 0
        var b = 0
        for (i in supply.indices) {
            a += supply[i]
        }
        for (i in demand.indices) {
            b += demand[i]
        }
        return a == b
    }
}