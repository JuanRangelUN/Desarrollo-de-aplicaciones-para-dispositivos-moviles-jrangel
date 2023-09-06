package co.edu.unal.tic_tac_toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TicTacToeActivity : AppCompatActivity() {
    private lateinit var ticTacToe: TicTacToe
    private lateinit var turnText: TextView
    private lateinit var OWinsText: TextView
    private lateinit var XWinsText: TextView
    private lateinit var tiesText: TextView
    private lateinit var gameStateText: TextView
    private lateinit var newGameButton: Button
    private lateinit var boardButtons: ArrayList<Button>
    private lateinit var scoreBoard: Array<Int>
    private lateinit var symbols: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)



        ticTacToe = TicTacToe()
        turnText = findViewById<TextView>(R.id.turn)
        OWinsText = findViewById<TextView>(R.id.o_wins)
        XWinsText = findViewById<TextView>(R.id.x_wins)
        tiesText = findViewById<TextView>(R.id.ties)
        gameStateText = findViewById<TextView>(R.id.game_status)
        newGameButton = findViewById<Button>(R.id.new_game)
        boardButtons = arrayListOf<Button>()
        boardButtons.add(findViewById(R.id.tile0))
        boardButtons.add(findViewById(R.id.tile1))
        boardButtons.add(findViewById(R.id.tile2))
        boardButtons.add(findViewById(R.id.tile3))
        boardButtons.add(findViewById(R.id.tile4))
        boardButtons.add(findViewById(R.id.tile5))
        boardButtons.add(findViewById(R.id.tile6))
        boardButtons.add(findViewById(R.id.tile7))
        boardButtons.add(findViewById(R.id.tile8))
        scoreBoard = arrayOf(0,0,0)
        symbols = arrayOf(ticTacToe.getPersonSymbol(),ticTacToe.getrobotSymbol())

        setInitialText()

        if((ticTacToe.getTurn() == ticTacToe.getrobot()) && !isGameFinished()){
            var computerTile = ticTacToe.setComputerMove()
            boardButtons[computerTile].text = ticTacToe.getrobotSymbol()
            isGameFinished()
        }

        newGameButton.setOnClickListener{
            ticTacToe.newGame()
            loadTurnText()
            for (button in boardButtons) {
                button.text = "-"
            }
            gameStateText.text = " "
            boardButtons[0].isClickable=true
            boardButtons[1].isClickable=true
            boardButtons[2].isClickable=true
            boardButtons[3].isClickable=true
            boardButtons[4].isClickable=true
            boardButtons[5].isClickable=true
            boardButtons[6].isClickable=true
            boardButtons[7].isClickable=true
            boardButtons[8].isClickable=true
        }

        for ((i, button) in boardButtons.withIndex()) {
            if (!isGameFinished()) {
                button.setOnClickListener {
                    var computerTile = 0
                    var actualTurn = ticTacToe.getTurn()
                    if (actualTurn == ticTacToe.getPerson()) {
                        val tileWasChanged = ticTacToe.setPlayerMove(i)
                        if (tileWasChanged) {
                            button.text = ticTacToe.getPersonSymbol()
                            actualTurn = ticTacToe.getTurn()
                            val isGameFinished = isGameFinished()
                            if (!isGameFinished) {
                                computerTile = ticTacToe.setComputerMove()
                                boardButtons[computerTile].text = ticTacToe.getrobotSymbol()
                                isGameFinished()
                            }
                        }
                    } else {
                        computerTile = ticTacToe.setComputerMove()
                        boardButtons[computerTile].text = ticTacToe.getrobotSymbol()
                        isGameFinished()
                    }
                }
            }
            else{
                boardButtons.withIndex()
                boardButtons[1].isClickable=false
                boardButtons[2].isClickable=false
                boardButtons[3].isClickable=false
                boardButtons[4].isClickable=false
                boardButtons[5].isClickable=false
                boardButtons[6].isClickable=false
                boardButtons[7].isClickable=false
                boardButtons[8].isClickable=false
            }
        }
    }

    private fun setInitialText(){
        loadTurnText()
        loadScoreBoardText()
        isGameFinished()
    }

    private fun isGameFinished(): Boolean{
        var result = ticTacToe.checkForWinner()
        when(result){
            ticTacToe.getPerson() -> {
                gameStateText.text = "Person Wins!"
                scoreBoard[0] = scoreBoard[0]+1
            }
            "TIE" -> {
                gameStateText.text = "It's a tie!"
                scoreBoard[1] = scoreBoard[1]+1
            }
            ticTacToe.getrobot() -> {
                gameStateText.text = "robot Wins!"
                scoreBoard[2] = scoreBoard[2]+1
            }
            else -> {
                gameStateText.text = " "
            }
        }
        loadTurnText()
        loadScoreBoardText()
        return result == "PESRON" || result == "TIE" || result == "ROBOT"
    }

    private fun loadTurnText(){
        var turn = ticTacToe.getTurn()
        turnText.text = "Actual turn: $turn"
    }

    private fun loadScoreBoardText(){
        var Wins1 = scoreBoard[0].toString()
        var ties = scoreBoard[1].toString()
        var Wins2 = scoreBoard[2].toString()
        XWinsText.text = "Person: $Wins1"
        tiesText.text = "Ties: $ties"
        OWinsText.text = "robot: $Wins2"
    }

}