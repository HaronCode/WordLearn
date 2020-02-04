package ru.haroncode.wordlearn.mainflow.ui.word.set.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_word_user_set.*
import ru.haroncode.mvi.core.Store
import ru.haroncode.wordlearn.common.domain.result.Product
import ru.haroncode.wordlearn.common.ui.MviFragment
import ru.haroncode.wordlearn.common.ui.adapter.DefaultItemClicker
import ru.haroncode.wordlearn.common.ui.adapter.ItemAsyncAdapter
import ru.haroncode.wordlearn.common.ui.adapter.delegate.ButtonAdapterDelegate
import ru.haroncode.wordlearn.common.util.unsafeLazy
import ru.haroncode.wordlearn.mainflow.R
import ru.haroncode.wordlearn.mainflow.presentation.set.WordSetUserViewModel
import ru.haroncode.wordlearn.mainflow.presentation.set.WordSetUserViewModel.Action
import ru.haroncode.wordlearn.mainflow.presentation.set.WordSetUserViewModel.ViewEvent
import ru.haroncode.wordlearn.mainflow.presentation.set.WordSetUserViewState
import ru.haroncode.wordlearn.mainflow.presentation.set.WordSetUserViewState.Item
import ru.haroncode.wordlearn.mainflow.ui.word.set.list.WordSetDelegate

/**
 * @author HaronCode.
 */
class WordSetUserFragment : MviFragment<Action, WordSetUserViewState, ViewEvent>(R.layout.fragment_word_user_set) {

    companion object {

        fun newInstance() = WordSetUserFragment()
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wordSetUserViewModel: WordSetUserViewModel by viewModels {
        viewModelFactory
    }

    override val store: Store<Action, WordSetUserViewState, ViewEvent> = wordSetUserViewModel

    private val itemAdapter: ItemAsyncAdapter<Item> by unsafeLazy {
        ItemAsyncAdapter.Builder<Item>()
            .add(WordSetDelegate())
            .add(ButtonAdapterDelegate(), DefaultItemClicker(::onClickAddSet))
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itemAdapter
        }
    }

    private fun onClickAddSet(item: Item) = postAction(Action.CreateNew)

    override fun render(state: WordSetUserViewState) = when (val items = state.items) {
        is Product.Data -> itemAdapter.updateItems(items.value)
        else -> Unit
    }

    override fun route(event: ViewEvent) = when (event) {
        is ViewEvent.CreateNewDialog -> Toast.makeText(requireContext(), "Dialog", Toast.LENGTH_SHORT).show()
    }
}