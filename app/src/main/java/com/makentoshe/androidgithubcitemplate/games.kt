package com.makentoshe.androidgithubcitemplate
import com.makentoshe.androidgithubcitemplate.gen2_game
import com.makentoshe.androidgithubcitemplate.Solver
fun game1(hard:Int):String {
    var query = gen2_game(hard)
    var ans = ""
    var result = Solver().solve(query).toInt()
    query = query + "=" + result
    for (i in 0..query.lastIndex) {
        if (!query[i].isDigit())
            ans += query[i]
        else
            ans += " "
    }
    return ans
}
fun game2(hard:Int):String {
    var query = gen2_game(hard)
    var ans = ""
    var result = Solver().solve(query).toInt()
    query = query + "=" + result
    for (i in 0..query.lastIndex) {
        if (query[i].isDigit())
            ans += query[i]
        else
            ans += " "
    }
    return ans
}
