package com.example.mymaterialdesign.recycler


const val TYPE_ERTHS = 0
const val TYPE_MARS = 1
const val TYPE_HEADER = 2
class Data(
    val id:Int = 0,
    val name: String = "Text",
    val someDescription: String? = "Description",
    val type: Int = TYPE_MARS
)