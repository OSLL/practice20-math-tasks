package com.makentoshe;

public class Calculator {

    private static String expression;
    private static int pos = -1;
    private static double curr_number;
    private static char curr_token;

    private static boolean isCharNumeric(char ch) {
        return (ch >= '0' && ch <= '9' || ch == '.' || ch == ',');
    }

    private static void get_next_token() {
        pos++;
        if(pos >= expression.length()) {
            curr_token = 'q';
        } else {
            switch(expression.charAt(pos)) {
                case '+': case '-': case '*': case '/': case '^': case '(': case ')': {
                    curr_token = expression.charAt(pos);
                    break;
                }
                case '0': case '1': case '2': case '3': case '4': case '5':
                case '6': case '7': case '8': case '9': case '.': {
                    curr_token = 'n';
                    int pos0 = pos;
                    while(pos < expression.length() && isCharNumeric(expression.charAt(pos)))
                        pos++;
                    try {
                        curr_number = Double.parseDouble(expression.substring(pos0, pos));
                    } catch (NumberFormatException e) {
                        curr_token = 'i';
                    }
                    pos--;
                    break;
                }
                default: {
                    //out.println("Incorrect symbol at position " + (pos+1));
                    curr_token = 'i';
                }
            }
        }
    }

    private static double prim() {
        switch (curr_token) {
            case 'n': {
                get_next_token();
                return curr_number;
            }
            case '-': {
                get_next_token();
                return -prim();
            }
            case '(': {
                get_next_token();
                double e = expr();
                get_next_token();
                return e;
            }
            default: {
                return expr();
            }
        }
    }

    private static double expr() {
        double left = term();
        while(true) {
            switch (curr_token) {
                case '+': {
                    get_next_token();
                    left += term();
                    break;
                }
                case '-': {
                    get_next_token();
                    left -= term();
                    break;
                }
                default: {
                    return left;
                }
            }
        }
    }

    private static double term() {
        double left = prim();
        while(true) {
            switch (curr_token) {
                case '*': {
                    get_next_token();
                    left *= prim();
                    break;
                }
                case '/': {
                    get_next_token();
                    left /= prim();
                    break;
                }
                case '^': {
                    get_next_token();
                    left = Math.pow(left, prim());
                    break;
                }
                default: {
                    return left;
                }
            }
        }
    }

    private static boolean check_expression() {
        expression = expression.replace(',', '.');
        expression = expression.replaceAll(" ", "");

        pos = -1;
        boolean res = true;

        if(expression.length() == 0)
            res = false;

        char prev_token = '(';
        get_next_token();
        while(curr_token != 'q') {
            if(curr_token == 'i')
                res = false;
            switch(prev_token) {
                case 'n': {
                    if(curr_token == '(') {
                        expression = expression.substring(0, pos) + '*' + expression.substring(pos);
                        pos++;
                    }
                    break;
                }
                case '+': case '-': case '*': case '/': case '^': case '(': {
                    if(curr_token == '+' || curr_token == '*' || curr_token == '/' || curr_token == '^')
                        res = false;
                    break;
                }
            }
            prev_token = curr_token;
            get_next_token();
        }
        if(prev_token == '+' || prev_token == '*' || prev_token == '/' || prev_token == '^' || prev_token == '-' || prev_token == '(')
            res = false;
        int braces = 0;
        for(int i = 0; i < expression.length(); ++i) {
            if(expression.charAt(i) == '(')
                braces++;
            else if(expression.charAt(i) == ')')
                braces--;
        }
        if(braces != 0)
            res = false;
        return res;
    }

    public static double calculate(String expr) {
        expression = expr;
        if(check_expression()) {
            pos = -1;
            get_next_token();
            return expr();
        }
        return 0;
    }

    private Calculator() {}
}
