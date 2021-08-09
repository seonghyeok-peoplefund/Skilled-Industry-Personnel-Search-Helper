package com.ray.personnel.fragment.company

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.ray.personnel.Global.gson
import com.ray.personnel.company.Company
import com.ray.personnel.R
import com.ray.personnel.viewmodel.company.info.CompanyInfoAdapter
import com.ray.personnel.databinding.ActivityCompanyInfoBinding

class CompanyInfo : AppCompatActivity() {
    val activity: ActivityCompanyInfoBinding by lazy{ ActivityCompanyInfoBinding.inflate(layoutInflater) }
    val company: Company by lazy{ getCompanyFromIntent() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity.root)

        setToolbar()
        setBackgroundImage()
        setRecyclerView()
        loadInformation()
        //activity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }


    private fun getCompanyFromIntent(): Company {
        return gson.fromJson(intent.getStringExtra("Company"), Company::class.java)
    }

    private fun setRecyclerView() {
        with(activity.companyInfo){
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@CompanyInfo)
            adapter = CompanyInfoAdapter(this@CompanyInfo, company)
        }
    }

    private fun setToolbar(){
        with(activity.toolbar) {
            title = company.title
            setSupportActionBar(this)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }



    private fun loadInformation(){

        company.observableNews.subscribe({ arr ->
            company.news = arr
            (activity.companyInfo.adapter as CompanyInfoAdapter).refresh(CompanyInfoAdapter.NEWS)
        }, {err -> Toast.makeText(this, "인터넷 연결이 올바르지 않습니다.", Toast.LENGTH_SHORT).show(); Toast.makeText(this, err.toString(), Toast.LENGTH_LONG).show()
        }
        )
    }
    // Set the background and text colors of a toolbar given a
    // bitmap image to match
    fun setBackgroundImage() {
        Glide.with(this)
                .asBitmap()
                .load(Uri.parse(company.thumbURL))
                .thumbnail(0.3f)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        activity.companyImage.setImageBitmap(resource)
                        Palette.from(resource).generate { palette ->
                            var backColor = getColorFromDarkToBright(
                                    palette?.lightVibrantSwatch?.rgb ?:
                                    palette?.lightMutedSwatch?.rgb ?:
                                    palette?.vibrantSwatch?.rgb ?:
                                    palette?.mutedSwatch?.rgb ?: Color.WHITE)
                            activity.toolbarLayout.setContentScrimColor(backColor)
                            activity.toolbarLayout.setStatusBarScrimColor(backColor)
                            //activity.toolbar.setBackgroundColor(backColor)
                        }
                    }
                })
    }
    fun getColorFromDarkToBright(_color: Int, alpha: Int = 0xff): Int{
        var color = _color
        var red = (color shr 16) and 0xff
        var green = (color shr 8) and 0xff
        var blue = color and 0xff
        var brightness = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
        var time = 0;
        while(brightness < 0.5){
            System.out.println("너무 어두워!!!"+Integer.toHexString(color))
            System.out.println("RED : $red GREEN : $green BLUE : $blue -> BRIGHTNESS : $brightness")
            red += ((0xff - red) * 0.299).toInt()
            green += ((0xff - green) * 0.587).toInt()
            blue += ((0xff - blue) * 0.114).toInt()
            color = (alpha shl 24) or (red shl 16) or (green shl 8) or (blue shl 0)
            brightness = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
            time ++;
            if(time > 15) break
        }
        System.out.println("결과 -> "+Integer.toHexString(color))
        return color
    }
    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top, menu)

        with((menu!!.findItem(R.id.search).actionView) as SearchView){
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String): Boolean {
                    (activity.companyInfo.adapter as CompanyInfoAdapter).filter(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            })
        }
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            /*
            R.id.search -> {
                return true
            }
            R.id.menu -> {
                activity.drawer.openDrawer(Gravity.RIGHT)
                return true
            }*/
            android.R.id.home ->{
                finish()
                overridePendingTransition(R.anim.activity_slide_enter,R.anim.activity_slide_exit)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.activity_slide_enter,R.anim.activity_slide_exit)
    }

}