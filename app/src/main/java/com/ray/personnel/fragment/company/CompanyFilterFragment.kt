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
import com.ray.personnel.databinding.CompanyFilterBinding
import com.ray.personnel.fragment.FragmentChangeInterface
import com.ray.personnel.viewmodel.company.filter.CompanyFilterViewModel
import androidx.lifecycle.Observer
import com.ray.personnel.utils.Constants
import com.ray.personnel.utils.PreferenceManager


class CompanyFilterFragment : Fragment(), FragmentChangeInterface {
    override var isAttached: MutableLiveData<Any?> = MutableLiveData()



    lateinit var ctx: Context
    private var _binding: CompanyFilterBinding? = null
    private val binding get() = _binding!!
    override val model: CompanyFilterViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        isAttached.value = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        _binding = CompanyFilterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        initAdapter(view)
        model.progress_cur.observe(viewLifecycleOwner, Observer<Int>{ id  -> binding.progress.progress = id })
        model.progress_max.observe(viewLifecycleOwner, Observer<Int>{ id  -> binding.progress.max = id })


        if(PreferenceManager.getString(ctx, Constants.TOKEN).isNullOrEmpty()) {
            model.warningColor.value = (0xff shl 24) or 0xff2020
            model.warningText.value = "주의 : Wanted로그인이 되어있지 않습니다.\n연봉 및 규모 검색기능이 비활성화됩니다."
        }
        else {
            model.warningColor.value = (0xff shl 24) or 0x000000
            model.warningText.value = "Wanted가 로그인되어있습니다."
        }



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

    fun initAdapter(view: View){

        var position1 = PreferenceManager.getInt(ctx, Constants.JOB)
        var position2 = PreferenceManager.getInt(ctx, Constants.JOB_CLASSIFIED)
        if(position1 == -1) position1 = 0
        if(position2 == -1) position2 = 0
        if(binding.jobs1.isNullOrEmpty()) {
            binding.jobs1 = CompanyOccupation.occupation.keys.toList()
            binding.sp1.post { binding.sp1.setSelection(position1); }
        }
        if(binding.jobs2.isNullOrEmpty()) {
            binding.jobs2 = CompanyOccupation.occupation[CompanyOccupation.occupation.keys.toList()[position1]]!!.keys.toList()
            binding.sp2.post { binding.sp2.setSelection(position2);}
        }

        if(position1 > 0 && position2 > 0) CompanyListParser.sortType = CompanyOccupation.occupation[CompanyOccupation.occupation.keys.toList()[position1]]!![binding.jobs2!![position2]!!]!!
        binding.sp1.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                if(position > 0) {
                    binding.jobs2 = (CompanyOccupation.occupation[parent.getItemAtPosition(position).toString()]!!).keys.toList()
                    PreferenceManager.setInt(ctx, Constants.JOB, position)
                    CompanyListParser.sortType = CompanyOccupation.occupation[binding.sp1.selectedItem.toString()]!![binding.jobs2!![0]!!]!!
                }
                println(PreferenceManager.getInt(ctx, Constants.JOB).toString()+" but "+position)
                println(PreferenceManager.getInt(ctx, Constants.JOB_CLASSIFIED))
                println(CompanyListParser.sortType)
            }
        }
        binding.sp2.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                if(position > 0) {
                    CompanyListParser.sortType = CompanyOccupation.occupation[binding.sp1.selectedItem.toString()]!![binding.jobs2!![position]!!]!!
                    PreferenceManager.setInt(ctx, Constants.JOB_CLASSIFIED, position)
                }
                println(PreferenceManager.getInt(ctx, Constants.JOB))
                println(PreferenceManager.getInt(ctx, Constants.JOB_CLASSIFIED).toString()+" but "+position)
                println(CompanyListParser.sortType)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

