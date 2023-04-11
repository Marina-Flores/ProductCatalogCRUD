package com.example.productcatalogcrud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.productcatalog.crud.data.AppDataBase
import com.example.productcatalog.crud.data.dao.ProductDAO
import com.example.productcatalog.crud.data.models.Product
import com.example.productcatalogcrud.databinding.FragmentProductFormBinding

class ProductFormFragment : Fragment() {
    private lateinit var binding : FragmentProductFormBinding
    private lateinit var db: AppDataBase
    private lateinit var productDao: ProductDAO
    private var productId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDataBase::class.java, "product_catalog_database"
        ).allowMainThreadQueries().build()

        productDao = db.productDao()

        binding = FragmentProductFormBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            productId = it.getInt("PRODUCT_ID", -1)
        }
        binding.saveProduct.setOnClickListener{
            productId ?: saveProduct()
            productId?.let { id -> updateProduct(id) }
        }

        productId?.let{
            val product = productDao.getById(it)
            binding.name.setText(product?.name)
            binding.description.setText(product?.description)
            binding.price.setText(product?.price.toString())
        }
    }

    private fun saveProduct()
    {
        val navController = findNavController()

        var name = binding.name.text.toString()
        var description = binding.description.text.toString()
        var price = binding.price.text.toString()

        var product = Product(null, name, description, price.toDouble())
        productDao.Insert(product)

        clearInputs()
        navController.navigateUp()
    }

    private fun updateProduct(id: Int)
    {
        val navController = findNavController()

        var name = binding.name.text.toString()
        var description = binding.description.text.toString()
        var price = binding.price.text.toString()

        var product = Product(id, name, description, price.toDouble())
        productDao.update(product)

        clearInputs()
        navController.navigateUp()
    }

    private fun clearInputs()
    {
        binding.name.setText("")
        binding.description.setText("")
        binding.price.setText("")
    }
}