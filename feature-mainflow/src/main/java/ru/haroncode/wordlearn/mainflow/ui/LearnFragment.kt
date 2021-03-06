package ru.haroncode.wordlearn.mainflow.ui

import android.os.Bundle
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_learn.*
import ru.haroncode.wordlearn.common.extension.onClick
import ru.haroncode.wordlearn.common.presentation.FlowRouter
import ru.haroncode.wordlearn.common.ui.BaseFragment
import ru.haroncode.wordlearn.mainflow.R
import ru.haroncode.wordlearn.mainflow.presentation.LearnReachableFlows

/**
 * @author HaronCode.
 */
class LearnFragment : BaseFragment(R.layout.fragment_learn) {

    @Inject
    lateinit var flowRouter: FlowRouter

    @Inject
    lateinit var flows: LearnReachableFlows

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wordsButton.onClick { flowRouter.startFlow(flows.learnWordsFlow()) }
        phraseButton.onClick { flowRouter.startFlow(flows.learnPhrasesFlow()) }
    }

    override fun onBackPressed() = flowRouter.finishFlow()
}
