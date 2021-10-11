fun main() {
    println(readLine()!!.split(" ").toList().maxByOrNull { it.length }!!)
}