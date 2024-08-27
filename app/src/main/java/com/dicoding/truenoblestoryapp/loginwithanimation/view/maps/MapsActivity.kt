package com.dicoding.truenoblestoryapp.loginwithanimation.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dicoding.truenoblestoryapp.loginwithanimation.R
import com.dicoding.truenoblestoryapp.loginwithanimation.data.response.ListStoryItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.truenoblestoryapp.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.truenoblestoryapp.loginwithanimation.view.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        val jkt = LatLng(-6.200000, 106.816666)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jkt))

        viewModel.getAllLocStories()

        viewModel.storyResponses.observe(this) {
            addManyMarker(it.listStory)
        }
    }

    private fun addManyMarker(listStory: List<ListStoryItem>) {
        listStory.forEach {
            val latLng = LatLng(it.lat!!, it.lon!!)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(it.name)
                    .snippet(it.description)
            )
        }
    }
}