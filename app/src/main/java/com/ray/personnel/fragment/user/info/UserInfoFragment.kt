package com.ray.personnel.fragment.user.info



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.R
import com.ray.personnel.SupportActivity
import com.ray.personnel.databinding.FragmentUserInfoBinding
import com.ray.personnel.fragment.FragmentChangeInterface
import com.ray.personnel.fragment.company.CompanyFilterFragment
import com.ray.personnel.utils.Constants
import com.ray.personnel.utils.PreferenceManager
import com.ray.personnel.viewmodel.user.info.UserInfoViewModel


class UserInfoFragment : Fragment(), FragmentChangeInterface {
    override var isAttached: MutableLiveData<Any?> = MutableLiveData()
    lateinit var ctx: Context
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    override val model: UserInfoViewModel by activityViewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        isAttached.value = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var webview = binding.webview
        webview.getSettings().setJavaScriptEnabled(true)
        webview.setWebViewClient(WebViewClient())
        webview.webViewClient = WebClient()
        webview.loadUrl(Constants.WANTED_LOGIN)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class WebClient: WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            getCookie(view?.url, Constants.TOKEN)?.let{ token ->
                PreferenceManager.setString(ctx, Constants.TOKEN, token)
                model.curFragment.value = CompanyFilterFragment()
                (activity as SupportActivity).binding.bottomNavigationbar.selectedItemId = R.id.icon_company
            }
            return true
        }

        fun getCookie(siteName: String?, CookieName: String?): String? {
            var CookieValue: String? = null
            val cookieManager = CookieManager.getInstance()
            val cookies = cookieManager.getCookie(siteName)
            if (cookies != null) {
                val temp = cookies.split(";").toTypedArray()
                for (ar1 in temp) {
                    if (ar1.contains(CookieName!!)) {
                        val temp1 = ar1.split("=").toTypedArray()
                        CookieValue = temp1[1]
                    }
                }
            }
            return CookieValue
        }
    }
}
