package com.makentoshe.androidgithubcitemplate
import com.makentoshe.androidgithubcitemplate.gen2_game
import com.makentoshe.androidgithubcitemplate.Solver
fun game1(hard:Int):String {
    var query = gen2_game(hard)
    var ans = ""
    var result = Solver().solve(query).toInt()
    for (i in 0..query.lastIndex) {
        if (!query[i].isDigit())
            ans += query[i]
    }
    return ans + '=' + result
}
fun game2(hard:Int):String {
    var query = gen2_game(hard)
    var ans = ""
    var result = Solver().solve(query).toInt()
    for (i in 0..query.lastIndex) {
        if (query[i].isDigit())
            ans += query[i]
    }
    return ans + '=' + result
}
fun game3(hard:Int):String {
    var query = gen2_game(hard)
    var ans = ""
    var result = Solver().solve(query).toInt()
    var used: Array<Int> = Array(query.length, { i -> 0 })
    var it = 0
    while (it < query.length / 2) {
        var k = (0..query.lastIndex).random()
        used[k] = 1
        it += 1
    }
    for (i in 0..query.lastIndex) {
        if (used[i] == 0)
            ans += query[i]
    }
    return ans + '=' + result
}
