package co.edu.unal.tic_tac_toe.dialogos

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import co.edu.unal.tic_tac_toe.R
import co.edu.unal.tic_tac_toe.TicTacToe

class dialogoDificultad: DialogFragment() {
    private lateinit var listener: dialogoDificultadListenner
    private lateinit var currentDifficulty: TicTacToe.Difficulty

    interface dialogoDificultadListenner {
        fun onDialogEasyClick(dialog: DialogFragment)
        fun onDialogHardClick(dialog: DialogFragment)
        fun onDialogExpertClick(dialog: DialogFragment)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as dialogoDificultadListenner
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement dialogoDificultadListenner "))
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.choose_difficulty)
                .setSingleChoiceItems(R.array.difficulty,
                    getCurrentDifficultyAsInt()){ dialogInterface, i ->
                    when(i){
                        0 -> listener.onDialogEasyClick(this)
                        1 -> listener.onDialogHardClick(this)
                        2 -> listener.onDialogExpertClick(this)
                    }
                    dialogInterface.dismiss()
                    Toast.makeText(activity,
                        "Current difficulty: "+resources.getStringArray(R.array.difficulty)[i],
                        Toast.LENGTH_LONG).show()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    fun setCurrentDifficulty(difficulty: TicTacToe.Difficulty){
        this.currentDifficulty = difficulty
    }
    private fun getCurrentDifficultyAsInt(): Int{
        return when(currentDifficulty){
            TicTacToe.Difficulty.FACIL -> 0
            TicTacToe.Difficulty.DIFICIL-> 1
            TicTacToe.Difficulty.EXPERTO -> 2
        }
    }
}