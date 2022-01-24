package com.example.skainet_android.user.tripList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.skainet_android.R
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.Trip
import com.example.skainet_android.user.userEdit.trip.TripEditFragment

class TripListAdapter(
    private val fragment: Fragment,
) : RecyclerView.Adapter<TripListAdapter.ViewHolder>() {

    var trips = emptyList<Trip>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onTripClick: View.OnClickListener = View.OnClickListener { view ->
        val trip = view.tag as Trip
        fragment.findNavController().navigate(R.id.TripEditFragment, Bundle().apply {
            putString(TripEditFragment.TRIP_ID, trip.id)
        })
    };

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val trip = trips[position]
        holder.textView.text = trip.toString()
        holder.itemView.tag = trip
        holder.itemView.setOnClickListener(onTripClick)
    }

    override fun getItemCount() = trips.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text)
    }
}
