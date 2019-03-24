package oliveiradev.com.github.audioplayercontrol.controller

import android.content.Context

/**
 * Created by felipe on 11/09/17.
 */
interface AudioPlayerContract {

    fun play()
    fun playAsService(context: Context)
    fun pause()
    fun seekTo(position: Int)
}