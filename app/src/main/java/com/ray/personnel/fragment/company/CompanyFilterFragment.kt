package com.ray.personnel.fragment.company

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.company.CompanyOccupation
import com.ray.personnel.utils.parser.CompanyListParser
import com.ray.personnel.databinding.FrgCompanyFilterBinding
import com.ray.personnel.fragment.FragmentChangeInterface
import com.ray.personnel.viewmodel.company.filter.CompanyFilterViewModel
import androidx.lifecycle.Observer
import com.ray.personnel.utils.Constants
import com.ray.personnel.utils.PreferenceManager


class CompanyFilterFragment : Fragment(), FragmentChangeInterface {
    override var isAttached: MutableLiveData<Any?> = MutableLiveData()



    lateinit var ctx: Context
    private var _binding: FrgCompanyFilterBinding? = null
    private val binding get() = _binding!!
    override val model: CompanyFilterViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        isAttached.value = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        _binding = FrgCompanyFilterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        model.checkLogin()
        model.progress_cur.observe(viewLifecycleOwner, Observer<Int>{ id  -> binding.progress.progress = id })
        model.progress_max.observe(viewLifecycleOwner, Observer<Int>{ id  -> binding.progress.max = id })

        var permissionGPS = arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        val permissionObserver = Observer<List<String>> { permissions ->
            permissions.forEach{ permission ->
                permissionGPS.remove(permission)
                if(permissionGPS.size == 0) {
                    model.find = true
                    return@Observer
                }
            }
        }
        model.permissionResult.observe(viewLifecycleOwner, permissionObserver)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

