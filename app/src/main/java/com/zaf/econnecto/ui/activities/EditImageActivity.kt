package com.zaf.econnecto.ui.activities

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.zaf.econnecto.R
import com.zaf.econnecto.model.ImageUpdateModelListener
import com.zaf.econnecto.ui.presenters.EditImagePresenter
import com.zaf.econnecto.ui.presenters.operations.IEditImage
import kotlinx.android.synthetic.main.activity_edit_image.*
import kotlinx.coroutines.launch
import java.io.File

class EditImageActivity : BaseActivity<EditImagePresenter?>(), IEditImage{

    private var stringUri: Uri? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)
        val stringPath = intent.getStringExtra("imagePath")
        val file = File(stringPath)
        stringUri =  Uri.fromFile(File(stringPath))
        imgView.setImageURI(stringUri)
        try {
            Handler().postDelayed(Runnable {
                 bitmap = MediaStore.Images.Media.getBitmap(contentResolver, stringUri)
                //  bitmap = BitmapUtils.getBitmap(this,stringUri)
                imgView1.setImageBitmap(bitmap)
            }, 100)

        } catch (e: Exception) {
            e.stackTrace
        }
        //imgView.setImageBitmap(bitmap)
        updateActionbar()

        btnCancel.setOnClickListener {
            onBackPressed()
        }
        btnUpload.setOnClickListener {
            lifecycleScope.launch {
                presenter!!.uploadBitmap(bitmap,file)
            }

        }
    }

    private fun updateActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbarBd)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.editImage)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
       // supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toolbar.setNavigationOnClickListener { //finish();
            onBackPressed()
        }
    }

    override fun initPresenter(): EditImagePresenter? {
        return EditImagePresenter(this,this)
    }

    override fun onUploadError(error: String) {

    }

    override fun onUploadSuccess() {
        ImageUpdateModelListener.getInstance().changeState(true)
        onBackPressed()
    }


}
