@file:JvmName("AudioPlayerViewHelper")

package oliveiradev.com.github.audioplayercontrol.extension

import android.view.MotionEvent
import android.widget.CompoundButton

/**
 * Created by felipe on 22/09/17.
 */
fun CompoundButton.setOnUserInteractionListener(isChecked: (Boolean) -> Unit) {
    this.setOnTouchListener { view, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            val state = (view as CompoundButton).isChecked.not()
            isChecked.invoke(state)
            this.isChecked = state
        }

        true
    }
}