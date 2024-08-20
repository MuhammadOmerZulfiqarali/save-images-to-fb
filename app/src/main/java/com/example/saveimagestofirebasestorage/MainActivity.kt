package com.example.saveimagestofirebasestorage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var storageReference: StorageReference
    private lateinit var selectedImageUri: Uri
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storageReference = FirebaseStorage.getInstance().reference
        imageView = findViewById(R.id.imageView)
        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        val uploadImageButton = findViewById<Button>(R.id.uploadImageButton)

        selectImageButton.setOnClickListener {
            // Open gallery to select image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }

        uploadImageButton.setOnClickListener {
            uploadImageToFirebase()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data!!
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToFirebase() {
        val fileName = UUID.randomUUID().toString()
        val imageRef = storageReference.child("images/$fileName.jpg")

        imageRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                Log.d("Upload", "Image uploaded successfully")
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.e("Upload", "Failed to upload image", it)
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }
}
