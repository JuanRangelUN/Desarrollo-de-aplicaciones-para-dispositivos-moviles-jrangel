package co.edu.unal.tic_tac_toe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
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
    private lateinit var gridView: GridView
    private lateinit var turnText: TextView
    private lateinit var OWinsText: TextView
    private lateinit var XWinsText: TextView
    private lateinit var tiesText: TextView
    private lateinit var gameStateText: TextView
    private lateinit var newGameButton: Button
    private lateinit var boardButtons: ArrayList<Button>
    private lateinit var scoreBoard: Array<Int>
    private lateinit var symbols: Array<String>
    private lateinit var xSound: MediaPlayer
    private lateinit var oSound: MediaPlayer


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
        gridView = findViewById<GridView>(R.id.grid)
        scoreBoard = arrayOf(0,0,0)
        symbols = arrayOf(ticTacToe.getPersonSymbol(),ticTacToe.getrobotSymbol())
        gridView.setTicTacToe(ticTacToe)
        gridView.setOnTouchListener { _ , motionEvent -> touchGridEvent(motionEvent) }

        setInitialText()
        firstComputerMove()
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

    override fun onResume() {
        super.onResume()
        xSound = MediaPlayer.create(applicationContext,R.raw.xsound)
        oSound = MediaPlayer.create(applicationContext,R.raw.osound)
    }

    override fun onPause() {
        super.onPause()
        xSound.release()
        oSound.release()
    }
    private fun isGameFinished(): Boolean{
        var result = ticTacToe.checkForWinner()
        when(result){
            ticTacToe.getPerson() -> {
                gameStateText.text = "Person Wins!"
                scoreBoard[0] = scoreBoard[0]+1
                gridView.isEnabled = false

            }
            "TIE" -> {
                gameStateText.text = "It's a tie!"
                scoreBoard[1] = scoreBoard[1]+1
                gridView.isEnabled = false
            }
            ticTacToe.getrobot() -> {
                gameStateText.text = "robot Wins!"
                scoreBoard[2] = scoreBoard[2]+1
                gridView.isEnabled = false
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {when(item.itemId){
        R.id.new_game -> {
            ticTacToe.newGame()
            loadTurnText()
            gridView.invalidate()
            gameStateText.text = " "
            gridView.isEnabled = true
            if(  ticTacToe.getTurn()==ticTacToe.getrobot()){
                setComputerMove()
            }
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
    private fun setPlayerMove(field: Int): Boolean{
        xSound.start()
        return if(ticTacToe.setPlayerMove(field)){
            gridView.invalidate()
            true
        }else{
            false
        }
    }

    private fun setComputerMove() {
        gridView.isEnabled = false
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            oSound.start()
            ticTacToe.setComputerMove()
            isGameFinished()
            gridView.isEnabled = true
        }, 1000)
    }

    private fun touchGridEvent(event: MotionEvent?): Boolean {
        if(!isGameFinished()){
            val xCoordinate = event?.x?.toInt()
            val yCoordinate = event?.y?.toInt()

            if (xCoordinate != null && yCoordinate != null) {
                val column = xCoordinate / gridView.getTileWidth()
                val row = yCoordinate / gridView.getTileHeight()
                val tilePosition = row * 3 + column
                    if (ticTacToe.getTurn() == ticTacToe.getPerson()) {
                        val tileWasChanged = setPlayerMove(tilePosition)
                        if (tileWasChanged) {
                            val isGameFinished = isGameFinished()
                            if (!isGameFinished) {
                                setComputerMove()
                            }
                        }
                    } else {
                        setComputerMove()
                    }
                }
            }

        return true
    }

    private fun firstComputerMove(){
        if( ticTacToe.getTurn()==ticTacToe.getrobotSymbol()){
            setComputerMove()
        }
    }

}