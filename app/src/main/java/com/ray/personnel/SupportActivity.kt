package com.ray.personnel

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.ray.personnel.viewmodel.SupportViewModel
import com.ray.personnel.databinding.SupportLayoutBinding
import com.ray.personnel.fragment.company.CompanyFilterFragment


open class SupportActivity : AppCompatActivity() {

    val binding: SupportLayoutBinding by lazy { SupportLayoutBinding.inflate(layoutInflater) }
    val model: SupportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)



        loadFragment(CompanyFilterFragment())
        val navObserver = Observer<Fragment>{id -> loadFragmentAnimation(id) }
        model.curFragment.observe(this, navObserver)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top, menu)
        return model.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem)
            = model.onOptionsItemSelected(item)?: super.onOptionsItemSelected(item)




    fun loadFragment(destination: Fragment, element: View? = null){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                .replace(R.id.container, destination)
                .addToBackStack(null)
        if(element != null) transaction.addSharedElement(element, ViewCompat.getTransitionName(element)!!)
        transaction.commit()
    }
    fun loadFragmentAnimation(destination: Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out, R.anim.activity_slide_enter, R.anim.activity_slide_exit)
        transaction.replace(R.id.container, destination)
                .addToBackStack(null)
        transaction.commit()
    }
}