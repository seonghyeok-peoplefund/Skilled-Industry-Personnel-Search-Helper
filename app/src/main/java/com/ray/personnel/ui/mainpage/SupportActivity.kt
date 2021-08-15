package com.ray.personnel.ui.mainpage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ray.personnel.Global
import com.ray.personnel.R
import com.ray.personnel.databinding.ActivitySupportLayoutBinding


class SupportActivity : AppCompatActivity() {

    val binding: ActivitySupportLayoutBinding by lazy { ActivitySupportLayoutBinding.inflate(layoutInflater) }
    val model: SupportViewModel by viewModels()
    lateinit var curFrg: FragmentChangeInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        val navObserver = Observer<Fragment> { frg ->
            let {
                loadFragmentAnimation(frg) }
        }
        model.curFragment.observe(this, navObserver)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top, menu)
        //return model.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)
            = model.onOptionsItemSelected(item)?: super.onOptionsItemSelected(item)




    fun loadFragment(destination: Fragment, element: View? = null){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                .replace(R.id.container, destination)
                .addToBackStack(null)
        if(element != null) transaction.addSharedElement(element, ViewCompat.getTransitionName(element)!!)
        transaction.commit()
        if(destination is FragmentChangeInterface) observeFragment(destination)
    }
    fun loadFragmentAnimation(destination: Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.activity_slide_in,
            R.anim.activity_slide_out,
            R.anim.activity_slide_enter,
            R.anim.activity_slide_exit
        )
        transaction.replace(R.id.container, destination)
                .addToBackStack(null)
        transaction.commit()
        if(destination is FragmentChangeInterface) observeFragment(destination)
    }

    fun observeFragment(frg : FragmentChangeInterface){
        curFrg = frg
        val isAttached = Observer<Any?>{
            val navObserver = Observer<Fragment> { id -> let {
                loadFragmentAnimation(id)
                //frg.model.curFragment = MutableLiveData()
            } }
            frg.model.curFragment.observe(this, navObserver)
            frg.isAttached = MutableLiveData()
            val permissionObserver = Observer<List<String>>{ permissions ->
                frg.model.permissionResult.value = Global.requestPermission(this, *permissions.toTypedArray())
            }
            frg.model.permissionRequest.observe(this, permissionObserver)
        }
        frg.isAttached.observe(this, isAttached)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*
        val accepted = ArrayList<String>()
        val denied = ArrayList<String>()
        when (requestCode) {
            100 -> {
                if ((grantResults.isNotEmpty())) {
                    grantResults.forEachIndexed{ i, result ->
                        if(result == PackageManager.PERMISSION_GRANTED) accepted.add(permissions[i])
                        //else denied.add(permissions[i])
                    }
                }
            }
        }*/
        curFrg.model.permissionResult.value = permissions.toList()

    }


}