package com.ray.personnel.Activity

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ray.personnel.Activity.CompanyActivity.CompanyFilterFragment
import com.ray.personnel.R
import com.ray.personnel.databinding.SupportLayoutBinding


open class SupportActivity : AppCompatActivity() {

    val activity: SupportLayoutBinding by lazy { SupportLayoutBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity.root)
        setToolbar()
        setTabLayout()
        loadFragment(CompanyFilterFragment())
    }

    fun setTabLayout(){
        activity.nav.run{
            this.setOnNavigationItemSelectedListener{item ->
                when(item.itemId){
                    R.id.icon_company -> loadFragment(CompanyFilterFragment())
                    R.id.icon_account -> loadFragment(NullFragment())
                    R.id.icon_note -> loadFragment(NullFragment())
                    else -> loadFragment(NullFragment())
                }
                true
            }
        }
    }

    fun setToolbar(){
        with(activity.toolbar) {
            setSupportActionBar(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top, menu)
        with((menu!!.findItem(R.id.search).actionView) as SearchView){
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.search -> {
                true
            }
            R.id.menu -> {
                findViewById<DrawerLayout>(R.id.drawer).openDrawer(Gravity.RIGHT)
                true
            }
            android.R.id.home ->{
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun loadFragment(destination: Fragment, element: View? = null){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                .replace(R.id.container, destination)
                .addToBackStack(null)
        if(element != null) transaction.addSharedElement(element, ViewCompat.getTransitionName(element)!!)
        transaction.commit()
    }
    fun loadFragment(destination: Fragment, isForward: Boolean){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if(isForward) transaction.setCustomAnimations(R.anim.activity_slide_in, R.anim.activity_slide_out, R.anim.activity_slide_enter, R.anim.activity_slide_exit)
            //transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, R.anim.fragment_right, R.anim.fragment_left)
        else overridePendingTransition(R.anim.activity_slide_enter,R.anim.activity_slide_exit)
        transaction.replace(R.id.container, destination)
                .addToBackStack(null)
        transaction.commit()
        /*
        setCustomAnimations(android.R.anim.fragment_left, android.R.anim.fragment_right,
                R.anim.fragment_right, R.anim.fragment_left);
         */
    }
}