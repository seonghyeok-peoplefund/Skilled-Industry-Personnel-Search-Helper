package com.ray.personnel.ui.companyinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ray.personnel.R
import com.ray.personnel.data.Company
import com.ray.personnel.databinding.ActivityCompanyInfoBinding
import com.ray.personnel.domain.parser.NaverParser
import io.reactivex.disposables.Disposable

class CompanyInfoActivity : AppCompatActivity() {
    private val binding: ActivityCompanyInfoBinding by lazy { ActivityCompanyInfoBinding.inflate(layoutInflater) }

    private val model: CompanyInfoViewModel by viewModels()

    private val beDisposed = mutableListOf<Disposable>()

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        initObserver()
    }
    //endregion Lifecycle

    //region initialize
    private fun initObserver() {
        model.uriLiveData.observe(this) { str ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
            startActivity(intent)
        }
        model.company.observe(this) { company ->
            setToolbar(company)
            setBackgroundImage(company)
            initRecyclerView(company)

            beDisposed.add(
                NaverParser.getNaverNews(
                    content = company.title,
                    onSuccess = { arr ->
                        company.news = arr
                        (binding.companyInfo.adapter as CompanyInfoAdapter).refresh(CompanyInfoAdapter.NEWS)
                    },
                    onError = { err -> // 원래라면 한 줄에 들어가야 하는 내용이지만, value parameter이 같은 곳에서 시작해야 한다고 생각했기 때문에 줄바꿈을 했다.
                        Log.e(TAG, "인터넷 연결이 올바르지 않습니다.", err)
                        Toast.makeText(
                            this,
                            "인터넷 연결이 올바르지 않습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            )
        }
    }

    private fun initRecyclerView(company: Company) {
        binding.companyInfo.also { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CompanyInfoAdapter(company).apply {
                onTitleClickListener = model.onTitleClickListener
                onLocationClickListener = model.onLocationClickListener
                onScaleClickListener = model.onScaleClickListener
                onSalaryClickListener = model.onSalaryClickListener
                onNewsClickListener = model.onNewsClickListener
            }

        }
    }
    //endregion initialize

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.activity_slide_enter, R.anim.activity_slide_exit)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.activity_slide_enter, R.anim.activity_slide_exit)
    }

    private fun setToolbar(company: Company) {
        with(binding.toolbar) {
            title = company.title
            setSupportActionBar(this)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setBackgroundImage(company: Company) {
        Glide.with(this)
            .asBitmap()
            .load(Uri.parse(company.thumbUrl))
            .thumbnail(0.3f)
            .into(model.targetBitmap)
    }

    companion object {
        private const val TAG = "CompanyInfoActivity"
    }
}