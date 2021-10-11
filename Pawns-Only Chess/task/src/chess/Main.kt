package chess

import kotlin.math.abs
import kotlin.system.exitProcess

data class Pawns(
    val White: String = "| W ",
    val Black: String = "| B ",
    val Empty: String = "|   ",
    val Size: Int = 8,
    val exit: String = "exit"
)

var firstPlayerTurn = true
private val regex = Regex("[a-h][1-8][a-h][1-8]")
var whitesLocation: MutableList<MutableList<Int>> = mutableListOf()
var blacksLocation: MutableList<MutableList<Int>> = mutableListOf()
var chessBoard: MutableList<MutableList<String>> = mutableListOf()
var lastMove = listOf(-20, -20)

fun main() {
    val pawns = Pawns()
    initializeGame(pawns)
}

fun initializeGame(pawns: Pawns) {
    chessBoard = MutableList(pawns.Size) { MutableList(pawns.Size) { pawns.Empty } }
    chessBoard[1] = MutableList(pawns.Size) { pawns.Black }
    blacksLocation = mutableListOf(mutableListOf(1, 0), mutableListOf(1, 1), mutableListOf(1, 2), mutableListOf(1, 3), mutableListOf(1, 4), mutableListOf(1, 5), mutableListOf(1, 6), mutableListOf(1, 7))
    chessBoard[6] = MutableList(pawns.Size) { pawns.White }
    whitesLocation = mutableListOf(mutableListOf(6, 0), mutableListOf(6, 1), mutableListOf(6, 2), mutableListOf(6, 3), mutableListOf(6, 4), mutableListOf(6, 5), mutableListOf(6, 6), mutableListOf(6, 7))
    println("Pawns-Only Chess")
    getPlayersNames(chessBoard, pawns)
}

fun getPlayersNames(chessBoard: MutableList<MutableList<String>>, pawns: Pawns) {
    println("First Player's name:")
    val firstPlayer = readLine()!!
    println("Second Player's name:")
    val secondPlayer = readLine()!!
    printBoard(chessBoard, pawns)
    playersMoves(firstPlayer, secondPlayer, pawns)
}

fun playersMoves(firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    println((if (firstPlayerTurn) firstPlayer else secondPlayer) + "'s turn:")
    checkCommand(readLine()!!, firstPlayer, secondPlayer, pawns)
}

fun checkCommand(input: String, firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    if (input == pawns.exit) stop() else moveOrRepeat(input, firstPlayer, secondPlayer, pawns)
}

fun stop() {
    println("Bye!")
    exitProcess(0)
}

fun moveOrRepeat(input: String, firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    while (input != pawns.exit) {
        if (input.matches(regex)) {
            moveOrNot(input, firstPlayer, secondPlayer, pawns)
        } else {
            println("Invalid Input")
            playersMoves(firstPlayer, secondPlayer, pawns)
        }
    }
}

fun moveOrNot(input: String, firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    val startPosition = getPosition(input, 0)
    val endPosition = getPosition(input, 2)
    checkStartPosition(startPosition, input, firstPlayer, secondPlayer, pawns)
    checkSteps(startPosition, endPosition, firstPlayer, secondPlayer, pawns)
    checkDiagonal(startPosition,endPosition,firstPlayer, secondPlayer, pawns)
    movePawns(startPosition, endPosition, firstPlayer, secondPlayer, pawns)
}

fun movePawns(startPosition: MutableList<Int>, endPosition: MutableList<Int>, firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    chessBoard[startPosition[0]][startPosition[1]] = pawns.Empty
    if (firstPlayerTurn) {
        whitesLocation.remove(startPosition)
        chessBoard[endPosition[0]][endPosition[1]] = pawns.White
        whitesLocation.add(endPosition)
    } else {
        blacksLocation.remove(startPosition)
        chessBoard[endPosition[0]][endPosition[1]] = pawns.Black
        blacksLocation.add(endPosition)
    }
    captureOrNot(endPosition, pawns)
    lastMove = endPosition
    firstPlayerTurn = !firstPlayerTurn
    checkStatus(firstPlayer, secondPlayer, pawns)
}

fun checkStatus(firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    if (chessBoard[0].contains(pawns.White) || allCaptured(pawns, false)) {
        printBoard(chessBoard, pawns)
        println("White Wins!")
        stop()
    } else if (chessBoard[7].contains(pawns.Black) || allCaptured(pawns, true)) {
        printBoard(chessBoard, pawns)
        println("Black Wins!")
        stop()
    } else if (stalemateWhite(pawns) || stalemateBlack(pawns)) {
        printBoard(chessBoard, pawns)
        println("Stalemate!")
        stop()
    } else {
        printBoard(chessBoard, pawns)
        playersMoves(firstPlayer, secondPlayer, pawns)
    }
}

fun stalemateBlack(gamePawns: Pawns): Boolean {
    for (rank in 0..7) {
        for (file in 0..7) {
            if (chessBoard[rank][file] == gamePawns.Black) {
                if (file == 0) {
                    if (chessBoard[rank + 1][file] == gamePawns.Empty || chessBoard[rank + 1][file + 1] == gamePawns.White) return false
                } else if (file in 1..6) {
                    if (chessBoard[rank + 1][file] == gamePawns.Empty || chessBoard[rank + 1][file + 1] == gamePawns.White || chessBoard[rank + 1][file - 1] == gamePawns.White) return false
                } else if (file == 7) {
                    if (chessBoard[rank + 1][file] == gamePawns.Empty ||  chessBoard[rank + 1][file - 1] == gamePawns.White) return false
                }
            }
        }
    }
    return true
}

