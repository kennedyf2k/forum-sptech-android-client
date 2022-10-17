package br.com.studenton.util_extesion

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.crypto.Cipher

/** EXIBE UMA MENSAGEM SIMPLES TEMPORARIZADA NA TELA DO CELULAR */
fun Fragment.toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

/** EXIBE UMA MENSAGEM SIMPLES TEMPORARIZADA POREM RETANGULAR. TAMBÉM PODE DAR A OPCÃO DE ADICIONAR ACÕES */
fun Fragment.snake(view: View, msg: String) = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

/** DISPARA UMA ACTIVITY PARTINDO DE UM FRAGMENT */
fun Fragment.startActivity(clazz: Class<*>, name: String = "", args: Bundle = Bundle()) {
    val intent = Intent(requireContext(), clazz).apply {
        if (!(name.isNotEmpty() && args.isEmpty)) {
            putExtra(name, args)
        }
    }
    requireActivity().startActivity(intent)
}

fun Fragment.showYoutubeVideo(videoId: String) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse("https://youtu.be/$videoId")
    startActivity(openURL)
}

/** VERIFICA SE A PERMISSÃO FOI CONCEDIDA: https://youtu.be/grYUKZDTzVA */
fun Fragment.hasPermission(permission: String): Boolean {
    val permissionCheckResult = ContextCompat.checkSelfPermission(requireContext(), permission)
    return PackageManager.PERMISSION_GRANTED == permissionCheckResult
}

fun Fragment.getDrawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), id)
}

/** VERIFICA SE DEVE SOLICITAR AS PERMISSÕES NOVAMENTE: https://youtu.be/grYUKZDTzVA */
fun Fragment.shouldRequestPermission(permissions: Array<String>): Boolean {
    val grantedPermissions = mutableListOf<Boolean>()
    permissions.forEach { permission ->
        grantedPermissions.add(hasPermission(permission))
    }
    return grantedPermissions.any { granted -> !granted }
}

fun Fragment.inputMethodManager() =
    context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager


/** VERIFICA SE TEM REDE E SE TEM ACESSO A INTERNET: https://youtu.be/DpyxLwibE0M  */
fun Fragment.hasInternet(): Boolean {
    val connMgr =
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
        capabilities != null &&
                // verifica se você tem rede ex: WIFI etc.
                capabilities.hasCapability(NET_CAPABILITY_INTERNET) &&
                // e realmetne consegue fazer requisições, pois em alguns casos
                // ex. aeroporto vc esta conectado, porem ainda não foi liberado
                // e por isso não tem rede
                capabilities.hasCapability(NET_CAPABILITY_VALIDATED)
    } else {
        @Suppress("DEPRECATION")
        connMgr.activeNetworkInfo?.isConnected == true
    }

/** EXIBIR MATERIAL ALERT DIALOG DE ACORDO COM SUAS NECESSIDADES */
fun Fragment.showDefaultMaterialAlertDialog(
    title: String? = null,
    message: String? = null,
    positiveButtonLabel: String? = null,
    positiveButtonClickListener: () -> Unit = {},
    negativeButtonLabel: String? = null,
    negativeButtonClickListener: () -> Unit = {},
    cancelable: Boolean = false,
    cancelListener: () -> Unit = {},
    dismissListener: () -> Unit = {},
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonLabel) { dialog, _ -> dialog.dismiss(); positiveButtonClickListener() }
        .setNegativeButton(negativeButtonLabel) { dialog, _ -> dialog.dismiss(); negativeButtonClickListener() }
        .setCancelable(cancelable)
        .setOnCancelListener { cancelListener() }
        .setOnDismissListener { dismissListener() }
        .create().also { it.show() }
}

/** CRIAR ALERTAS SEMI-CUSTOMIZAOS (APENAS COM CONTEUDO ESTÁTICO CUSTOMIZADO) */
fun Fragment.showDefaultMaterialAlertDialogWithCustomStaticContent(
    positiveButtonLabel: String? = null,
    positiveButtonClickListener: () -> Unit = {},
    negativeButtonLabel: String? = null,
    negativeButtonClickListener: () -> Unit = {},
    cancelable: Boolean = false,
    cancelListener: () -> Unit = {},
    dismissListener: () -> Unit = {},
    @LayoutRes customLayoutId: Int,
    @StyleRes styleId: Int? = null,
    @DrawableRes customBackgroundId: Int? = null
) {
    // if you want to customize the dialog's theme
    val builder = if (styleId != null) MaterialAlertDialogBuilder(
        ContextThemeWrapper(
            requireContext(),
            styleId
        )
    ) else MaterialAlertDialogBuilder(requireContext())

    // if you want to customize the alert dialog's content!
    builder.setView(customLayoutId)

    val dialog = builder
        .setPositiveButton(positiveButtonLabel) { dialog, _ -> dialog.dismiss(); positiveButtonClickListener() }
        .setNegativeButton(negativeButtonLabel) { dialog, _ -> dialog.dismiss(); negativeButtonClickListener() }
        .setCancelable(cancelable)
        .setOnCancelListener { cancelListener() }
        .setOnDismissListener { dismissListener() }
        .create()

    // if you want to customize the window background like color and border
    if (customBackgroundId != null) {
        dialog.window?.setBackgroundDrawableResource(customBackgroundId)
    }
    dialog.show()
}

