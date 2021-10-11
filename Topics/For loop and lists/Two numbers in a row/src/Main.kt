import kotlin.math.abs

fun main() {
    val numArray = Array(readLine()!!.toInt()) { readLine()!!.toInt() }
    val nums =  readLine()!!.split(" ").map { it.toInt() }
    var resultList = "YES"
     for (i in numArray.lastIndex downTo  1) {
            if (numArray[i] in nums && numArray[i - 1] in nums)
                resultList = "NO"
         break
        }
    println(resultList)
}