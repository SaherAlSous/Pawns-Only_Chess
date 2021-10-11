fun main() {
    val list = Array(readLine()!!.toInt()) { readLine()!!.toInt() }
    println(list.indexOf(list.maxOrNull()))
}
