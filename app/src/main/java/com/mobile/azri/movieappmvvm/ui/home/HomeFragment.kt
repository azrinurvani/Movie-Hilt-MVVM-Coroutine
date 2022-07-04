package com.mobile.azri.movieappmvvm.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import com.mobile.azri.movieappmvvm.BuildConfig
import com.mobile.azri.movieappmvvm.R
import com.mobile.azri.movieappmvvm.databinding.FragmentHomeBinding
import com.mobile.azri.movieappmvvm.model.Movie
import com.mobile.azri.movieappmvvm.ui.MainActivity
import com.mobile.azri.movieappmvvm.ui.home.adapter.HomeAdapter
import com.mobile.azri.movieappmvvm.ui.home.adapter.RecyclerViewHomeClickListener
import com.mobile.azri.movieappmvvm.ui.home.viewmodel.HomeViewModel
import com.mobile.azri.movieappmvvm.util.Resource
import dagger.hilt.android.AndroidEntryPoint

//TODO 6 - Create HomeFragment (Don't forget to add annotation @AndroidEntryPoint at Class UI (Fragment/Activity))
@AndroidEntryPoint
class HomeFragment : Fragment(), RecyclerViewHomeClickListener{

    //TODO 27 - Binding View and Retrieve data from ViewModel
    private lateinit var binding : FragmentHomeBinding
    private val homeViewModel : HomeViewModel by viewModels()
    private val homeAdapter : HomeAdapter by lazy {
        HomeAdapter(requireContext(),this@HomeFragment)}

    var totalPages = 0
    var counter = 1
    var present_state = POPULAR



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply { R.integer.reply_motion_duration_large.toLong() } //menggunakan motion values
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.recyclerView.apply {
            adapter = homeAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {//1 for down
                        if (counter<=totalPages){
                            if (present_state == POPULAR){
                                homeViewModel.fetchPopular(BuildConfig.MOVIE_DB_TOKEN)
                            }else{
                                homeViewModel.fetchTopRateMovies(BuildConfig.MOVIE_DB_TOKEN)
                            }
                            ++counter
                        }
                    }
                }
            })
        }

        homeViewModel.fetchPopular(BuildConfig.MOVIE_DB_TOKEN)
        (activity as MainActivity?)?.changeTitle("Popular Movies")
        observeUI()
        observeTop()

        return binding.root
    }

    private fun observeUI() {
        homeViewModel.moviePopular.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progress.visibility = View.GONE
                    val value = it.data
                    totalPages = value?.totalPages!!
                    val data = value.movies
                    data?.let { it1 -> homeAdapter.submitList(it1) }
                }
                is Resource.Error -> {
                    binding.progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
                else -> {
                    binding.progress.visibility = View.GONE
//                    it.message?.let { message ->
//                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
//                    }
                }
            }

        }
    }

    private fun observeTop() {
        homeViewModel.topMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progress.visibility = View.GONE
                    val value = it.data!!
                    totalPages = value.totalPages
                    val data = value.movies
                    homeAdapter.submitList(data!!)
                }
                is Resource.Error -> {
                    binding.progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
                else ->{
                    binding.progress.visibility = View.GONE
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.most_rated -> {
                (activity as MainActivity?)?.changeTitle("Top Movies")
                homeViewModel.fetchTopRateMovies(BuildConfig.MOVIE_DB_TOKEN)
                present_state = TOP_RATED
                true
            }
            R.id.popular -> {
                (activity as MainActivity?)?.changeTitle("Popular Movies")
                homeViewModel.fetchPopular(BuildConfig.MOVIE_DB_TOKEN)
                present_state = POPULAR
                true
            }
            else ->   return super.onOptionsItemSelected(item)
        }

    }


    override fun clickOnItem(data: Movie, card: View) {
        Toast.makeText(requireContext(),"Item : "+data.title,Toast.LENGTH_LONG).show()
    }

    companion object{
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
    }
}