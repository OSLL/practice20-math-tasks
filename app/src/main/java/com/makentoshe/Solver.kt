package com.makentoshe

class Solver {
    private var expression: String = ""
    private var pos = -1
    private var currNumber = 0.0
    private var currToken = 0.toChar()

    private fun getNextToken() {
        pos++
        if (pos >= expression.length) {
            currToken = 'q'
        } else {
            when (expression[pos]) {
                '+', '-', '*', '/', '^', '(', ')' -> {
                    currToken = expression[pos]
                }
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> {
                    currToken = 'n'
                    val pos0 = pos
                    while (pos < expression.length && expression[pos].isDigit()) pos++
                    try {
                        currNumber = expression.substring(pos0, pos).toDouble()
                    } catch (e: NumberFormatException) {
                        currToken = 'i'
                    }
                    pos--
                }
                else -> {
                    //out.println("Incorrect symbol at position " + (pos+1));
                    currToken = 'i'
                }
            }
        }
    }

    private fun prim(): Double {
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
                val e = expr()
                getNextToken()
                e
            }
            else -> {
                expr()
            }
        }
    }

    private fun expr(): Double {
        var left = term()
        while (true) {
            when (currToken) {
                '+' -> {
                    getNextToken()
                    left += term()
                }
                '-' -> {
                    getNextToken()
                    left -= term()
                }
                else -> {
                    return left
                }
            }
        }
    }

    private fun term(): Double {
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
                '^' -> {
                    getNextToken()
                    left = Math.pow(left, prim())
                }
                else -> {
                    return left
                }
            }
        }
    }

    private fun checkExpression(): Boolean {
        expression = expression.replace(',', '.')
        expression = expression.replace(" ", "")
        pos = -1
        var res = true
        if (expression.length == 0) res = false
        var prev_token = '('
        getNextToken()
        while (currToken != 'q') {
            if (currToken == 'i') res = false
            when (prev_token) {
                'n' -> {
                    if (currToken == '(') {
                        expression = expression.substring(
                            0,
                            pos
                        ) + '*' + expression.substring(pos)
                        pos++
                    }
                }
                '+', '-', '*', '/', '^', '(' -> {
                    if (currToken == '+' || currToken == '*' || currToken == '/' || currToken == '^') res =
                        false
                }
            }
            prev_token = currToken
            getNextToken()
        }
        if (prev_token == '+' || prev_token == '*' || prev_token == '/' || prev_token == '^' || prev_token == '-' || prev_token == '(') res =
            false
        var braces = 0
        for (i in 0 until expression.length) {
            if (expression[i] == '(') braces++ else if (expression[i] == ')') braces--
        }
        if (braces != 0) res = false
        pos = -1
        return res
    }

    fun solve(expr: String): Double {
        expression = expr
        if (checkExpression()) {
            getNextToken()
            return expr()
        }
        return 0.0
    }
}
