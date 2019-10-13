package ru.coderedwolf.wordlearn.wordflow.ui

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_create_word.*
import ru.coderedwolf.wordlearn.common.domain.system.SchedulerProvider
import ru.coderedwolf.wordlearn.common.domain.validator.ResourceViolation
import ru.coderedwolf.wordlearn.common.extension.onClick
import ru.coderedwolf.wordlearn.common.presentation.FlowRouter
import ru.coderedwolf.wordlearn.common.ui.BaseFragment
import ru.coderedwolf.wordlearn.common.ui.event.ChangeText
import ru.coderedwolf.wordlearn.common.ui.event.UiEvent
import ru.coderedwolf.wordlearn.common.ui.item.ComposeItemClicker
import ru.coderedwolf.wordlearn.common.ui.item.DefaultItemClicker
import ru.coderedwolf.wordlearn.word.model.WordExample
import ru.coderedwolf.wordlearn.wordflow.R
import ru.coderedwolf.wordlearn.wordflow.presentation.*
import javax.inject.Inject

/**
 * @author CodeRedWolf. Date 06.06.2019.
 */
class CreateWordFragment : BaseFragment(),
    CreateWordExampleDialogFragment.OnCreateExampleListener,
    ObservableSource<UiEvent>,
    Consumer<CreateWordViewModel> {

    private val source = PublishSubject.create<UiEvent>()

    override val layoutRes: Int = R.layout.fragment_create_word

    private val mainSection = Section().apply {
        setFooter(AddExampleItem())
    }
    private val wordExampleAdapter = GroupAdapter<ViewHolder>().apply {
        add(mainSection)
    }

    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var router: FlowRouter
    @Inject lateinit var createWordFeature: CreateWordFeature

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { router.exit() }
        examplesList.apply {
            itemAnimator = null
            adapter = wordExampleAdapter
        }

        val itemClicker = ComposeItemClicker.Builder()
            .add(WordExampleItem::class, DefaultItemClicker(::onClickRemoveExample))
            .add(AddExampleItem::class, DefaultItemClicker { showDialogCreateExample() })
            .build()

        wordExampleAdapter.setOnItemClickListener(itemClicker)

        saveButton.onClick { source.onNext(SaveClick) }
        listOf(word, translation, transcription, association)
            .forEach(::connect)

        CreateWordFragmentBindings(this, createWordFeature)
            .setup(this)
    }

    private fun onClickRemoveExample(item: WordExampleItem) = item.wordExample
        .let(::RemoveWordExample)
        .let(source::onNext)

    override fun accept(viewModel: CreateWordViewModel) {
        updateExampleList(viewModel.exampleList)

        wordLayout.error = (viewModel.wordVerify as? ResourceViolation)?.res?.resString()
        translationLayout.error = (viewModel.translationVerify as? ResourceViolation)?.res?.resString()
        saveButton.isEnabled = viewModel.enableButtonApply
    }

    private fun updateExampleList(list: List<WordExample>) {
        mainSection.update(list.map(::WordExampleItem))
    }

    override fun onCreateWordExample(wordExample: WordExample) = source
        .onNext(AddWordExample(wordExample))

    private fun showDialogCreateExample() = CreateWordExampleDialogFragment.instance()
        .show(childFragmentManager, CreateWordExampleDialogFragment.TAG)

    override fun onBackPressed() = router.exit()

    override fun onDestroyView() {
        super.onDestroyView()
        examplesList.adapter = null
    }

    override fun subscribe(observer: Observer<in UiEvent>) = source.subscribe(observer)

    private fun connect(editText: EditText) = RxTextView.textChangeEvents(editText)
        .skipInitialValue()
        .observeOn(schedulerProvider.computation)
        .map { event -> ChangeText(event.view().id, event.text()) }
        .autoDisposable()
        .subscribe(source)

}