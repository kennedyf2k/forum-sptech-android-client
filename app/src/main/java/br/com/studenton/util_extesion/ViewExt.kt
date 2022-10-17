package br.com.studenton.util_extesion

import android.view.*
import android.view.animation.Animation
import androidx.annotation.LayoutRes



fun View.startAnimation(anim: Animation, onEnd: () -> Unit) {
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(anim: Animation?) = onEnd()
        override fun onAnimationRepeat(anim: Animation?) = Unit
        override fun onAnimationStart(anim: Animation?) = Unit
    })
    this.startAnimation(anim)
}

fun View.setVisible(show: Boolean) {
    if (show) this.visibility = View.VISIBLE else this.visibility = View.GONE
}


fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)

