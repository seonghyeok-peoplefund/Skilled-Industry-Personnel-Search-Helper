package com.ray.personnel.ui.mainpage.favorite

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ray.personnel.databinding.FragmentFavoriteListBinding
import com.ray.personnel.ui.mainpage.FragmentChangeInterface
import com.ray.personnel.ui.mainpage.filter.list.CompanyListAdapter


class FavoriteListFragment() : Fragment(), FragmentChangeInterface {
    lateinit var ctx: Context
    override var isAttached: MutableLiveData<Any?> = MutableLiveData()
    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!
    override val model: FavoriteListViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        isAttached.value = null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
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
        with(binding.companyListRecyclerview){
            layoutManager = GridLayoutManager(ctx, 2)
            addItemDecoration(getGridDecoration())
            adapter = FavoriteListAdapter(ctx, emptyList())
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