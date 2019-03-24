package oliveiradev.com.github.audioplayercontrol.controller

/**
 * Created by felipe on 11/09/17.
 */
interface AudioPlayerCommand {

    fun play(path: String)
    fun pause()
    fun seekTo(position: Int)
}