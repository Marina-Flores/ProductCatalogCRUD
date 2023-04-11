package com.example.productcatalog.crud.data.dao

import androidx.room.*
import com.example.productcatalog.crud.data.models.Product

@Dao
interface ProductDAO{

    @Insert
    fun Insert(product: Product)

    @Query("SELECT * FROM Product")
    fun GetAll(): List<Product>

    @Query("SELECT * FROM Product WHERE uid = :id")
    fun getById(id: Int): Product?

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)
}
