package com.makentoshe;
fun gen2_game(hard:Int):String{
    var operations:String = " +-*^%&|"
    var ans:String=""
    if(hard==1){
        val num=(1..4).random()
        for(i in 0..num) {
            ans += (1..9).random()
            if (i != num) {
                var opo:Char= operations[(0..3).random()]
                if(opo!=' ')
                    ans+=opo
            }
        }
        return ans
    }
    if(hard==2){
        val num=(1..7).random()
        for(i in 0..num){
            ans+=(1..9).random()
            if(i!=num) {
                var opo:Char= operations[(0..3).random()]
                if(opo!=' ')
                    ans+=opo
            }
        }
        return ans
    }
    if(hard==3){
        val num=(1..10).random()
        for(i in 0..num) {
            ans += (1..9).random()
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
