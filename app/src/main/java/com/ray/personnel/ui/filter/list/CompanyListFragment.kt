package com.ray.personnel.ui.filter.list

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.databinding.FragmentCompanyListBinding
import com.ray.personnel.ui.favorite.FavoriteListAdapter

//TODO("Favorite 작업 완료하고, 합치거나 코드를 가져오거나 할거임. 그 때까지 보류")
class CompanyListFragment : Fragment() {
    private var _binding: FragmentCompanyListBinding? = null
    private val binding get() = _binding!!
    private val model: CompanyListViewModel by viewModels()

    //region Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCompanyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        binding.viewmodel = model
        binding.lifecycleOwner = this
        model.getAllByDistanceAsc()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion Lifecycle

    //region initialize
    private fun initRecyclerView() {
        with(binding.companyListRecyclerview) {
            if (context != null) layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(getGridDecoration())
            adapter = CompanyListAdapter(emptyList()).apply {
                isLogined = model.loginToken.value?.isNotEmpty() ?: false
                onItemClickListener = model.onItemClickListener
                onLikeListener = model.onLikeListener
            }
        }
    }
    //endregion initialize

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

    companion object {
        private fun getInitialArgumentsBundle(token: String): Bundle {
            return Bundle().apply {
                putString(KEY_TOKEN, token)
            }
        }

        fun newInstance(token: String): CompanyListFragment {
            return CompanyListFragment().apply {
                arguments = getInitialArgumentsBundle(token)
            }
        }
    }
}
