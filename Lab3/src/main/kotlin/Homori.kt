import kotlin.math.absoluteValue

class Homori() {
    //Works
    private fun prepareSimplex(
        matrix: ArrayList<ArrayList<Int>>,
        signs: ArrayList<String>,
        b: ArrayList<Int>,
        z: ArrayList<Int>
    ): ArrayList<ArrayList<Double>> {
        //определяем количество строк как количество нервенств + 1 для строки дельт
        var m = b.size + 1
        //определяем количество столбцов как количество коэффициентов функции +
        // базисные переменные и столбец B со значениями неравенств
        var n = z.size + m
        var simplexTable = ArrayList<ArrayList<Double>>()
        var temp = ArrayList<Double>()
        var fZ = 0
        var lZ = signs.size - (fZ + 1)
        var bi = 0
        for (i in 0..<m - 1) {
            for (j in 0..<z.size) {
                if (signs[i] == ">=") {
                    temp.add((matrix[i][j] * (-1)).toDouble())
                } else {
                    temp.add((matrix[i][j]).toDouble())
                }
            }
            for (i in 0..<fZ) {
                temp.add(0.0)
            }
            temp.add(1.0)
            for (i in 0..<lZ) {
                temp.add(0.0)
            }
            if (signs[i] == ">=") {
                temp.add((b[bi] * (-1).toDouble()))
            } else {
                temp.add(b[bi].toDouble())
            }
            bi++
            fZ++
            lZ--
            simplexTable.add(temp)
            temp = ArrayList()
        }
        for (j in simplexTable[0].indices) {
            temp.add(0.0)
        }
        simplexTable.add(temp)
        println("====Исходная симплекс таблица====")
        for (i in simplexTable) {
            for (j in i) {
                if (j < 0) {
                    print("| $j ")
                } else {
                    print("|  $j ")
                }
            }
            println("|")
        }
        outerLoop@ while (true) {
            var maxAbsStr = 0.0
            //ID строки
            var maxAbsStrId = -1
            for (i in simplexTable) {
                if (i.last() < 0 && maxAbsStr < i.last().absoluteValue) {
                    maxAbsStr = i.last()
                    maxAbsStrId = simplexTable.indexOf(i)
                }
            }
            if (maxAbsStr == 0.0) {
                break@outerLoop
            }
            var maxAbs = 0.0
            var maxAbsId = -1
            for (i in 0..<simplexTable[maxAbsStrId].size - 1) {
                if (simplexTable[maxAbsStrId][i] < 0 && simplexTable[maxAbsStrId][i].absoluteValue > maxAbs) {
                    maxAbs = simplexTable[maxAbsStrId][i]
                    maxAbsId = i
                }
            }
            for (j in simplexTable[maxAbsStrId].indices) {
                if (simplexTable[maxAbsStrId][j] != 0.0) {
                    simplexTable[maxAbsStrId][j] = simplexTable[maxAbsStrId][j] / maxAbs
                }
            }
            var multipliers = ArrayList<Double>()
            for (i in 0..<simplexTable.size) {
                if (i != maxAbsStrId) {
                    multipliers.add(simplexTable[i][maxAbsId] * (-1))
                } else {
                    multipliers.add(0.0)
                }
            }
            println("Множители для преобразования симплекс-таблицы относительно новой базисной переменной: $multipliers")
            for (i in simplexTable.indices) {
                for (j in simplexTable[0].indices) {
                    simplexTable[i][j] = simplexTable[i][j] + (multipliers[i] * simplexTable[maxAbsStrId][j])
                }
            }
            println("====Обновлённая симплекс таблица====")
            for (i in simplexTable) {
                for (j in i) {
                    if (j < 0) {
                        print("| $j ")
                    } else {
                        print("|  $j ")
                    }
                }
                println("|")
            }
            var deltas = ArrayList(MutableList(simplexTable[0].size) { 0.0 })
            for (i in z.indices) {
                deltas[i] = z[i].toDouble()
            }
            var zeroCounter = ArrayList<Int>()
            var c = 0
            for (i in simplexTable[0].indices) {
                for (j in simplexTable.indices) {
                    if (simplexTable[j][i] == 0.0) {
                        c++
                    }
                }
                zeroCounter.add(c - 1)
                c = 0
            }
            var baseIdVector = ArrayList<Int>()

            for (i in 0..<simplexTable.size - 1) {
                for (j in 0..<simplexTable[i].size - 1) {
                    if ((simplexTable[i][j] == 1.0) && (zeroCounter[j] == simplexTable.size - 2)) {
                        baseIdVector.add(j)
                    }
                }
            }
            for (i in 0..<simplexTable.size - 1) {
                for (j in 0..<simplexTable[i].size) {
                    simplexTable.last()[j] = simplexTable.last()[j] + (deltas[baseIdVector[i]] * simplexTable[i][j])
                }
            }
            for (j in simplexTable.last().indices) {
                simplexTable.last()[j] = simplexTable.last()[j] - deltas[j]
            }
            println("====Обновлённая симплекс таблица c дельтами====")
            for (i in simplexTable) {
                for (j in i) {
                    if (j < 0) {
                        print("| $j ")
                    } else {
                        print("|  $j ")
                    }
                }
                println("|")
            }
        }

        return simplexTable
    }


