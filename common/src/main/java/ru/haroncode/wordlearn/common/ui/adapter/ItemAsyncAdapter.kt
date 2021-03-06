package ru.haroncode.wordlearn.common.ui.adapter

import android.view.View
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlin.reflect.KClass

class ItemAsyncAdapter<ItemModel : Any>(
    private val itemClickers: SparseArrayCompat<ItemClicker<*, out BaseViewHolder>>,
    diffUtilCallback: DiffUtil.ItemCallback<ItemModel>,
    adapterDelegatesManager: AdapterDelegatesManager<List<ItemModel>>
) : AsyncListDifferDelegationAdapter<ItemModel>(
    diffUtilCallback,
    adapterDelegatesManager
) {

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val adapterDelegate = delegatesManager.getDelegateForViewType(holder.itemViewType)
        if (adapterDelegate is ClickableAdapter && holder is BaseViewHolder) {
            bindClicker(adapterDelegate, holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val adapterDelegate = delegatesManager.getDelegateForViewType(holder.itemViewType)
        if (adapterDelegate is ClickableAdapter && holder is BaseViewHolder) {
            unbindClicker(adapterDelegate, holder)
        }
    }

    fun updateItems(items: List<ItemModel>) {
        this.items = items
    }

    private fun <VH : BaseViewHolder> bindClicker(adapter: ClickableAdapter, viewHolder: VH) {
        val itemClicker = findItemClicker<Any, VH>(viewHolder.itemViewType)
        adapter.bindClickListener(viewHolder, ::onClick)
        itemClicker.onBindClicker(viewHolder)
    }

    private fun <VH : BaseViewHolder> unbindClicker(adapter: ClickableAdapter, viewHolder: VH) {
        val itemClicker = findItemClicker<Any, VH>(viewHolder.itemViewType)
        adapter.unbindClickListener(viewHolder)
        itemClicker.onUnbindClicker(viewHolder)
    }

    private fun <VH : BaseViewHolder> onClick(viewHolder: VH, view: View) {
        val itemViewType = viewHolder.itemViewType
        val position = viewHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val itemClicker = findItemClicker<Any, VH>(itemViewType)
            val adapterDelegate = findAdapterDelegate<ItemModel>(itemViewType)
            adapterDelegate.getItem(items[position])?.let { item ->
                itemClicker.invoke(viewHolder, view, item)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <E, VH : BaseViewHolder> findItemClicker(itemViewType: Int): ItemClicker<E, VH> {
        return itemClickers.get(itemViewType) as ItemClicker<E, VH>
    }

    @Suppress("UNCHECKED_CAST")
    private fun <ItemModel> findAdapterDelegate(itemViewType: Int): ItemAdapterDelegate<ItemModel, *> {
        return delegatesManager.getDelegateForViewType(itemViewType) as ItemAdapterDelegate<ItemModel, *>
    }

    class Builder<ItemModel : ComparableItem> {

        fun singleViewType(
            delegate: ItemAdapterDelegate<ItemModel, *>
        ): SingleViewTypeBuilder<ItemModel> = singleViewType(delegate, ItemClicker.NoneClicker())

        fun singleViewType(
            delegate: ItemAdapterDelegate<ItemModel, *>,
            itemClicker: ItemClicker<out ItemModel, BaseViewHolder>
        ): SingleViewTypeBuilder<ItemModel> = SingleViewTypeBuilder(delegate, itemClicker)

        fun withViewTypeSelector(
            viewTypeSelector: (KClass<out ItemModel>) -> Int
        ) = SealedViewTypeBuilder(viewTypeSelector)
    }

    class SingleViewTypeBuilder<ItemModel : ComparableItem>(
        private val viewHolderRenderer: ItemAdapterDelegate<ItemModel, *>,
        private val itemClicker: ItemClicker<*, out BaseViewHolder>
    ) {

        companion object {
            private const val DEFAULT_VIEW_TYPE = 0
        }

        private var diffCallback: DiffUtil.ItemCallback<ItemModel> = ComparableDiffUtilItemCallback()

        fun with(diffCallback: DiffUtil.ItemCallback<ItemModel>): SingleViewTypeBuilder<ItemModel> {
            this.diffCallback = diffCallback
            return this
        }

        fun build(): ItemAsyncAdapter<ItemModel> {
            val adapterDelegatesManager = ItemAdapterDelegatesManager<ItemModel> { DEFAULT_VIEW_TYPE }
            adapterDelegatesManager.addDelegate(DEFAULT_VIEW_TYPE, viewHolderRenderer)

            val itemClickers = SparseArrayCompat<ItemClicker<*, out BaseViewHolder>>()
            itemClickers.put(DEFAULT_VIEW_TYPE, itemClicker)

            return ItemAsyncAdapter(
                diffUtilCallback = diffCallback,
                adapterDelegatesManager = adapterDelegatesManager,
                itemClickers = itemClickers
            )
        }
    }

    class SealedViewTypeBuilder<ItemModel : ComparableItem>(sealedClassViewTypeSelector: (KClass<out ItemModel>) -> Int) {

        private val adapterDelegatesManager = ItemAdapterDelegatesManager(sealedClassViewTypeSelector)
        private val clickers: SparseArrayCompat<ItemClicker<*, out BaseViewHolder>> = SparseArrayCompat()
        private var diffCallback: DiffUtil.ItemCallback<ItemModel> = ComparableDiffUtilItemCallback()

        fun with(diffCallback: DiffUtil.ItemCallback<ItemModel>): SealedViewTypeBuilder<ItemModel> {
            this.diffCallback = diffCallback
            return this
        }

        fun add(
            kClass: KClass<out ItemModel>,
            delegate: ItemAdapterDelegate<ItemModel, *>,
            clicker: ItemClicker<out ItemModel, BaseViewHolder> = ItemClicker.NoneClicker()
        ): SealedViewTypeBuilder<ItemModel> {
            val viewType = adapterDelegatesManager.addDelegateByClass(kClass, delegate)
            clickers.put(viewType, clicker)
            return this
        }

        fun build() = ItemAsyncAdapter(
            adapterDelegatesManager = adapterDelegatesManager,
            itemClickers = clickers,
            diffUtilCallback = diffCallback
        )
    }
}
