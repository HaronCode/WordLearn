package ru.coderedwolf.wordlearn.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import de.siegmar.fastcsv.reader.CsvParser
import de.siegmar.fastcsv.reader.CsvRow
import ru.coderedwolf.wordlearn.ui.base.BaseFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace

/**
 * @author CodeRedWolf. Date 21.04.2019.
 */
fun BaseFragment.scopeName(): String {
    return "${javaClass.simpleName}_${hashCode()}"
}

fun Navigator.setLaunchScreen(screen: SupportAppScreen) {
    applyCommands(
            arrayOf(
                    BackTo(null),
                    Replace(screen)
            )
    )
}

fun Activity.hideKeyboard() {
    currentFocus?.apply {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Activity.showKeyboard() {
    currentFocus?.apply {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

