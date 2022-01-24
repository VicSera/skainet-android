package com.example.skainet_android.user.userList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.skainet_android.R
import com.example.skainet_android.auth.data.AuthRepository
import com.example.skainet_android.core.TAG
import com.example.skainet_android.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var usersModel: UserListViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.FragmentLogin)
            return;
        }
        setupItemList()
        binding.fab.setOnClickListener {
            Log.v(TAG, "add new item")
            findNavController().navigate(R.id.UserEditFragment)
        }
        binding.fabAddTrip.setOnClickListener {
            Log.v(TAG, "add new trip")
            findNavController().navigate(R.id.TripEditFragment)
        }
    }

    private fun setupItemList() {
        userListAdapter = UserListAdapter(this)
        binding.userList.adapter = userListAdapter
        usersModel = ViewModelProvider(this).get(UserListViewModel::class.java)
        usersModel.users.observe(viewLifecycleOwner, { value ->
            Log.i(TAG, "update users")
            userListAdapter.users = value
        })
        usersModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        usersModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        usersModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
        _binding = null
    }
}