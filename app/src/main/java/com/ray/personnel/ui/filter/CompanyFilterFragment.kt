package com.ray.personnel.ui.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.R
import com.ray.personnel.databinding.FragmentCompanyFilterBinding
import com.ray.personnel.domain.LocationManager
import com.ray.personnel.domain.PreferenceManager
import com.ray.personnel.domain.database.CompanyDatabaseMethods
import com.ray.personnel.domain.parser.CompanyListParser
import com.ray.personnel.ui.filter.list.CompanyListFragment
import io.reactivex.disposables.Disposable

class CompanyFilterFragment : Fragment() {
    private var _binding: FragmentCompanyFilterBinding? = null
    private val binding get() = _binding!!
    private val model: CompanyFilterViewModel by viewModels()
    private val beDisposed = mutableListOf<Disposable>()

    //region Lifecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion Lifecycle

    //region initialize
    private fun initObserver() {
        model.progressCurrent.observe(viewLifecycleOwner) { id -> binding.progress.progress = id }
        model.progressMax.observe(viewLifecycleOwner) { id -> binding.progress.max = id }
        model.requestPermission.observe(viewLifecycleOwner) {
            TODO("권한 요청")
        }
        model.moveToNextFragment.observe(viewLifecycleOwner) { move ->
            if (move) moveToNextFragment()
        }
        model.toastMessage.observe(viewLifecycleOwner) { msg ->
            if (context != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
        model.requestLocation.observe(viewLifecycleOwner) { request ->
            if (request && context != null) {
                beDisposed.add(
                    LocationManager.getLocation(
                        context = requireContext(),
                        onSuccess = { loc -> //view에 접근해야해서 어쩔 수 없이...
                            model.latitude.value = loc?.latitude.toString()
                            model.longitude.value = loc?.longitude.toString()
                            binding.locationGps.doResult(true)
                        },
                        onError = { err ->
                            binding.locationGps.doResult(false)
                            Log.e(TAG, "위치를 가져오지 못했습니다.", err)
                        }
                    )
                )
            }

        }
        model.requestDatabaseGetMethod.observe(viewLifecycleOwner) { options ->
            if (context != null) {
                beDisposed.add(
                    CompanyDatabaseMethods.getDataByUsingOptions(
                        context = requireContext(),
                        options = options,
                        departmentType = CompanyListParser.departmentType,
                        onSuccess = model.onCompanyGetSuccess,
                        onError = {}
                    )
                )
            }
        }
        model.requestDatabaseInsertMethod.observe(viewLifecycleOwner) { company ->
            if (context != null) {
                beDisposed.add(
                    CompanyDatabaseMethods.insert(
                        context = requireContext(),
                        company = company,
                        onComplete = model.onCompanyInsertComplete,
                        onError = {}
                    )
                )
            }
        }
        model.requestDatabaseUpdateMethod.observe(viewLifecycleOwner) { companies ->
            if (context != null) {
                beDisposed.add(
                    CompanyDatabaseMethods.update(
                        context = requireContext(),
                        companies = companies,
                        onComplete = model.onCompanyUpdateComplete,
                        onError = {}
                    )
                )
            }
        }
    }
    //endregion initialize

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
        private const val TAG = "CompanyFilterFragment"

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

