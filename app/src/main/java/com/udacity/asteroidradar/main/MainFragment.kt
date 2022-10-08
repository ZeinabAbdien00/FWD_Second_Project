package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.DatabaseBuilder
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.recycler_view.Adapter
import com.udacity.asteroidradar.repository.Repository
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val databaseDAO by lazy { DatabaseBuilder.getInstance(requireActivity()).databaseDAO_object }
    private val databaseBuilderObject by lazy { DatabaseBuilder.getInstance(requireActivity()) }
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerAdapter: Adapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val repository = Repository(databaseBuilderObject)
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        val adapter = AstAdapter(AstAdapter.AsteroidClickListener { asteroid ->
            viewModel.navigateToDetailFragment.value = asteroid
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        if (checkForInternetConnection(requireContext())) {

            viewModel.getAsteroids()
            viewModel.getAsteroidPicture()
            viewModel.responseIncomeData.observe(viewLifecycleOwner, Observer { incomeData ->

                recyclerAdapter = Adapter(viewModel.responseIncomeData.value!!)
                asteroid_recycler.layoutManager = LinearLayoutManager(activity)
                recyclerAdapter.setOnItemClick(object : Adapter.OnItemClick {
                    override fun OnItemClick(position: Int) {
                        findNavController().navigate(
                            MainFragmentDirections.actionShowDetail(
                                viewModel.responseIncomeData.value!![position]
                            )
                        )
                    }
                })


                  viewModel.navigateToDetailFragment.observe(viewLifecycleOwner , Observer{
                  asteroid ->
                  if(asteroid!=null){
                  findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                  viewModel.navigateToDetailFragment.value=null
                  }})

                GlobalScope.launch {
                    databaseDAO.insertAll(viewModel.responseIncomeData.value!!)
                }
            })
        } else {
                Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show()
            try {
                GlobalScope.launch {
                    viewModel.arraylist = databaseDAO.getAll() as ArrayList<Asteroid>
                    recyclerAdapter = Adapter(viewModel.arraylist)
                    asteroid_recycler.layoutManager = LinearLayoutManager(activity)

                    recyclerAdapter.setOnItemClick(object : Adapter.OnItemClick {
                        override fun OnItemClick(position: Int) {
                            findNavController().navigate(
                                MainFragmentDirections.actionShowDetail(
                                    viewModel.arraylist[position]
                                )
                            )
                        }
                    })
                }
            } catch (E: Exception) {
                Toast.makeText(context, "Failed , Try again", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.picture.observe(viewLifecycleOwner, Observer { picture ->
            Log.i("ResponseAsteroids", "pic $picture ")
        })
        setHasOptionsMenu(true)
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onChangeFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> MainViewModel.AsteroidTime.TODAY
                else -> MainViewModel.AsteroidTime.ALL
            }
        )
        return true
    }
}
