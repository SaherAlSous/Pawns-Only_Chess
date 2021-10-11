fun main() {
    val nums = Array(readLine()!!.toInt()) { readLine()!!.toInt() }
        for (digit in 2..5) {
            print("${nums.count{i -> i == digit}} ")
        }
}
