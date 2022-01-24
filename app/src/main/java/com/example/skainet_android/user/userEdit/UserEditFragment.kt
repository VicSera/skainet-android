package com.example.skainet_android.user.userEdit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.skainet_android.core.TAG
import com.example.skainet_android.databinding.FragmentUserEditBinding
import com.example.skainet_android.user.data.User
import com.example.skainet_android.user.tripList.TripListAdapter

class UserEditFragment : Fragment() {
    companion object {
        const val USER_ID = "USER_ID"
    }

    private lateinit var viewModel: UserEditViewModel
    private var userId: String? = null
    private var user: User? = null

    private var _binding: FragmentUserEditBinding? = null

    private val binding get() = _binding!!
    private lateinit var tripListAdapter: TripListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        arguments?.let {
            if (it.containsKey(USER_ID)) {
                userId = it.getString(USER_ID).toString()
            }
        }
        _binding = FragmentUserEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setupViewModel()
        binding.fab.setOnClickListener {
            Log.v(TAG, "save user")
            val i = user
            if (i != null) {
                i.name = binding.userName.text.toString()
                viewModel.saveOrUpdateUser(i)
            }
        }
        binding.userName.setText(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i(TAG, "onDestroyView")
    }

    private fun setupItemList() {
        tripListAdapter = TripListAdapter(this)
        binding.tripList.adapter = tripListAdapter
    }

    private fun setupViewModel() {
        setupItemList()

        viewModel = ViewModelProvider(this).get(UserEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            binding.progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = userId
        if (id == null) {
            user = User("", "")
        } else {
            viewModel.getItemById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update users")
                if (it != null) {
                    user = it
                    binding.userName.setText(it.name)
                    tripListAdapter.trips = it.trips ?: emptyList()
                }
            })
        }
    }
}