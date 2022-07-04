package com.mobile.azri.movieappmvvm.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.azri.movieappmvvm.databinding.MovieCardBinding
import com.mobile.azri.movieappmvvm.model.Movie

//TODO 26 - Create HomeAdapter
class HomeAdapter(val context: Context, private val recyclerViewHomeClickListener: RecyclerViewHomeClickListener): RecyclerView.Adapter<ViewHolder>() {

    private lateinit var recyclerView: RecyclerView
    lateinit var mActivity:AppCompatActivity

    private var items:List<Movie?> = ArrayList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = MovieCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item  = items[position]
        item.let {
            holder.apply {
                if (item != null) {
                    bind(item, isLinearLayoutManager() )
                    itemView.tag = item
                }
            }
        }
        holder.itemView.setOnClickListener {
            item?.let { data -> recyclerViewHomeClickListener.clickOnItem(data,holder.itemView) }
        }
    }

    override fun getItemCount(): Int {
        if (items==null){
            return 0
        }else{
            return items.size
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun submitList(itemList:List<Movie?>){
        items = itemList
        notifyDataSetChanged()
    }

    private fun isLinearLayoutManager() = recyclerView.layoutManager is LinearLayoutManager

    companion object {
        private const val TAG = "HomeAdapter"
    }

}

class ViewHolder(private val binding : MovieCardBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(data:Movie,isLinearLayoutManager: Boolean){
        binding.apply {
            doc = data
            executePendingBindings()
        }
    }
}

interface RecyclerViewHomeClickListener {
    fun clickOnItem(data: Movie, card: View)
}