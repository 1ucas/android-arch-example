package br.com.dtidigital.examploarquitetura.di

import br.com.dtidigital.examploarquitetura.viewmodel.ListViewModel
import dagger.Component

@Component(modules = [ServiceModule::class])
interface ServiceComponent {

    fun inject(service: ListViewModel)
}