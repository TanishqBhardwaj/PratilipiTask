package com.example.task.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.task.R
import com.example.task.local.Data
import com.example.task.local.Database
import com.example.task.repository.DataRepository
import com.example.task.viewModel.DataViewModel
import com.example.task.viewModel.DataViewModelFactory

class InputActivity : AppCompatActivity() {

    private lateinit var editTextTitle : EditText
    private lateinit var editTextDescription : EditText
    private lateinit var imageView: ImageView
    private lateinit var buttonSave: Button

    private lateinit var title : String
    private lateinit var description : String

    private var imageUri: Uri? = null

    private lateinit var viewModel : DataViewModel
    private var data : Data? = null
    private var isUpdate = false

    private val selectImageFromGalleryResult = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = uri
            imageView.setImageURI(uri) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        setView()
        setListeners()
        setData()

    }

    private fun setView() {
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        imageView = findViewById(R.id.imageView)
        buttonSave = findViewById(R.id.buttonSave)

        val dataRepository = DataRepository(Database(this))
        val viewModelProviderFactory = DataViewModelFactory(dataRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(DataViewModel::class.java)
    }

    private fun setListeners() {

        imageView.setOnClickListener {
            selectImageFromGalleryResult.launch("image/*")
        }

        buttonSave.setOnClickListener {
            title = editTextTitle.text.toString()
            description = editTextDescription.text.toString()
            if(!validateTitleDescription()) {
                return@setOnClickListener
            }
            if(isUpdate) {
                data?.let { it1 -> viewModel.updateData(Data(title, description, imageUri.toString(), it1.id)) }
            }
            else {
                viewModel.saveData(Data(title, description, imageUri.toString()))
            }
            finish()
        }
    }

    private fun validateTitleDescription() : Boolean {
        if(title.isEmpty()) {
            showToast("Title Is Empty!")
            return false
        }
        if(description.isEmpty()) {
            showToast("Description Is Empty")
            return false
        }
        return true
    }

    private fun setData() {
        data = intent.getSerializableExtra("data") as Data?
        if(data!=null) {
            isUpdate = true
            editTextTitle.setText(data!!.title)
            editTextDescription.setText(data!!.description)
            Glide.with(this).load(data!!.image).into(imageView)
        }
        else {
            isUpdate = false
        }
    }

    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}