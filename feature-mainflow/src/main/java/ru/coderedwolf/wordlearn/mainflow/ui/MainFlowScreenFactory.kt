package ru.coderedwolf.wordlearn.mainflow.ui

import ru.coderedwolf.wordlearn.mainflow.R
import ru.terrakok.cicerone.android.support.SupportAppScreen

/**
 * @author HaronCode. Date 01.05.2019.
 */
object MainFlowScreenFactory {

    fun findScreen(itemId: Int): SupportAppScreen = when (itemId) {
        R.id.action_word_set -> MainFlowScreens.WordSet
        R.id.action_phrases -> MainFlowScreens.PhraseTopic
        R.id.action_learn -> MainFlowScreens.Learn
        R.id.action_settings -> MainFlowScreens.Settings
        R.id.action_search -> MainFlowScreens.Search
        else -> throw IllegalArgumentException("Unknown itemId $itemId")
    }
}