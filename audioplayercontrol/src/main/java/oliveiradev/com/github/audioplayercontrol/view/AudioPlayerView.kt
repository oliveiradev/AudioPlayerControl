package oliveiradev.com.github.audioplayercontrol.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import oliveiradev.com.github.audioplayercontrol.R
import oliveiradev.com.github.audioplayercontrol.controller.AudioPlayerController
import oliveiradev.com.github.audioplayercontrol.extension.setOnUserInteractionListener
import java.text.SimpleDateFormat

/**
 * Created by felipe on 10/09/17.
 */
class AudioPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SeekBar.OnSeekBarChangeListener {

    private val playerController by lazy { AudioPlayerController() }
    private val duration: TextView
    private val timer: TextView
    private val progress: SeekBar
    private val playControl: ToggleButton
    private val durationFormatter = SimpleDateFormat("mm:ss")
    private var audioPath = ""

    init {
        inflate(context, R.layout.view_default_audio_player, this)

        duration = findViewById(R.id.duration)
        timer = findViewById(R.id.timer)
        progress = findViewById(R.id.progress)
        playControl = findViewById(R.id.play_control)

        playerController.playerSetup(context)
        configPlayControl()
        configProgressSeekBar()
    }

    override fun onProgressChanged(seekBar: SeekBar?, position: Int, fromUser: Boolean) {
//        if (fromUser) audioPlayerContract?.seekTo(position)
//        updateTimer(position)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
//        audioPlayerContract?.pause()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//        audioPlayerContract?.setAudioPath(context)
    }

    private fun configPlayControl() {
        playControl.setOnUserInteractionListener { isChecked ->
            if (isChecked) playerController.play(audioPath)
            else playerController.pause()
        }
    }

    private fun configProgressSeekBar() {
        progress.setOnSeekBarChangeListener(this)
    }

/*
    fun setPlayControlState(playerState: AudioPlayerState) {
        playControl.isChecked = playerState == AudioPlayerState.PLAY
    }

    fun setDuration(duration: Int) {
        this.duration.text = getDurationFormatted(duration)
    }

    fun setProgressSize(size: Int) {
        progress.max = size
    }

    fun updateTimer(duration: Int) {
        timer.text = getDurationFormatted(duration * 1000)
    }

    fun updateProgress(newSize: Int) {
        progress.progress = newSize
    }

    fun setAudioPlayerContract(audioPlayerContract: AudioPlayerCommand) {
//        this.audioPlayerContract = audioPlayerContract
    }*/

    fun setAudioPath(path: String) {
        this.audioPath = path
    }

//    private fun getDurationFormatted(duration: Int) = durationFormatter.format(Date(duration.toLong()))
}