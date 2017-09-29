package oliveiradev.com.github.audioplayercontrol.controller

/**
 * Created by felipe on 11/09/17.
 */
interface AudioPlayerContract {

    fun play()

    fun pause()

    fun seekTo(position: Int)
}