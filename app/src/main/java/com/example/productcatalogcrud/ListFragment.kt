package com.example.productcatalogcrud

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.productcatalog.crud.data.AppDataBase
import com.example.productcatalog.crud.data.dao.ProductDAO
import com.example.productcatalogcrud.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var db: AppDataBase
    private lateinit var productDao: ProductDAO
    private lateinit var listView: ListView
    private lateinit var navigation: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDataBase::class.java, "product_catalog_database"
        ).allowMainThreadQueries().build()

        productDao = db.productDao()
        navigation = findNavController()
        binding = FragmentListBinding.inflate(inflater, container, false)
        listView = binding.listView

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addProduct.setOnClickListener {
            navigation.navigate(R.id.productFormFragment)
        }
        productList()
    }

    private fun productList(){
        val list = productDao.GetAll()
        val productList = mutableListOf<String>()
        val productIdList = mutableListOf<Int>()

        for (product in list)
        {
            val productString = "${product.name} - ${product.description} - $${product.price}"
            product.uid?.let { productList.add(productString) }
            product.uid?.let { productIdList.add(it) }
        }

        val resource = android.R.layout.simple_list_item_1
        val adapter: ArrayAdapter<String>
        val context = activity?.baseContext as Context

        adapter = ArrayAdapter(context, resource, productList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Obtenha o ID do produto selecionado
            val productId = productIdList[position]

            // Chame a tela de edição para o produto selecionado
            val bundle = Bundle()
            bundle.putInt("PRODUCT_ID", productId)
            navigation.navigate(R.id.productFormFragment, bundle)
        }

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener() { _, _, position, _ ->
            // Obtenha o ID do produto selecionado
            val productId = productIdList[position]

            // Chame a tela de edição para o produto selecionado
            showConfirmationDialog(productId, position)
            true
        }
    }

    private fun showConfirmationDialog(productId: Int, position: Int){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(R.string.delete_title)
        alertDialogBuilder.setMessage(R.string.delete_confirm)

        alertDialogBuilder.setPositiveButton(R.string.confirm_btn) { _, _ ->
            deleteProduct(productId, position)
        }
        alertDialogBuilder.setNegativeButton(R.string.decline_btn) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    private fun deleteProduct(productId: Int, position: Int){
        val product = productDao.getById(productId)
        product?.let {
            productDao.delete(product)
            val adapter = listView.adapter as ArrayAdapter<String>
            adapter.remove(adapter.getItem(position))
            adapter.notifyDataSetChanged()
        }
    }
}