package com.makentoshe.androidgithubcitemplate

class Solver {
    private var expression: String = ""
    private var pos = -1
    private var currNumber = 0
    private var currToken = 0.toChar()
    private val ops = "+-*/@%|&^"

    private fun getNextToken() {
        pos++
        if (pos >= expression.length) {
            currToken = 'q'
        } else {
            when (expression[pos]) {
                in ops, '(', ')' -> {
                    currToken = expression[pos]
                }
                in '0'..'9' -> {
                    currToken = 'n'
                    val pos0 = pos
                    while (pos < expression.length && expression[pos].isDigit()) pos++
                    currNumber = expression.substring(pos0, pos).toInt()

                    pos--
                }
                else -> {
                    currToken = 'i'
                }
            }
        }
    }

    private fun prim(): Int {
        return when (currToken) {
            'n' -> {
                getNextToken()
                currNumber
            }
            '-' -> {
                getNextToken()
                -prim()
            }
            '(' -> {
                getNextToken()
                val e = operPlus()
                getNextToken()
                e
            }
            else -> {
                operOr()
            }
        }
    }

    private fun operMult(): Int {
        var left = prim()
        while (true) {
            when (currToken) {
                '*' -> {
                    getNextToken()
                    left *= prim()
                }
                '/' -> {
                    getNextToken()
                    left /= prim()
                }
                '@' -> {
                    getNextToken()
                    left = Math.pow(left.toDouble(), prim().toDouble()).toInt()
                }
                '%' -> {
                    getNextToken()
                    left %= prim()
                }
                else -> {
                    return left
                }
            }
        }
    }

    private fun operPlus(): Int {
        var left = operMult()
        while (true) {
            when (currToken) {
                '+' -> {
                    getNextToken()
                    left += operMult()
                }
                '-' -> {
                    getNextToken()
                    left -= operMult()
                }
                else -> {
                    return left
                }
            }
        }
    }

    private fun operAnd() : Int {
        var left = operPlus()
        while (true) {
            when (currToken) {
                '&' -> {
                    getNextToken()
                    left = left and operMult()
                }
                else -> {
                    return left
                }
            }
        }
    }

    private fun operXor() : Int {
        var left = operAnd()
        while (true) {
            when (currToken) {
                '^' -> {
                    getNextToken()
                    left = left xor operMult()
                }
                else -> {
                    return left
                }
            }
        }
    }

    private fun operOr() : Int {
        var left = operXor()
        while (true) {
            when (currToken) {
                '|' -> {
                    getNextToken()
                    left = left or operMult()
                }
                else -> {
                    return left
                }
            }
        }
    }

    private fun checkExpression(): Boolean {
        expression = expression.replace(" ", "")
        pos = -1
        var res = true
        if (expression.isEmpty()) res = false
        var prevToken = '('
        getNextToken()
        while (currToken != 'q') {
            if (currToken == 'i') res = false
            when (prevToken) {
                'n', ')' -> {
                    if (currToken == '(' || currToken == 'n') {
                        expression = expression.substring(0, pos) + '*' + expression.substring(pos)
                        pos++
                    }
                }
                in ops, '(' -> {
                    when(currToken) {
                        in ops, ')' -> res = false
                    }
                }
            }
            prevToken = currToken
            getNextToken()
        }
        when(prevToken) {
            in ops, '(' -> res = false
        }
        var braces = 0
        for (e in expression) {
            if (e == '(') braces++ else if (e == ')') braces--
        }
        if (braces != 0) res = false
        pos = -1
        return res
    }

    fun solve(expr: String): Int {
        expression = expr
        if (checkExpression()) {
            getNextToken()
            return operOr()
        }
        return 0
    }
}

