package oliveiradev.com.github.audioplayercontrol.controller

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder
import oliveiradev.com.github.audioplayercontrol.view.AudioPlayerState
import oliveiradev.com.github.audioplayercontrol.view.AudioPlayerView

/**
 * Created by felipe on 10/09/17.
 */
class AudioPlayerController(
    private val audioUrl: String,
    private val audioPlayerView: AudioPlayerView
) : AudioPlayerContract {

    private val mediaPlayer by lazy { MediaPlayer().apply { setDataSource(audioUrl) } }
    private val serviceConnection: ServiceConnection
    private var currentPosition = 0
    private var onPlayerLoadListener: OnPlayerLoadListener? = null
    private var timer: CountDownTimer? = null
    private var wasStarted = false

    init {
        audioPlayerView.setAudioPlayerContract(this)
        configMediaPlayer()
        serviceConnection = object : ServiceConnection {
            override fun onServiceDisconnected(componentName: ComponentName?) {}

            override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {

            }
        }
    }

    override fun play() {
        start()
    }

    override fun playAsService(context: Context) {
        val intent = Intent(context, AudioPlayerService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun pause() {
        timer?.cancel()
        currentPosition = mediaPlayer.currentPosition
        mediaPlayer.pause()
    }

    override fun seekTo(position: Int) {
        currentPosition = position * ONE_SECOND_IN_MILLIS
    }

    private fun configMediaPlayer() {
        mediaPlayer.setOnPreparedListener {
            it.seekTo(currentPosition)
            it.start()
            wasStarted = true
            setupView()
            onPlayerLoadListener?.onAudioPrepared()
        }
        mediaPlayer.setOnErrorListener { _, _, extra ->
            audioPlayerView.setPlayControlState(AudioPlayerState.PAUSE)
            onPlayerLoadListener?.onAudioLoadError(Throwable("Audio error $extra"))
            true
        }
    }

    private fun setupView() = with(audioPlayerView) {
        val duration = getDuration()
        val seconds = duration / ONE_SECOND_IN_MILLIS
        setDuration(duration)
        setProgressSize(seconds)
        starProgressObserver(duration)
    }

    private fun starProgressObserver(progress: Int) {
        timer = object : CountDownTimer(
            progress.toLong(),
            ONE_SECOND_IN_MILLIS.toLong()
        ) {
            override fun onFinish() {
                updateProgress(progress.div(ONE_SECOND_IN_MILLIS))
                audioPlayerView.setPlayControlState(AudioPlayerState.PAUSE)
            }

            override fun onTick(rest: Long) {
                val newProgress = progress.minus(rest)
                updateProgress(newProgress.div(ONE_SECOND_IN_MILLIS).toInt())
            }
        }
        timer?.start()
    }

    private fun updateProgress(duration: Int) = with(audioPlayerView) {
        updateTimer(duration)
        updateProgress(duration)
    }

    private fun start() {
        if (currentPosition > 0 || wasStarted) {
            restart()
        } else if (currentPosition == 0 && wasStarted.not()) {
            mediaPlayer.prepareAsync()
            onPlayerLoadListener?.onAudioLoadingStart()
        }
    }

    private fun restart() {
        mediaPlayer.seekTo(currentPosition)
        mediaPlayer.start()
        starProgressObserver(getDuration().minus(currentPosition))
    }

    fun addPlayerLoadListener(onPlayerLoadListener: OnPlayerLoadListener) {
        this.onPlayerLoadListener = onPlayerLoadListener
    }

    fun stop() {
        mediaPlayer.apply {
            stop()
            release()
        }

        timer?.cancel()
    }

    private fun getDuration(): Int = mediaPlayer.duration

    companion object {
        private const val ONE_SECOND_IN_MILLIS = 1000
    }
}