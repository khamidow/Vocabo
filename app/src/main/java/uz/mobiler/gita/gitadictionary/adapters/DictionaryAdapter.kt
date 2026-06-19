package uz.mobiler.gita.gitadictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.mobiler.gita.gitadictionary.R
import uz.mobiler.gita.gitadictionary.data.source.entity.DictionaryEntity
import uz.mobiler.gita.gitadictionary.databinding.ItemWordBinding
import uz.mobiler.gita.gitadictionary.utils.foregroundColor

class DictionaryAdapter :
    ListAdapter<DictionaryEntity, DictionaryAdapter.DictionaryViewHolder>(DictionaryDiffUtil) {
    private var query: String? = null

    object DictionaryDiffUtil : DiffUtil.ItemCallback<DictionaryEntity>() {
        override fun areItemsTheSame(
            oldItem: DictionaryEntity,
            newItem: DictionaryEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DictionaryEntity,
            newItem: DictionaryEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    private var onFavouriteClick: ((DictionaryEntity, Int) -> Unit)? = null
    fun setFavouriteEditListener(f: (DictionaryEntity, Int) -> Unit) {
        onFavouriteClick = f
    }
    private var onSpeechClick: ((DictionaryEntity, Int) -> Unit)? = null
    fun setSpeechBtnListener(f: (DictionaryEntity, Int) -> Unit) {
        onSpeechClick = f
    }

    inner class DictionaryViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val color by lazy { ContextCompat.getColor(binding.root.context, R.color.prColor) }

        fun bind() {
//            binding.englishWord.text = getItem(absoluteAdapterPosition).english
            if (query == null) binding.englishWord.text = getItem(absoluteAdapterPosition).english
            else binding.englishWord.text = getItem(absoluteAdapterPosition).english
                .foregroundColor(color, query!!)

            binding.uzbekWord.text = getItem(absoluteAdapterPosition).uzbek

            if (getItem(absoluteAdapterPosition).isFavourite == 1)
                binding.favouriteBtn.setImageResource(R.drawable.marked)
            else binding.favouriteBtn.setImageResource(R.drawable.un_marked)

            binding.favouriteBtn.setOnClickListener { onFavouriteClick?.invoke(getItem(absoluteAdapterPosition), position) }
            binding.speakerImg.setOnClickListener { onSpeechClick?.invoke(getItem(absoluteAdapterPosition), position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DictionaryViewHolder(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bind()
    }

    fun mySubmitList(ls: List<DictionaryEntity>, query: String?) {
        this.query = query
        submitList(ls)
        notifyDataSetChanged()
    }
}