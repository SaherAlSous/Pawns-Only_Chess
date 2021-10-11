fun main() {
    val n = readLine()!!.toInt()
    var output = 0
    repeat(n){
        if(readLine()!!.toInt() > 0) output++
    }
    println(output)
}
