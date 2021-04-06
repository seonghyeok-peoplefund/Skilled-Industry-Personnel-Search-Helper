package com.ray.personnel.fragment.company

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.UiThread
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kaopiz.kprogresshud.KProgressHUD
import com.ray.personnel.Global
import com.ray.personnel.SupportActivity
import com.ray.personnel.company.Company
import com.ray.personnel.model.database.CompanyDatabase
import com.ray.personnel.company.CompanyOccupation
import com.ray.personnel.company.Location
import com.ray.personnel.model.parser.CompanyListParser
import com.ray.personnel.R
import com.ray.personnel.databinding.CompanyFilterBinding
import com.ray.personnel.databinding.CompanyListBinding
import com.ray.personnel.fragment.FragmentChangeInterface
import com.ray.personnel.viewmodel.company.filter.CompanyFilterViewModel
import com.ray.personnel.viewmodel.company.list.CompanyListViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.*


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
        initAdapter(view)
        binding.viewmodel = model
        binding.lifecycleOwner = this
    }

    fun initAdapter(view: View){

        view.findViewById<Spinner>(R.id.sp1).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, CompanyOccupation.occupation.keys.toList())
        view.findViewById<Spinner>(R.id.sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("<비어 있음>"))
        view.findViewById<Spinner>(R.id.sp1).onItemSelectedListener = object: OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                view.findViewById<Spinner>(R.id.sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, (CompanyOccupation.occupation[parent.getItemAtPosition(position).toString()]!!).keys.toList())

            }

        }
        view.findViewById<Spinner>(R.id.sp2).onItemSelectedListener = object: OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                println(CompanyOccupation.occupation[view.findViewById<Spinner>(R.id.sp1).selectedItem.toString()]?.get(parent.getItemAtPosition(position).toString()))
            }

        }/*
        view.findViewById<Spinner>(R.id.sp1).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("<비어 있음>"))
        view.findViewById<Spinner>(R.id.sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("<비어 있음>"))*/
        view.findViewById<Spinner>(R.id.sp3).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("원티드"))
        view.findViewById<Spinner>(R.id.sp4).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("서울"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

