package com.internat239.digitality


fun game1(hard: Int): Pair<String, String> {
    val query = gen2_game(hard)
    var ans = ""
    val result = Solver.solve(query)
    for (q in query) {
        if (!q.isDigit())
            ans += q
    }
    return Pair("$ans=$result", "$query=$result")
}

fun game2(hard: Int): Pair<String, String> {
    val query = gen2_game(hard)
    var ans = ""
    val result = Solver.solve(query)
    for (q in query) {
        if (q.isDigit())
            ans += q
    }
    return Pair("$ans=$result", "$query=$result")
}

fun game3(hard: Int): Pair<String, String> {
    val query = gen2_game(hard)
    var ans = ""
    val result = Solver.solve(query)
    val used: Array<Int> = Array(query.length) { 0 }
    var it = 0
    while (it < query.length / 2) {
        val k = (0..query.lastIndex).random()
        used[k] = 1
        it += 1
    }
    for (i in 0..query.lastIndex) {
        if (used[i] == 0)
            ans += query[i]
    }
    return Pair("$ans=$result", "$query=$result")
}

fun hint(task: Pair<String, String>): Pair<String, Char> {
    val query = task.first
    val ans = task.second
    for (i in 0..query.lastIndex) {
        if (query[i] != ans[i])
            return Pair(
                query.substring(0, i) + ans[i] + query.substring(i, query.lastIndex + 1),
                ans[i]
            )
    }
    return Pair(task.first, ' ')
}