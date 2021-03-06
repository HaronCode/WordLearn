package ru.haroncode.wordlearn.mainflow.presentation.set

import com.haroncode.gemini.android.connection.DelegateConnectionRuleFactory
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.dsl.connectTo
import com.haroncode.gemini.dsl.connectionFactory
import com.haroncode.gemini.dsl.eventsTo
import com.haroncode.gemini.dsl.observeOn
import javax.inject.Inject
import ru.haroncode.wordlearn.common.di.PerFragment
import ru.haroncode.wordlearn.common.domain.system.SchedulerProvider
import ru.haroncode.wordlearn.mainflow.ui.word.set.user.WordSetUserFragment

@PerFragment
class WordSetUserConnectionFactory @Inject constructor(
    private val wordSetUserStore: WordSetUserStore,
    private val schedulerProvider: SchedulerProvider
) : DelegateConnectionRuleFactory<WordSetUserFragment>() {

    override val connectionRuleFactory: ConnectionRule.Factory<WordSetUserFragment> = connectionFactory { view ->
        rule { view connectTo wordSetUserStore }
        rule { wordSetUserStore connectTo view observeOn schedulerProvider.mainThread }
        rule { wordSetUserStore eventsTo view }
        autoDispose { wordSetUserStore }
    }
}