fun stalemateWhite(gamePawns: Pawns): Boolean {
    for (rank in 0..7) {
        for (file in 0..7) {
            if (chessBoard[rank][file] == gamePawns.White) {
                if (file == 0) {
                    if (chessBoard[rank - 1][file] == gamePawns.Empty || chessBoard[rank - 1][file + 1] == gamePawns.Black) return false
                } else if (file in 1..6) {
                    if (chessBoard[rank - 1][file] == gamePawns.Empty || chessBoard[rank - 1][file + 1] == gamePawns.Black || chessBoard[rank + 1][file - 1] == gamePawns.Black) return false
                }else if (file == 7) {
                    if (chessBoard[rank - 1][file] == gamePawns.Empty ||  chessBoard[rank - 1][file - 1] == gamePawns.Black) return false
                }
            }
        }
    }
    return true
}

fun allCaptured(pawns: Pawns, b: Boolean): Boolean {
    val pawn = if (b) pawns.White else pawns.Black
    for (rank in 0..7) {
        for (file in 0..7) {
            if (chessBoard[rank][file] == pawn) return false
        }
    }
    return true
}

fun captureOrNot(endPosition: MutableList<Int>, pawns: Pawns) {
    if (firstPlayerTurn) {
        if (blacksLocation.contains(endPosition)) {
            blacksLocation.remove(endPosition)
        }
        if (endPosition[0] - lastMove[0] == -1 && endPosition[1] - lastMove[1] == 0) {
            chessBoard[lastMove[0]][lastMove[1]] = pawns.Empty
            blacksLocation.remove(lastMove)
        }
    } else {
        if (whitesLocation.contains(endPosition)) {
            whitesLocation.remove(endPosition)
        }
        if (endPosition[0] - lastMove[0] == 1 && endPosition[1] - lastMove[1] == 0) {
            chessBoard[lastMove[0]][lastMove[1]] = pawns.Empty
            whitesLocation.remove(lastMove)
        }
    }
}

fun checkSteps(startPosition: MutableList<Int>, endPosition: MutableList<Int>, firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    val startP = startPosition[0].toString().toInt()
    val endP = endPosition[0].toString().toInt()
    val forwardCapture = if (firstPlayerTurn) startPosition[1] == endPosition[1] && blacksLocation.contains(endPosition) else startPosition[1] == endPosition[1] && whitesLocation.contains(endPosition)
    if (firstPlayerTurn) {
        if (startP - endP > 2 || startP != 6 && startP - endP > 1 || startP - endP <= 0 || forwardCapture || abs(startPosition[1] - endPosition[1]) > 1) {
            println("Invalid Input")
            playersMoves(firstPlayer, secondPlayer, pawns)
        }
    } else {
        if (endP - startP > 2 || startP != 1 && endP - startP > 1 || endP - startP == 0 || endP - startP < 0 || forwardCapture || abs(startPosition[1] - endPosition[1]) > 1) {
            println("Invalid Input")
            playersMoves(firstPlayer, secondPlayer, pawns)
        }
    }
}

fun checkDiagonal(startPosition: MutableList<Int>, endPosition: MutableList<Int>, firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    val enPas = if (abs(endPosition[0] - lastMove[0]) == 1 && abs(endPosition[1] - lastMove[1]) == 0) false
    else if (abs(endPosition[0] - lastMove[0]) != 1 && abs(endPosition[1] - lastMove[1]) == 0) true
    else true
    if (startPosition[1] != endPosition[1] && chessBoard[endPosition[0]][endPosition[1]] == pawns.Empty && enPas ) {
        println("Invalid Input")
        playersMoves(firstPlayer, secondPlayer, pawns)
    }
}

fun checkStartPosition(startPosition: MutableList<Int>, input: String, firstPlayer: String, secondPlayer: String, pawns: Pawns) {
    if (firstPlayerTurn) {
        if (!whitesLocation.contains(startPosition)) {
            println("No white pawn at ${input.substring(0, 2)}")
            playersMoves(firstPlayer, secondPlayer, pawns)
        }
    } else {
        if (!blacksLocation.contains(startPosition)) {
            println("No black pawn at ${input.substring(0, 2)}")
            playersMoves(firstPlayer, secondPlayer, pawns)
        }
    }
}

fun getPosition(input: String, start: Int): MutableList<Int> {
    val file = when (input[start]) {
        'a' -> 0
        'b' -> 1
        'c' -> 2
        'd' -> 3
        'e' -> 4
        'f' -> 5
        'g' -> 6
        else -> 7
    }
    val rank = when (input[start + 1]) {
        '1' -> 7
        '2' -> 6
        '3' -> 5
        '4' -> 4
        '5' -> 3
        '6' -> 2
        '7' -> 1
        else -> 0
    }
    return mutableListOf(rank, file)
}

fun printBoard(chessBoard: MutableList<MutableList<String>>, pawns: Pawns) {
    var rank = pawns.Size
    for (line in chessBoard) {
        println("  ${"+---".repeat(pawns.Size)}+")
        println("$rank ${line.joinToString("")}|")
        rank--
    }
    println("  ${"+---".repeat(pawns.Size)}+\n    a   b   c   d   e   f   g   h")
}