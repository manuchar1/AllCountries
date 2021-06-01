package com.example.allcountries.mainactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allcountries.Constants
import com.example.allcountries.databinding.ActivityMainBinding
import com.example.allcountries.databinding.LayoutListItemBinding
import com.example.allcountries.databinding.LoadingItemBinding
import com.example.allcountries.models.CountriesItem
import com.example.allcountries.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var adapter = RecyclerAdapter()
    private lateinit var binding: ActivityMainBinding

    private var countriesList = mutableListOf<CountriesItem>()

    private var loadingMore = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }


    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class LoadingViewHolder(val binding: LoadingItemBinding
        ) : RecyclerView.ViewHolder(binding.root)

        inner class CountryViewHolder(val binding: LayoutListItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =

            when (viewType) {
                Constants.VIEW_TYPE_COUNTRY -> CountryViewHolder(
                    LayoutListItemBinding.inflate(LayoutInflater.from(parent.context))
                )
                Constants.VIEW_TYPE_LOADER -> LoadingViewHolder(
                    LoadingItemBinding.inflate(LayoutInflater.from(parent.context))
                )
                else -> throw RuntimeException("unknown ViewType")
            }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is CountryViewHolder -> {
                    val item = countriesList[position]
                    holder.binding.tvName.text = item.name
                    holder.binding.tvNativeName.text = item.name
                    holder.binding.tvCapital.text = item.capital
                    holder.binding.tvRegion.text = item.region
                    holder.binding.tvArea.text = item.area.toString()
                    holder.binding.tvCurrency.text = item.currencies.toString()
                    Glide.with(this@MainActivity).load(item.flag)
                        .into(holder.binding.ivFlag)
                    holder.binding.root.tag = item

                }
                is LoadingViewHolder -> {
                    holder.binding.loader.visibility = if (loadingMore) View.VISIBLE else View.GONE
                 }

            }
        }

        override fun getItemCount() = countriesList.size + 1


        override fun getItemViewType(position: Int): Int {
            return if (itemCount - 1 == position) Constants.VIEW_TYPE_LOADER else Constants.VIEW_TYPE_COUNTRY
        }

    }

    private fun init() {
        loadData()
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = LoaderSpanSizeLookup()
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    private fun loadData() {

        lifecycleScope.launchWhenStarted {
            loadingMore = true
            adapter.notifyItemChanged(adapter.itemCount - 1)
            try {
                val data = withContext(Dispatchers.IO) {
                    NetworkClient.counstriesService.getCountries()
                }
                countriesList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                DialogFragment()
            }
        }
    }

    inner class LoaderSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (adapter.itemCount - 1 == position) 2 else 1
        }
    }
}