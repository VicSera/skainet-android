package com.example.skainet_android.user.userList

import android.animation.Animator
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
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
            it.apply {
                rotation = 0f
                animate()
                    .rotation(360f)
                    .scaleXBy(1.2f)
                    .scaleYBy(1.2f)
                    .setDuration(1000)
                    .setListener(NavigationAnimationListener(findNavController(), R.id.UserEditFragment))
            }
            Log.v(TAG, "add new item")
        }
        binding.fabAddTrip.setOnClickListener {
            val keyframeTimepoints = arrayListOf(0f, 0.2f, 0.3f, 0.5f, 0.8f, 1f)

            val xScale0 = Keyframe.ofFloat(keyframeTimepoints[0], 1f)
            val xScale1 = Keyframe.ofFloat(keyframeTimepoints[1], 1.3f)
            val xScale2 = Keyframe.ofFloat(keyframeTimepoints[2], 0.9f)
            val xScale3 = Keyframe.ofFloat(keyframeTimepoints[3], 0.9f)
            val xScale4 = Keyframe.ofFloat(keyframeTimepoints[4], 1f)
            val xScale5 = Keyframe.ofFloat(keyframeTimepoints[5], 1f)

            val yScale0 = Keyframe.ofFloat(keyframeTimepoints[0], 1f)
            val yScale1 = Keyframe.ofFloat(keyframeTimepoints[1], 0.3f)
            val yScale2 = Keyframe.ofFloat(keyframeTimepoints[2], 1.4f)
            val yScale3 = Keyframe.ofFloat(keyframeTimepoints[3], 1.4f)
            val yScale4 = Keyframe.ofFloat(keyframeTimepoints[4], 1f)
            val yScale5 = Keyframe.ofFloat(keyframeTimepoints[5], 1f)

            val height0 = Keyframe.ofFloat(keyframeTimepoints[0], 0f)
            val height1 = Keyframe.ofFloat(keyframeTimepoints[1], 5f)
            val height2 = Keyframe.ofFloat(keyframeTimepoints[2], -20f)
            val height3 = Keyframe.ofFloat(keyframeTimepoints[3], -200f)
            val height4 = Keyframe.ofFloat(keyframeTimepoints[4], -100f)
            val height5 = Keyframe.ofFloat(keyframeTimepoints[5], 0f)

            val xScales = PropertyValuesHolder.ofKeyframe("scaleX", xScale0, xScale1, xScale2, xScale3, xScale4, xScale5)
            val yScales = PropertyValuesHolder.ofKeyframe("scaleY", yScale0, yScale1, yScale2, yScale3, yScale4, yScale5)
            val heights = PropertyValuesHolder.ofKeyframe("translationY", height0, height1, height2, height3, height4, height5)
            ObjectAnimator.ofPropertyValuesHolder(it, xScales, yScales, heights).apply {
                duration = 3000
                addListener(NavigationAnimationListener(findNavController(), R.id.TripEditFragment))
                start()
            }
            Log.v(TAG, "add new trip")
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

class NavigationAnimationListener(
    private val navController: NavController,
    private val targetPath: Int
): Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        navController.navigate(targetPath)
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

}