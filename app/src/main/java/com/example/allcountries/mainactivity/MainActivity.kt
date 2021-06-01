package com.example.allcountries.mainactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.allcountries.databinding.ActivityMainBinding
import com.example.allcountries.databinding.LayoutListItemBinding
import com.example.allcountries.models.CountriesItem
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.layout_list_item.view.*


class MainActivity : AppCompatActivity() {

    private var adapter = RecyclerAdapter()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this).get(CountriesViewModel::class.java)
        viewModel.init()
        viewModel._loadingLiveData.observe(this, {
            binding.swipeToRefresh.isRefreshing = it
        })
        viewModel._countriesLiveData.observe(this, {
            adapter.setData(it.toMutableList())

        })

        initRecycler()

     binding.swipeToRefresh.setOnRefreshListener {
         adapter.clearData()
         viewModel.init()

     }
    }



    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

        private val countries = mutableListOf<CountriesItem>()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind()

        }

        override fun getItemCount() = countries.size

        fun setData(countries: MutableList<CountriesItem>) {
            this.countries.clear()
            this.countries.addAll(countries)
            notifyDataSetChanged()
        }
        fun clearData() {
            this.countries.clear()
        }

        inner class ViewHolder(binding: LayoutListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            private lateinit var model: CountriesItem
            fun bind() {
                model = countries[adapterPosition]
                itemView.tvName.text = model.name
                itemView.tvNativeName.text = model.nativeName
                itemView.tvCapital.text = model.capital
                itemView.tvRegion.text =  model.region
                itemView.tvPopulation.text = model.population.toString()


                val requestBuilder = GlideToVectorYou
                    .init()
                    .with(itemView.context)
                    .requestBuilder

                requestBuilder
                    .load(model.flag)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(
                        RequestOptions()
                            .centerCrop()
                    )
                    .into(itemView.ivFlag)



            }
        }

    }

    private fun initRecycler() {
        adapter = RecyclerAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }



}