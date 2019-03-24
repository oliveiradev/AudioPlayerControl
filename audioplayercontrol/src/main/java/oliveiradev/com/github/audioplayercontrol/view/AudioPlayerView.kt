package oliveiradev.com.github.audioplayercontrol.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import oliveiradev.com.github.audioplayercontrol.R
import oliveiradev.com.github.audioplayercontrol.controller.AudioPlayerContract
import oliveiradev.com.github.audioplayercontrol.extension.setOnUserInteractionListener
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by felipe on 10/09/17.
 */
class AudioPlayerView : FrameLayout, SeekBar.OnSeekBarChangeListener {

    private val duration: TextView
    private val timer: TextView
    private val progress: SeekBar
    private val playControl: ToggleButton
    private val durationFormatter = SimpleDateFormat("mm:ss")
    private var audioPlayerContract: AudioPlayerContract? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.AudioPlayerView, 0, 0)

        val autoPlay = typedArray.getBoolean(R.styleable.AudioPlayerView_auto_play, false)
        val customViewReference = typedArray.getResourceId(R.styleable.AudioPlayerView_custom_view, 0)

        typedArray.recycle()

        val layoutToInflate = if (customViewReference != 0) customViewReference
        else R.layout.view_default_audio_player

        LayoutInflater.from(context).inflate(layoutToInflate, this, true)

        val container = getChildAt(0)
        duration = container.findViewById(R.id.duration)
        timer = container.findViewById(R.id.timer)
        progress = container.findViewById(R.id.progress)
        playControl = container.findViewById(R.id.play_control)

        configPlayControl()
        configProgressSeekBar()
        checkAutoPlay(autoPlay)
    }

    override fun onProgressChanged(seekBar: SeekBar?, position: Int, fromUser: Boolean) {
        if (fromUser) audioPlayerContract?.seekTo(position)
        updateTimer(position)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        audioPlayerContract?.pause()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        audioPlayerContract?.playAsService(context)
    }

    private fun configPlayControl() {
        playControl.setOnUserInteractionListener { isChecked ->
            if (isChecked) audioPlayerContract?.playAsService(context)
            else audioPlayerContract?.pause()
        }
    }

    private fun configProgressSeekBar() {
        progress.setOnSeekBarChangeListener(this)
    }

    private fun checkAutoPlay(isAutoPlay: Boolean) {
        if (isAutoPlay) {
            audioPlayerContract?.playAsService(context)
        }
    }

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

    fun setAudioPlayerContract(audioPlayerContract: AudioPlayerContract) {
        this.audioPlayerContract = audioPlayerContract
    }

    private fun getDurationFormatted(duration: Int) = durationFormatter.format(Date(duration.toLong()))
}