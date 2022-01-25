package com.example.skainet_android.user.userEdit.trip

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
import com.example.skainet_android.databinding.FragmentTripEditBinding
import com.example.skainet_android.user.data.Trip

class TripEditFragment : Fragment() {
    companion object {
        const val TRIP_ID = "TRIP_ID"
    }

    private lateinit var viewModel: TripEditViewModel
    private var tripId: String? = null
    private var trip: Trip? = null

    private var _binding: FragmentTripEditBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView")
        arguments?.let {
            if (it.containsKey(TRIP_ID)) {
                tripId = it.getString(TRIP_ID).toString()
            }
        }
        _binding = FragmentTripEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setupViewModel()
        binding.fab.setOnClickListener {
            Log.v(TAG, "save user")
            val tr = trip
            if (tr != null) {
                tr.name = binding.name.text.toString()
                tr.userId = binding.userId.text.toString()
                tr.datetime = binding.datetime.text.toString()
                viewModel.saveOrUpdateTrip(tr)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i(TAG, "onDestroyView")
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(TripEditViewModel::class.java)
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
        val id = tripId
        if (id == null) {
            trip = Trip()
        } else {
            viewModel.getItemById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update users")
                if (it != null) {
                    trip = it
                    binding.name.setText(it.name)
                    binding.userId.setText(it.userId)
                    binding.datetime.setText(it.datetime)
                }
            })
        }
    }
}