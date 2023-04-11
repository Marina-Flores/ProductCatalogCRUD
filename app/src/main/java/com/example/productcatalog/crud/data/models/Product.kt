package com.example.productcatalog.crud.data.models

import android.text.Editable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DecimalFormat

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val uid: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "price")
    val price: Double
)