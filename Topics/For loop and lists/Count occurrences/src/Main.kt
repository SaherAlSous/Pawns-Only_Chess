fun main() {
    val numbers = Array(readLine()!!.toInt()) { readLine()!!.toInt()}
    val m = readLine()!!.toInt()
    var counter = 0
    for(num in numbers) {
        if(num == m) counter++
    }
    println(counter)
}
