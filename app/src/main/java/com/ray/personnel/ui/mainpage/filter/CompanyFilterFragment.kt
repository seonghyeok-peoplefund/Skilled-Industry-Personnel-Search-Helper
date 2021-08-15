package com.ray.personnel.ui.mainpage.filter

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.databinding.FragmentCompanyFilterBinding
import com.ray.personnel.ui.mainpage.FragmentChangeInterface
import androidx.lifecycle.Observer


class CompanyFilterFragment : Fragment(), FragmentChangeInterface {
    override var isAttached: MutableLiveData<Any?> = MutableLiveData()



    lateinit var ctx: Context
    private var _binding: FragmentCompanyFilterBinding? = null
    private val binding get() = _binding!!
    override val model: CompanyFilterViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        isAttached.value = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        _binding = FragmentCompanyFilterBinding.inflate(inflater, container, false)
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

