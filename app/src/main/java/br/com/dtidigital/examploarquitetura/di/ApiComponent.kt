package br.com.dtidigital.examploarquitetura.di

import br.com.dtidigital.examploarquitetura.model.CountriesService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CountriesService)

}