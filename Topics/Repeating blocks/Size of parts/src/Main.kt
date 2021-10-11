fun main() {
    val n = readLine()!!.toInt()
    var larger = 0
    var reject = 0
    var perfect = 0
    repeat(n) {
        val input = readLine()!!.toInt()
        if (input == -1) reject++ else if (input == 0) perfect++ else larger++
    }
    print("$perfect $larger $reject")
}
