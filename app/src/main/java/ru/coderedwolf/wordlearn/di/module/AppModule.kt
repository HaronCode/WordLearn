package ru.coderedwolf.wordlearn.di.module

import android.content.Context
import ru.coderedwolf.wordlearn.di.provider.DataBaseProvider
import ru.coderedwolf.wordlearn.domain.data.DataBase
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module


class AppModule(context: Context) : Module() {
    init {
        //Global
        bind(Context::class.java).toInstance(context)
        bind(DataBase::class.java).toProvider(DataBaseProvider::class.java).singletonInScope()

        //Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    }
}