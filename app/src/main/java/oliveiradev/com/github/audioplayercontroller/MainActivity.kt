package oliveiradev.com.github.audioplayercontroller

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import oliveiradev.com.github.audioplayercontrol.controller.OnPlayerLoadListener
import oliveiradev.com.github.audioplayercontrol.view.AudioPlayerView

class MainActivity : AppCompatActivity(), OnPlayerLoadListener {

    private val audioPlayerView by lazy { findViewById<AudioPlayerView>(R.id.player) }
    private val progressBar by lazy { ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.setMessage(getString(R.string.loading))

        audioPlayerView.setAudioPath(BuildConfig.SOME_AUDIO_URL)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onAudioLoadingStart() {
        progressBar.show()
    }

    override fun onAudioPrepared() {
        progressBar.dismiss()
    }

    override fun onAudioLoadError(throwable: Throwable) {
        progressBar.dismiss()
        Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
    }
}
