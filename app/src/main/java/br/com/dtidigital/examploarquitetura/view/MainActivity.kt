package br.com.dtidigital.examploarquitetura.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.com.dtidigital.examploarquitetura.R
import br.com.dtidigital.examploarquitetura.model.Country
import br.com.dtidigital.examploarquitetura.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private val countriesAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        countriesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refresh()
        }
        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.countries.observe(this, Observer {
                countries: List<Country>? -> countries?.let {
            countriesList.visibility = View.VISIBLE
            countriesAdapter.updateCountries(it)
        }
        })

        viewModel.countryLoadError.observe(this, Observer {
                isError: Boolean? -> isError?.let {
            list_error.visibility = if(it) View.VISIBLE else View.GONE
        }
        })

        viewModel.loading.observe(this, Observer{
                isLoading: Boolean? -> isLoading?.let {
            loading_view.visibility = if(it) View.VISIBLE else View.GONE
            if(it) {
                list_error.visibility = View.GONE
                countriesList.visibility = View.GONE
            }
        }
        })
    }
}
