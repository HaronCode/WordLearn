package ru.haroncode.wordlearn.common.di

interface Injector<T> {

    fun inject(target: T)
}
