package br.com.dtidigital.examploarquitetura

import android.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.dtidigital.examploarquitetura.model.CountriesService
import br.com.dtidigital.examploarquitetura.model.Country
import br.com.dtidigital.examploarquitetura.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess() {
        // Preparando os dados
        val country = Country("Nome", "Capital", "Url")
        val countriesList: ArrayList<Country> = arrayListOf(country)

        testSingle = Single.just(countriesList)

        `when`(countriesService.getCountries()).thenReturn(testSingle)

        // Testando a funcionalidade
        listViewModel.refresh()


        // Conferindo o teste
        Assert.assertEquals(1, listViewModel.countries.value?.size)
        Assert.assertEquals(false, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCountriesFailed() {
        // Preparando os dados
        testSingle = Single.error(Throwable())

        `when`(countriesService.getCountries()).thenReturn(testSingle)

        // Testando a funcionalidade
        listViewModel.refresh()


        // Conferindo o teste
        Assert.assertEquals(true, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object: Scheduler() {
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        // Configura todos Schedulers do teste apontarem para o objeto criado no setup
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler: Callable<Scheduler> -> immediate}
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler: Callable<Scheduler> -> immediate}
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler: Callable<Scheduler> -> immediate}
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler: Callable<Scheduler> -> immediate}
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler> -> immediate}
    }
}