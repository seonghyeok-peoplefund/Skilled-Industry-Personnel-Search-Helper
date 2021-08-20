package com.ray.personnel.ui.mainpage

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.R
import com.ray.personnel.databinding.ActivitySupportLayoutBinding
import com.ray.personnel.domain.preference.PreferenceManager
import com.ray.personnel.ui.mainpage.favorite.FavoriteListFragment
import com.ray.personnel.ui.mainpage.filter.CompanyFilterFragment
import com.ray.personnel.ui.mainpage.login.LoginFragment

class SupportActivity : AppCompatActivity() {
    private val binding: ActivitySupportLayoutBinding by lazy { ActivitySupportLayoutBinding.inflate(layoutInflater) }
    private val model: SupportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        model.selectedNavItemId.observe(this) { itemId ->
            val token = PreferenceManager.getString(this, KEY_TOKEN)
            when (itemId) {
                R.id.icon_company -> {
                    loadFragmentAnimation(CompanyFilterFragment.newInstance(token))
                }
                R.id.icon_account -> {
                    loadFragmentAnimation(LoginFragment.newInstance())
                }
                R.id.icon_favorite -> {
                    loadFragmentAnimation(FavoriteListFragment.newInstance(token))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top, menu)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // TODO("fragment 에게 어떻게 전달?")
    }

    private fun loadFragmentAnimation(destination: Fragment) {
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
    }

    private fun requestPermission(vararg permissions: String): List<String> {
        val result = mutableListOf<String>()
        var required = false
        permissions.forEach { permission ->
            val hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                //거부한 적이 있다.
                if (!required) {
                    val alreadyDenied = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                    if (alreadyDenied) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
                    }
                    required = true
                }
            } else {
                //거부한적 없다.
                result.add(permission)
            }
        }
        return result
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 100
    }
}