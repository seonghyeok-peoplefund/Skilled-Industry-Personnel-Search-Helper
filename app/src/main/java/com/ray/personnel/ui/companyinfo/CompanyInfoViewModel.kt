package com.ray.personnel.ui.companyinfo

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.ray.personnel.Constants
import com.ray.personnel.data.Company
import com.ray.personnel.data.Location
import com.ray.personnel.data.News

class CompanyInfoViewModel(state: SavedStateHandle) : ViewModel() {
    val company = state.getLiveData<Company>("Company")

    val uriLiveData = MutableLiveData<String>()

    val companyImage = MutableLiveData<Bitmap>()

    val colorOfTheme = MutableLiveData<Int>()

    val onTitleClickListener = fun(company: Company) {
        uriLiveData.value = Constants.WANTED_RECRUIT + company.jobId
    }

    val onLocationClickListener = fun(location: Location) {
        uriLiveData.value = "https://www.google.co.kr/maps/@${location.geoLocation.latitude},${location.geoLocation.longitude},20z"
    }

    val onScaleClickListener = fun(company: Company) {
        uriLiveData.value = Constants.MILITARY_SEARCH + company.militaryUrl
    }

    val onSalaryClickListener = fun(company: Company) {
        uriLiveData.value = Constants.WANTED_INTRO + company.companyId
    }

    val onNewsClickListener = fun(news: News) {
        uriLiveData.value = news.url
    }

    val targetBitmap = object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            companyImage.value = resource
            Palette.from(resource)
                .generate { palette ->
                    colorOfTheme.value = getColorFromDarkToBright(
                        palette?.lightVibrantSwatch?.rgb
                            ?: palette?.lightMutedSwatch?.rgb
                            ?: palette?.vibrantSwatch?.rgb
                            ?: palette?.mutedSwatch?.rgb
                            ?: Color.WHITE
                    )
                }
        }
    }

    private fun getColorFromDarkToBright(color: Int, alpha: Int = 0xff): Int {
        var color = color
        var red = (color shr 16) and 0xff
        var green = (color shr 8) and 0xff
        var blue = color and 0xff
        var brightness = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
        var time = 0
        while (brightness < 0.5) {
            red += ((0xff - red) * 0.299).toInt()
            green += ((0xff - green) * 0.587).toInt()
            blue += ((0xff - blue) * 0.114).toInt()
            color = (alpha shl 24) or (red shl 16) or (green shl 8) or (blue shl 0)
            brightness = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
            time++
            if (time > 15) break
        }
        return color
    }
}