/** CRIAR ALERTAS TOTALMENTE CUSTOMIZADOS */
fun Fragment.createFullCustomAlertDialog(
    customView: View,
    @StyleRes styleId: Int? = null,
    @DrawableRes customBackgroundId: Int? = null
): AlertDialog {
    // if you want to customize the dialog's theme
    val builder = if (styleId != null) MaterialAlertDialogBuilder(
        ContextThemeWrapper(
            requireContext(),
            styleId
        )
    ) else MaterialAlertDialogBuilder(requireContext())

    builder.setView(customView)
    val dialog = builder.create()

    // if you want to customize the window background like color and border
    if (customBackgroundId != null) {
        dialog.window?.setBackgroundDrawableResource(customBackgroundId)
    }
    return dialog
}

fun Fragment.openPhoneDial(phoneNumber: String) {
    Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }.let { startActivity(it) }
}

fun Fragment.setDefaultTheme(@StyleRes styleId: Int) {
    requireActivity().setTheme(styleId)
}

fun Fragment.showTransparentStatusBar(isTransparent: Boolean) {
    val translucentColor = 0x04000000
    val window = requireActivity().window
    if (isTransparent) window.addFlags(translucentColor) else window.clearFlags(translucentColor)
}

fun Fragment.setSystemStatusBarColorOverColorResource(@ColorRes id: Int) {
    requireActivity().window.statusBarColor = requireActivity().getColor(id)
}

fun Fragment.setSystemNavigationBarColorOverColorResource(@ColorRes id: Int) {
    requireActivity().window.navigationBarColor = requireActivity().getColor(id)
}

//fun Fragment.getColor(@AttrRes id: Int): Int {
//    val typedValue = TypedValue()
//    requireContext().theme.resolveAttribute(id, typedValue, true)
//    return typedValue.data
//}

fun Fragment.setTranslucentWindow(translucent: Boolean) {
    if (translucent) {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    } else {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
    }
}

fun openUrl(activity: Activity, url: String) {
    activity.startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
}


// ==================================================
// - Query Packages Android 12    -------------------
// ==================================================
/** PRECISA ESPECIFICAR A PERMISSION NO MANIFEST A PARTIR DO ANDROID 12 */
fun canHandleIntent(context: Context, intent: Intent): Boolean {
    return context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        .isNotEmpty()
}


fun Fragment.startFurtaCorAnim(view: TextView, fromColor:Int, toColor:Int){
    val furtaCorAnim: ValueAnimator = ObjectAnimator.ofInt(
        view,
        "textColor",
        fromColor,
        toColor
    )
    furtaCorAnim.duration = 1000
    furtaCorAnim.setEvaluator(ArgbEvaluator())
    furtaCorAnim.repeatCount = ValueAnimator.INFINITE
    furtaCorAnim.repeatMode = ValueAnimator.REVERSE
    furtaCorAnim.start()
}

fun Fragment.getDrawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), id)
}

fun Fragment.getColor(@ColorRes id: Int): Int {
    return requireActivity().resources.getColor(id, null)
}

fun Fragment.showDialog(
    title: String? = null,
    message: String,
    positiveButtonLabel: String,
    positiveButtonClickListener: () -> Unit = {},
    negativeButtonLabel: String? = null,
    negativeButtonClickListener: () -> Unit = {},
    cancelable: Boolean = true,
    cancelListener: () -> Unit = {}
) = MaterialAlertDialogBuilder(requireContext())
    .setTitle(title)
    .setMessage(message)
    .setPositiveButton(positiveButtonLabel) { _, _ -> positiveButtonClickListener() }
    .setNegativeButton(negativeButtonLabel) { _, _ -> negativeButtonClickListener() }
    .setCancelable(cancelable)
    .setOnCancelListener { cancelListener() }
    .create()
    .also { it.show() }}
