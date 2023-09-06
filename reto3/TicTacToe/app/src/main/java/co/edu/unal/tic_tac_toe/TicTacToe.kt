package co.edu.unal.tic_tac_toe

class TicTacToe() {
    private val  person = "PERSON"
    private val robot = "ROBOT"
    private var board: Array<String>
    private var personSymbol ="X"
    private var robotSymbol="O"
    private var turn: String

    init{
        this.board = Array(9) { "-" }
        this.turn = setOf(person,robot).random()
    }

    public fun getTurn(): String{
        return this.turn
    }

    public fun getPerson(): String{
        return this.person
    }

    public fun getrobot(): String{
        return this.robot
    }
    public fun getrobotSymbol(): String {
        return this.robotSymbol
    }
    public fun getPersonSymbol(): String {
        return this.personSymbol
    }
    public fun checkForWinner(): String {
        var i = 0
        // Check rows
        while (i <= 6) {
            if (board[i] === robotSymbol && board[i + 1] === robotSymbol && board[i + 2] === robotSymbol)
                return GameState.ROBOT.name
            if (board[i] === personSymbol && board[i + 1] === personSymbol && board[i + 2] === personSymbol)
                return GameState.PERSON.name
            i += 3
        }

        // Check columns
        for (i in 0..2) {
            if (board[i] === robotSymbol && board[i + 3] === robotSymbol && board[i + 6] === robotSymbol)
                return GameState.ROBOT.name
            if (board[i] === personSymbol && board[i + 3] === personSymbol && board[i + 6] === personSymbol)
                return GameState.PERSON.name
        }

        // Check diagonals
        if (board[0] === robotSymbol && board[4] === robotSymbol && board[8] === robotSymbol ||
            board[2] === robotSymbol && board[4] === robotSymbol && board[6] === robotSymbol)
            return GameState.ROBOT.name
        if (board[0] === personSymbol && board[4] === personSymbol && board[8] === personSymbol ||
            board[2] === personSymbol&& board[4] === personSymbol && board[6] === personSymbol)
            return GameState.PERSON.name

        // Check for tie
        for (i in 0..8) {
            if (board[i] == "-")
                return GameState.GAME_CONTINUES.name
        }
        return GameState.TIE.name
    }

    public fun setPlayerMove(field: Int): Boolean{
        var tileWasChanged: Boolean
        if (this.board[field] == "-") {
            this.board[field] = this.personSymbol
            this.turn = alternateTurn(this.turn)
            tileWasChanged = true
        }else{
            tileWasChanged = false
        }
        return tileWasChanged
    }

    public fun setComputerMove(): Int{
        var move: Int
        // First see if there's a move computer can make to win
        for (i in 0..8) {
            if (this.board[i] !== personSymbol && this.board[i] !== robotSymbol) {
                val curr = this.board[i]
                this.board[i] = this.robotSymbol
                if (checkForWinner() == this.robot) {
                    this.turn = alternateTurn(this.turn)
                    return i
                }
                else
                    this.board[i] = curr
            }
        }
        // See if there's a move computer can make to block X from winning
        for (i in 0..8) {
            if (this.board[i] !== personSymbol && this.board[i] !== robotSymbol) {
                val curr = this.board[i] // Save the current number
                this.board[i] = this.personSymbol
                if (checkForWinner() == this.person) {
                    this.board[i] = this.robotSymbol
                    this.turn = alternateTurn(this.turn)
                    return i
                } else
                    this.board[i] = curr
            }
        }
        // Generate random move
        do {
            move = (0..8).random()
        } while (this.board[move] === personSymbol || this.board[move] === robotSymbol)
        this.board[move] = robotSymbol
        this.turn = alternateTurn(this.turn)
        return move
    }

    public fun newGame(){
        this.board = Array(9) { "-" }
        this.turn = setOf(person,robot).random()
    }

    private fun alternateTurn(turn: String): String{
        return if (turn == person)
            robot
        else
            person
    }

    private enum class GameState{
        GAME_CONTINUES, TIE, PERSON, ROBOT
    }
}