    private fun simplexMethod(
        matrix: ArrayList<ArrayList<Int>>,
        signs: ArrayList<String>,
        b: ArrayList<Int>,
        z: ArrayList<Int>
    ): ArrayList<ArrayList<Double>> {
        val simplexTable = prepareSimplex(matrix, signs, b, z)
        val rows = simplexTable.size
        val cols = simplexTable[0].size
        //Для частных
        var bx = ArrayList<Double>()
        var iteration = 0
        //запускаем бесконечный цикл до тех пор, пока все значения не станут отрицательными в последней строке
        outerLoop@ while (true) {
            var maxElement = 0.0
            var maxElId = -1
            //находим ведущий столбец Works
            for (j in 0..<cols - 1) {
                if (simplexTable.last()[j] > maxElement) {
                    maxElement = simplexTable.last()[j]
                    maxElId = j
                }
            }
            if (maxElId == 0) {
                return simplexTable
            }
            println()
            println("====Итерация № ${iteration + 1}====")
            println("====Максимальный элемент в последней строке: $maxElement====")
            var minBx = Double.MAX_VALUE
            var minBxId = -1
            for (i in 0..<rows - 1) {
                //находим частные последнего столбца на ведущий столбец
                if (simplexTable[i][maxElId] != 0.0) {
                    bx.add((simplexTable[i][cols - 1] / simplexTable[i][maxElId]))
                } else {
                    bx.add(0.0)
                }

                if (minBx > bx[i] && bx[i] > 0.0) {
                    minBx = bx[i]
                    minBxId = i
                }
            }
            println("B/x${maxElId + 1} =  $bx")
            println("Минимальное положительное значение среди найденных отношений = $minBx")
            //в find_element записываем значение элемента находящегося на пересечении
            //ранее ведущего столбца и ведущей строки
            var findEl = simplexTable[minBxId][maxElId]
            //делим ведущую строку на find_element
            for (j in 0..<cols) {
                if (simplexTable[minBxId][j] != 0.0) {
                    simplexTable[minBxId][j] = simplexTable[minBxId][j] / findEl
                }
            }
            println("Поделим ${minBxId + 1} строку на $findEl")
            for (i in simplexTable) {
                for (j in i) {
                    if (j < 0) {
                        print("| $j ")
                    } else {
                        print("|  $j ")
                    }
                }
                println("|")
            }
            var multipliers = ArrayList<Double>()
            for (i in 0..<rows) {
                if (i != minBxId) {
                    multipliers.add(simplexTable[i][maxElId] * (-1))
                } else {
                    multipliers.add(0.0)
                }
            }
            println("Множители для преобразования симплекс-таблицы относительно новой базисной переменной = $multipliers")
            for (i in 0..<rows) {
                for (j in 0..<cols) {
                    simplexTable[i][j] = simplexTable[i][j] + (multipliers[i] * simplexTable[minBxId][j])
                }
            }
            println("Преобразованная симплекс-таблица:")
            for (i in simplexTable) {
                for (j in i) {
                    if (j < 0) {
                        print("| $j ")
                    } else {
                        print("|  $j ")
                    }
                }
                println("|")
            }
            iteration++
            return simplexTable
            break@outerLoop
        }
    }

