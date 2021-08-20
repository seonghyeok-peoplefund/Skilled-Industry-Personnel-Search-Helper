package com.ray.personnel.ui.mainpage.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.ray.personnel.databinding.FragmentCompanyFilterBinding
import androidx.lifecycle.Observer
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.R
import com.ray.personnel.domain.preference.PreferenceManager
import com.ray.personnel.ui.mainpage.filter.list.CompanyListFragment

class CompanyFilterFragment : Fragment() {
    private var _binding: FragmentCompanyFilterBinding? = null
    private val binding get() = _binding!!
    private val model: CompanyFilterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCompanyFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        model.progressCurrent.observe(viewLifecycleOwner) { id -> binding.progress.progress = id }
        model.progressMax.observe(viewLifecycleOwner) { id -> binding.progress.max = id }
        model.permissionRequested.observe(viewLifecycleOwner) {
            TODO("권한 요청")
        }
        model.moveToNextFragment.observe(viewLifecycleOwner) { move ->
            if (move) moveToNextFragment()
        }
        model.toastMessage.observe(viewLifecycleOwner) { msg ->
            if (context != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToNextFragment() {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.activity_slide_in,
            R.anim.activity_slide_out,
            R.anim.activity_slide_enter,
            R.anim.activity_slide_exit
        )
        val token = model.loginToken.value
            ?: if (context != null) PreferenceManager.getString(requireContext(), KEY_TOKEN) else ""
        transaction.replace(R.id.container, CompanyListFragment.newInstance(token))
            .addToBackStack(null)
        transaction.commit()
    }

    companion object {
        private fun getInitialArgumentsBundle(token: String): Bundle {
            return Bundle().apply {
                putString(KEY_TOKEN, token)
            }
        }

        fun newInstance(token: String): CompanyFilterFragment {
            return CompanyFilterFragment().apply {
                arguments = getInitialArgumentsBundle(token)
            }
        }
    }
}

