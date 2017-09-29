package oliveiradev.com.github.audioplayercontrol.controller

/**
 * Created by felipe on 22/09/17.
 */
interface OnPlayerLoadListener {

    fun onAudioLoadingStart()
    fun onAudioPrepared()
    fun onAudioLoadError(throwable: Throwable)
}