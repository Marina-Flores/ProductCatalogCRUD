package com.example.productcatalog.crud.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productcatalog.crud.data.dao.ProductDAO
import com.example.productcatalog.crud.data.models.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun productDao() : ProductDAO
}