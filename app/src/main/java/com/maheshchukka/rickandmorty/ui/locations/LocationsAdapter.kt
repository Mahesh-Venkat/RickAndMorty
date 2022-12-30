import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maheshchukka.rickandmorty.R
import com.maheshchukka.rickandmorty.databinding.HeaderBinding
import com.maheshchukka.rickandmorty.databinding.LocationViewItemBinding
import com.maheshchukka.rickandmorty.domain.model.LocationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class LocationsAdapter(private val clickListener: LocationItemListener) :
    ListAdapter<LocationDataItem, RecyclerView.ViewHolder>(LocationDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<LocationModel>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(LocationDataItem.Header)
                else -> listOf(LocationDataItem.Header) + list.map {
                    LocationDataItem.LocationItem(
                        it
                    )
                }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val locationItem = getItem(position) as LocationDataItem.LocationItem
                holder.bind(locationItem.locationModel, clickListener)
            }

            is TextViewHolder -> {
                holder.bind()
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
            is LocationDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is LocationDataItem.LocationItem -> ITEM_VIEW_TYPE_ITEM
            else -> throw IllegalArgumentException("Unknown position $position")
        }
    }

    class TextViewHolder private constructor(private val binding: HeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): LocationsAdapter.TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBinding.inflate(layoutInflater, parent, false)
                return LocationsAdapter.TextViewHolder(binding)
            }
        }

        fun bind() {
            binding.headerTitle.text =
                binding.headerTitle.context.getString(R.string.header_locations)
        }
    }

    class ViewHolder private constructor(private val binding: LocationViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationModel, clickListener: LocationItemListener) {
            binding.locationName.text = item.name
                ?: binding.locationName.context.getString(R.string.shared_label_unavailable)
            binding.locationType.text = binding.locationType.context.getString(
                R.string.location_type_label,
                item.type
                    ?: binding.locationType.context.getString(R.string.shared_label_unavailable)
            )
            binding.parent.setOnClickListener { clickListener.onClick(locationModel = item) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LocationViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class LocationDiffCallback : DiffUtil.ItemCallback<LocationDataItem>() {
    override fun areItemsTheSame(oldItem: LocationDataItem, newItem: LocationDataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LocationDataItem, newItem: LocationDataItem): Boolean {
        return oldItem == newItem
    }
}

class LocationItemListener(val clickListener: (infoUrl: String) -> Unit) {
    fun onClick(locationModel: LocationModel) = clickListener(locationModel.infoUrl ?: "")
}

sealed class LocationDataItem {
    data class LocationItem(val locationModel: LocationModel) : LocationDataItem() {
        override val id = locationModel.locationId
    }

    object Header : LocationDataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}
