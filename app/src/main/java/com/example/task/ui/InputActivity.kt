package com.example.task.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.task.R
import com.example.task.local.Data
import com.example.task.local.Database
import com.example.task.repository.DataRepository
import com.example.task.viewModel.DataViewModel
import com.example.task.viewModel.DataViewModelFactory

class InputActivity : AppCompatActivity() {

    lateinit var editTextTitle : EditText
    lateinit var editTextDescription : EditText
    lateinit var imageView: ImageView
    lateinit var buttonSave: Button

    private lateinit var title : String
    private lateinit var description : String
    private var id : Int = 0
    private lateinit var imageUrl : String

    private val pickImage = 100
    private var imageUri: Uri? = null

    private lateinit var viewModel : DataViewModel

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
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        buttonSave.setOnClickListener {
            title = editTextTitle.text.toString()
            description = editTextDescription.text.toString()
            if(!title.isNotEmpty()) {
                showToast("Title Is Empty!")
                return@setOnClickListener
            }
            if(!description.isNotEmpty()) {
                showToast("Description Is Empty")
                return@setOnClickListener
            }
            viewModel.saveData(Data(title, description, imageUri.toString()))
            finish()
        }
    }

    private fun setData() {
        title = intent.getStringExtra(TITLE).toString()
        description = intent.getStringExtra(DESCRIPTION).toString()
        imageUrl = intent.getStringExtra(IMAGE).toString()
        id = intent.getIntExtra(ID, 0)

        if(id!=0) {
            editTextTitle.setText(title)
            editTextDescription.setText(description)
            Glide.with(this).load(imageUrl).into(imageView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val IMAGE = "image"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val ID = "id"
    }
}