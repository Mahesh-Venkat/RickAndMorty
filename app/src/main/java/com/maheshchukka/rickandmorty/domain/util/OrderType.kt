package com.maheshchukka.rickandmorty.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
