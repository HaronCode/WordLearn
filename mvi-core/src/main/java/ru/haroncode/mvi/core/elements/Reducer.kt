package ru.haroncode.mvi.core.elements

/**
 * @author HaronCode. Date 13.10.2019.
 */
typealias Reducer<State, Effect> = (state: State, effect: Effect) -> State