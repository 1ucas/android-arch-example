package br.com.dtidigital.examploarquitetura.di

import br.com.dtidigital.examploarquitetura.model.CountriesService
import dagger.Module
import dagger.Provides

@Module
class ServiceModule {

    @Provides
    fun provideCountriesService() : CountriesService {
        return CountriesService()
    }
}