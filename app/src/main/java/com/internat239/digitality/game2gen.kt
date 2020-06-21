package com.internat239.digitality

fun gen2_game(hard:Int):String {
    var operations: String = " +-*^%&|"
    var ans: String = ""
    var col: Array<Int> = Array(10, { i -> if (i == 0) 1; else 0 })
    if (hard == 1) {
        val num = (2..4).random()
        for (i in 0..num) {
            var temp = (1..9).random()
            var it = 0
            while (col[temp] == 1 && it < 10) {
                temp = (1..9).random()
                it++;
            }
            if (col[temp] == 1) {
                for (j in 0..9) {
                    if (col[j] != 1) {
                        temp = j
                        break
                    }
                }
            }
            col[temp] = 1
            ans += temp
            if (i != num) {
                var opo: Char = operations[(0..3).random()]
                if (opo != ' ')
                    ans += opo
            }
        }
        return ans
    }
    if (hard == 2) {
        val num = (4..7).random()
        for (i in 0..num) {
            var temp = (1..9).random()
            var it = 0
            while (col[temp] == 1 && it < 10) {
                temp = (1..9).random()
                it++;
            }
            if (col[temp] == 1) {
                for (j in 0..9) {
                    if (col[j] != 1) {
                        temp = j;
                        break
                    }
                }
            }
            col[temp] = 1
            ans += temp
            if (i != num) {
                var opo: Char = operations[(0..3).random()]
                if (opo != ' ')
                    ans += opo
            }
        }
        return ans
    }
    if (hard == 3) {
        val num = (7..9).random()
        for (i in 0..num) {
            var temp = (1..9).random()
            var it = 0
            while (col[temp] == 1 && it < 10) {
                temp = (1..9).random()
                it++;
            }
            if (col[temp] == 1) {
                for (j in 0..9) {
                    if (col[j] != 1) {
                        temp = j
                        break
                    }
                }
            }
            col[temp] = 1
            ans += temp
            if (i != num) {
                var opo: Char = operations[(0..7).random()]
                if (opo != ' ')
                    ans += opo
            }
        }
        return ans
    }
    return ""
}
