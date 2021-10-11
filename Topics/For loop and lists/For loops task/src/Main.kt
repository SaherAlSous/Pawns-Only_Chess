fun main() {
    val size = readLine()!!.toInt()
    val list = MutableList<Int>(size) { readLine()!!.toInt() }
    var (p, m) = readLine()!!.trim().split("\\s+".toRegex()).map (String::toInt)
    val result = p in list && m in list
    println(if (result)"YES" else "NO")
}
