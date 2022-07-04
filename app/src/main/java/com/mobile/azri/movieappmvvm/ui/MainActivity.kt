package com.mobile.azri.movieappmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.RecyclerView
import com.mobile.azri.movieappmvvm.BuildConfig
import com.mobile.azri.movieappmvvm.R
import com.mobile.azri.movieappmvvm.databinding.ActivityMainBinding
import com.mobile.azri.movieappmvvm.model.Movie
import com.mobile.azri.movieappmvvm.ui.home.adapter.HomeAdapter
import com.mobile.azri.movieappmvvm.ui.home.adapter.RecyclerViewHomeClickListener
import com.mobile.azri.movieappmvvm.ui.home.viewmodel.HomeViewModel
import com.mobile.azri.movieappmvvm.util.Resource
import com.mobile.azri.movieappmvvm.util.contentView
import dagger.hilt.android.AndroidEntryPoint

//TODO 9 - Add annotation @AndroidEntryPoint at MainActivity class
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),NavController.OnDestinationChangedListener {

    //TODO 25 - Add NavController and view binding for activity_main
    private val binding : ActivityMainBinding by contentView(R.layout.activity_main)
    private lateinit var navController : NavController
    private lateinit var appBarConfig : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding.run {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //  set status text dark
            window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.white) // set status background white
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
            navController = navHostFragment.navController
            navController.addOnDestinationChangedListener(this@MainActivity)
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }

    //action back
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            else -> {

            }
        }
        return false
    }

    fun changeTitle(title:String){
        setTitle(title)
    }
}