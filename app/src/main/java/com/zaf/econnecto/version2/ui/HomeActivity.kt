package com.zaf.econnecto.version2.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.zaf.econnecto.R
import com.zaf.econnecto.ui.activities.BaseActivity
import com.zaf.econnecto.ui.fragments.BizCategoryFragment
import com.zaf.econnecto.ui.presenters.MainPresenter
import com.zaf.econnecto.ui.presenters.operations.IMain
import com.zaf.econnecto.version2.ui.home.HomeFragment

class HomeActivity : BaseActivity<MainPresenter>(),IMain {

    private lateinit var mContext :Context
    private lateinit var navController: NavController
    override fun onLogoutCall() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAddBizFab(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateVerifyEmailUI() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateProfilePic(bitmap: Bitmap?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): MainPresenter {
        return MainPresenter(this,this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mContext = this

        initUI()

        moveToHome()
    }

    private fun moveToHome() {
        //presenter.moveToFragment(HomeFragment::class.java.simpleName)
    }

    private fun initUI() {
        navController = findNavController(R.id.navHomeFragment)

    }
}
