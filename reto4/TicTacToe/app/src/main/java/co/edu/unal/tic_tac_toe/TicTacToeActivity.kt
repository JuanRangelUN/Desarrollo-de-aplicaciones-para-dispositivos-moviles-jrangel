package co.edu.unal.tic_tac_toe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import co.edu.unal.tic_tac_toe.dialogos.dialogoDescripcion
import co.edu.unal.tic_tac_toe.dialogos.dialogoDificultad
import co.edu.unal.tic_tac_toe.dialogos.dialogoSalir


class TicTacToeActivity : AppCompatActivity(),
    dialogoDificultad.dialogoDificultadListenner,
        dialogoSalir.dialogoSalirListener {
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
        setSupportActionBar(findViewById(R.id.toolbar))

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
                button.isEnabled=true
            }
        }

            for ((i, button) in boardButtons.withIndex()) {
                button.setOnClickListener {
                    if (!isGameFinished()) {
                        Log.d("string", "prueba")
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
            }
    }
    override fun onDialogEasyClick(dialog: DialogFragment) {
        ticTacToe.setDifficulty(TicTacToe.Difficulty.FACIL)
    }
    override fun onDialogHardClick(dialog: DialogFragment) {
        ticTacToe.setDifficulty(TicTacToe.Difficulty.DIFICIL)
    }

    override fun onDialogExpertClick(dialog: DialogFragment) {
        ticTacToe.setDifficulty(TicTacToe.Difficulty.EXPERTO)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        this.finish()
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
                boardButtons[0].isEnabled=false
                boardButtons[1].isEnabled=false
                boardButtons[2].isEnabled=false
                boardButtons[3].isEnabled=false
                boardButtons[4].isEnabled=false
                boardButtons[5].isEnabled=false
                boardButtons[6].isEnabled=false
                boardButtons[7].isEnabled=false
                boardButtons[8].isEnabled=false
            }
            "TIE" -> {
                gameStateText.text = "It's a tie!"
                scoreBoard[1] = scoreBoard[1]+1
                boardButtons[0].isEnabled=false
                boardButtons[1].isEnabled=false
                boardButtons[2].isEnabled=false
                boardButtons[3].isEnabled=false
                boardButtons[4].isEnabled=false
                boardButtons[5].isEnabled=false
                boardButtons[6].isEnabled=false
                boardButtons[7].isEnabled=false
                boardButtons[8].isEnabled=false
            }
            ticTacToe.getrobot() -> {
                gameStateText.text = "robot Wins!"
                scoreBoard[2] = scoreBoard[2]+1
                boardButtons[0].isEnabled=false
                boardButtons[1].isEnabled=false
                boardButtons[2].isEnabled=false
                boardButtons[3].isEnabled=false
                boardButtons[4].isEnabled=false
                boardButtons[5].isEnabled=false
                boardButtons[6].isEnabled=false
                boardButtons[7].isEnabled=false
                boardButtons[8].isEnabled=false
            }
            else -> {
                gameStateText.text = " "
            }
        }
        loadTurnText()
        loadScoreBoardText()
        return result == ticTacToe.getPerson() || result == "TIE" || result == "ROBOT"
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.opciones, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.new_game -> {
                ticTacToe.newGame()
                loadTurnText()
                for (button in boardButtons) {
                    button.text = "-"
                }
                gameStateText.text = " "
                return true
            }
            R.id.ai_difficulty -> {
                val difficultyDialog = dialogoDificultad()
                difficultyDialog.setCurrentDifficulty(ticTacToe.getDifficulty())
                difficultyDialog.show(supportFragmentManager,"difficulty")
                return true
            }
            R.id.quit -> {
                val quitDialog = dialogoSalir()
                quitDialog.show(supportFragmentManager, "quit")
                return true
            }
            R.id.about -> {
                val aboutDialog = dialogoDescripcion()
                aboutDialog.show(supportFragmentManager, "about")
            }
        }
        return false
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