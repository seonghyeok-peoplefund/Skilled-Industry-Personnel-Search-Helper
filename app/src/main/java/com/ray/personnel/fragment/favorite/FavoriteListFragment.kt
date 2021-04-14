package com.ray.personnel.fragment.favorite

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ray.personnel.Global.gson
import com.ray.personnel.company.Company
import com.ray.personnel.utils.database.CompanyDatabase
import com.ray.personnel.R
import com.ray.personnel.databinding.CompanyListBinding
import com.ray.personnel.databinding.FavoriteListBinding
import com.ray.personnel.fragment.FragmentChangeInterface
import com.ray.personnel.utils.parser.CompanyListParser
import com.ray.personnel.viewmodel.company.list.CompanyListAdapter
import com.ray.personnel.viewmodel.company.list.CompanyListViewModel
import com.ray.personnel.viewmodel.favorite.FavoriteListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class FavoriteListFragment() : Fragment(), FragmentChangeInterface {
    lateinit var ctx: Context
    override var isAttached: MutableLiveData<Any?> = MutableLiveData()
    private var _binding: FavoriteListBinding? = null
    private val binding get() = _binding!!
    override val model: FavoriteListViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        isAttached.value = null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        _binding = FavoriteListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView(view)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        model.getAllByDistanceAsc()
    }

    private fun setRecyclerView(view: View) {
        with(binding.list){
            layoutManager = GridLayoutManager(ctx, 2)
            addItemDecoration(getGridDecoration())
            adapter = CompanyListAdapter(ctx, emptyList())
        }
    }

    private fun getGridDecoration(): ItemDecoration = object : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val maxCount = parent.adapter!!.itemCount
            val position = parent.getChildAdapterPosition(view)
            val spanCount = 2
            val spacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics).toInt()
            val outerMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()
            val column = position % spanCount
            val row = position / spanCount
            val lastRow = (maxCount - 1) / spanCount
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            outRect.top = spacing * 2
            if (row == lastRow) {
                outRect.bottom = outerMargin
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
