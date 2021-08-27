package com.ray.personnel.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.R
import com.ray.personnel.databinding.FragmentLoginBinding
import com.ray.personnel.domain.PreferenceManager
import com.ray.personnel.ui.filter.CompanyFilterFragment

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val model: LoginViewModel by viewModels()

    //region Lifecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = model
        binding.lifecycleOwner = this
        model.loginToken.observe(viewLifecycleOwner) { token ->
            if (context != null) PreferenceManager.setString(requireContext(), KEY_TOKEN, token)
            moveToNextFragment(token)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion Lifecycle

    private fun moveToNextFragment(token: String) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.activity_slide_in,
            R.anim.activity_slide_out,
            R.anim.activity_slide_enter,
            R.anim.activity_slide_exit
        )
        transaction.replace(R.id.container, CompanyFilterFragment.newInstance(token))
            .addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }
}