    fun homoriMethod(
        matrix: ArrayList<ArrayList<Int>>,
        signs: ArrayList<String>,
        b: ArrayList<Int>,
        z: ArrayList<Int>
    ): ArrayList<ArrayList<Double>> {
        var newHomoriTable = simplexMethod(matrix, signs, b, z)
        var iteration = 0

        println("\nМетод Гомори")
        outerLoop@ while (true) {
            var solution = ArrayList<Double>()
            var rows = newHomoriTable.size
            var cols = newHomoriTable[0].size
            val zeroCounter = ArrayList(MutableList(cols - 1) { 0 })
            var curZeroCounter = 0

            for (i in 0..<rows - 1) {
                for (j in 0..<cols - 1) {
                    if (newHomoriTable[i][j] == 0.0) {
                        zeroCounter[j]++
                    }
                }
            }

            for (i in 0..<rows - 1) {
                for (j in 0..<z.size) {
                    if (newHomoriTable[i][j] == 1.0 && zeroCounter[j] == rows - 2) {
                        solution.add(newHomoriTable[i][cols - 1])
                    }
                }
            }
            var solutionSize = solution.size
            println("Найденное решение: x = ${solution}, z = ${newHomoriTable[rows - 1][cols - 1]}")
            //Проверка на цело численность
            innerLoop@ for (i in 0..<solutionSize) {
                if (solution[i] % 1.0 != 0.0) {
                    println("Решение не целочисленное")
                    break@innerLoop
                } else if (i == solutionSize - 1) {
                    println("Решение найдено")
                    println("x min = $solution")
                    println("z min = ${newHomoriTable[rows - 1][cols - 1]}")
                    return newHomoriTable
                }
            }
            println("Итерация № ${iteration + 1}")
            var fractionSol = ArrayList(MutableList(solutionSize) { 0.0 })
            var maxFraction = 0.0
            var maxFractionId = -1
            for (i in 0..<solutionSize) {
                fractionSol[i] = solution[i] % 1.0
                if (fractionSol[i] >= maxFraction) {
                    maxFraction = fractionSol[i]
                    maxFractionId = i
                }
            }
            println("Дробные части найденных значений х: $fractionSol")
            println("Большее из них: $maxFraction")
            innerLoop@ for (i in 0..<rows - 1) {
                if (newHomoriTable[i][maxFractionId] == 1.0) {
                    maxFractionId = i
                    break@innerLoop
                }
            }
            var baseVector = ArrayList(MutableList(cols + 1) { 0.0 })
            var reserveVar = 0
            for (j in 0..<cols - 1) {
                if (newHomoriTable[maxFractionId][j] % 1.0 != 0.0) {
                    if (newHomoriTable[maxFractionId][j] > 0) {
                        baseVector[j] = -(newHomoriTable[maxFractionId][j] % 1.0)
                    } else {
                        reserveVar = (newHomoriTable[maxFractionId][j] * -1.0).toInt() + 1
                        baseVector[j] = -((newHomoriTable[maxFractionId][j] - reserveVar) % 1)
                    }
                }
            }
            baseVector[cols - 1] = 1.0
            baseVector[cols] = -maxFraction
            println("Значения в симплекс-таблице новой базисной переменной: $baseVector")
            var homoriTable = ArrayList<ArrayList<Double>>()
            for (i in newHomoriTable){
                homoriTable.add(i)
                println(homoriTable.last())
            }
            newHomoriTable = ArrayList(MutableList(rows + 1) { ArrayList(MutableList(cols + 1) { 0.0 }) })
            for (i in 0..<rows - 1) {
                for (j in 0..<cols - 1) {
                    newHomoriTable[i][j] = homoriTable[i][j]
                }
            }
            for (i in 0..<rows - 1) {
                newHomoriTable[i][cols] = homoriTable[i][cols - 1]
            }
            for (j in 0..<cols - 1) {
                newHomoriTable[rows][j] = homoriTable[rows - 1][j]
            }
            newHomoriTable[rows][cols] = homoriTable[rows - 1][cols - 1]
            for (j in 0..cols) {
                newHomoriTable[rows - 1][j] = baseVector[j]
            }
            println("Симплекс-таблица с новой базисной переменной:")
            for (i in newHomoriTable) {
                for (j in i) {
                    if (j < 0) {
                        print("| $j ")
                    } else {
                        print("|  $j ")
                    }
                }
                println("|")
            }
            // создаем вектор для нахождения частных при делении последней строки на предпоследнюю строку (строку новой базисной переменной)
            var bNewVector = ArrayList(MutableList(cols - 1) { 0.0 })
            for (i in 0..<cols - 1) {
                bNewVector[i] = Double.POSITIVE_INFINITY
            }
            for (j in 0..<cols - 1) {
                if (newHomoriTable[rows - 1][j] != 0.0) {
                    bNewVector[j] = newHomoriTable[rows][j] / newHomoriTable[rows - 1][j]
                }
            }
            println("Частные z/x${cols} = $bNewVector")
            var minB = bNewVector.min()
            var minBId = bNewVector.indexOf(minB)
            println("Минимальное значение среди найденных частных = $minB")
            println("Поделим $rows строку на ${newHomoriTable[rows - 1][minBId]}")
            var findEl = newHomoriTable[rows - 1][minBId]
            for (j in 0..cols) {
                if (newHomoriTable[rows - 1][j] != 0.0) {
                    newHomoriTable[rows - 1][j] = newHomoriTable[rows - 1][j] / findEl
                }
            }
            for (i in homoriTable) {
                for (j in i) {
                    if (j < 0) {
                        print("| $j ")
                    } else {
                        print("|  $j ")
                    }
                }
                println("|")
            }
            var multipliers = ArrayList(MutableList(rows + 1) { 0.0 })
            for (i in 0..rows) {
                if (i != rows - 1) {
                    multipliers[i] = -newHomoriTable[i][minBId]
                }
            }
            println("Множители для преобразования симплекс-таблицы относительно новой базисной переменной = $multipliers")
            for (i in 0..rows) {
                for (j in 0..cols) {
                    newHomoriTable[i][j] = newHomoriTable[i][j] + (multipliers[i] * newHomoriTable[rows - 1][j])
                }
            }
            println("Преобразованная симплекс-таблица: ")
            for (i in newHomoriTable) {
                for (j in i) {
                    if (j < 0) {
                        print("| $j ")
                    } else {
                        print("|  $j ")
                    }
                }
                println("|")
            }
            iteration++
        }
    }
}