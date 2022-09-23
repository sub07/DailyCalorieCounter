package dev.mpardo.dailycaloriecounter.repository

interface Repository<T, K> {
    val nextId: Long
    val all: List<T>
    fun get(key: K) : T?
    fun add(e: T)
    fun delete(e: T)
    fun deleteAll()
    fun update(e: T)
}