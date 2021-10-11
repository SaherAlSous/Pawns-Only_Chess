fun main() {
    val size = readLine()!!.toInt()
    val list = MutableList<Int>(size) { readLine()!!.toInt() }
    var sequence = 0
    var triples = 0
    for (i in 1 until size) {
        if (list[i - 1] - list[i] == -1) sequence++ else sequence = 0
        if (sequence == 2) triples++ else if (sequence > 2) triples = sequence - 1
    }
    println(triples)
}
