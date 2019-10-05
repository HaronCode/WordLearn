package ru.coderedwolf.wordlearn.wordflow.di

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.coderedwolf.wordlearn.common.di.*
import ru.coderedwolf.wordlearn.common.presentation.ErrorHandler
import ru.coderedwolf.wordlearn.wordflow.presentation.CreateWordFeature
import ru.coderedwolf.wordlearn.wordflow.ui.CreateWordFragment

@Module
object CreateWordComponentBuilderModule {
    @Provides
    @JvmStatic
    @IntoMap
    @ClassKey(CreateWordFragment::class)
    fun provideCreateWordComponentBuilder() = InjectorBuilder<CreateWordFragment> {
        DaggerCreateWordComponent.builder()
                .createWordDependencies(findComponentDependencies())
                .build()
    }
}

@PerFragment
@Component(
        dependencies = [
            CreateWordDependencies::class
        ]
)
interface CreateWordComponent : Injector<CreateWordFragment>

interface CreateWordDependencies : ScreenDependencies {
    fun categoryId(): Long
    fun createWordFeature(): CreateWordFeature
    fun errorHandler(): ErrorHandler
}