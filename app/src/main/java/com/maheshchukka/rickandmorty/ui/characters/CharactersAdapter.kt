import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.maheshchukka.rickandmorty.R
import com.maheshchukka.rickandmorty.databinding.CharacterViewItemBinding
import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class CharactersAdapter(private val clickListener: CharacterItemListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(CharacterDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<CharacterModel>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.CharacterDataItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val characterDataItem = getItem(position) as DataItem.CharacterDataItem
                holder.bind(characterDataItem.characterModel, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CharacterDataItem -> ITEM_VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Unknown position $position")
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(private val binding: CharacterViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterModel, clickListener: CharacterItemListener) {
            binding.characterName.text = item.name
                ?: binding.characterName.context.getString(R.string.shared_label_unavailable)
            binding.characterStatus.text = binding.characterStatus.context.getString(
                R.string.character_status_label,
                item.status
                    ?: binding.characterStatus.context.getString(R.string.shared_label_unavailable)
            )
            binding.characterImage.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_broken_image)
                transformations(CircleCropTransformation())
            }
            binding.parent.setOnClickListener { clickListener.onClick(characterModel = item) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CharacterViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CharacterDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class CharacterItemListener(val clickListener: (characterId: Long) -> Unit) {
    fun onClick(characterModel: CharacterModel) = clickListener(characterModel.characterId)
}

sealed class DataItem {
    data class CharacterDataItem(val characterModel: CharacterModel) : DataItem() {
        override val id = characterModel.characterId
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}
