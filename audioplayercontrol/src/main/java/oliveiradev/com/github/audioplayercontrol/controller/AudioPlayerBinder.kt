package oliveiradev.com.github.audioplayercontrol.controller

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder

internal class AudioPlayerBinder : Binder() {

    private var mediaPlayer: MediaPlayer? = null

    private fun playerSetup(path: String): MediaPlayer? {
        return if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(path)
                prepare()
            }
        } else {
             mediaPlayer
        }
    }

    fun start(path: String) {
        playerSetup(path)?.also {
            it.start()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }
}