fun main() {
    val list = Array(readLine()!!.toInt()) { readLine()!!.toInt()}
    val m = readLine()!!.toInt()
    println(
        if (list.contains(m)) "YES" else "NO"
    )
}