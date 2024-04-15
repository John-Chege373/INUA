package com.example.inua.fragments

import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.inua.R
import com.example.inua.data.MapLocation
import com.example.inua.data.location1
import com.example.inua.data.location2
import com.example.inua.data.location3
import com.example.inua.data.location4
import com.example.inua.data.location5
import com.example.inua.data.location6
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


@Suppress("DEPRECATION")
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermission()
    }

    private fun openGoogleMaps(latitude: Double, longitude: Double) {
        val uri = "geo:$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Google Maps app not installed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        Log.d("MapsFragment", "onMapReady: Map is initialized")
        getCurrentLocation { location ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                openGoogleMaps(latitude, longitude)
            }
        }
    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            getCurrentLocation { location ->
                location?.let {
                    // Handle the obtained location if needed
                }
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Explain why you need the permission
            // You can show a dialog or a snackbar here
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation { location ->
                    location?.let {
                        // Handle the obtained location if needed
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun getCurrentLocation(callback: (Location?) -> Unit) {
        // Check if the map is initialized, as map manipulation requires it.
        if (!::map.isInitialized) {
            Log.d("MapsFragment", "getCurrentLocation: Map is not initialized yet")
            return // Exit the function if the map is not ready
        }

        // Check if location permissions have been granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                map.isMyLocationEnabled = true // Enable the blue dot for user's location

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        callback(location) // Pass the location to the callback function
                    }
                    .addOnFailureListener { exception ->
                        Log.e("MapsFragment", "getCurrentLocation: Failed ", exception)
                        Toast.makeText(
                            requireContext(),
                            "Error getting location: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        callback(null) // Pass null to the callback in case of failure
                    }
            } catch (e: SecurityException) {
                Log.e("MapsFragment", "getCurrentLocation: SecurityException: ${e.message}")
                // Handle the potential lack of permission more gracefully
                // e.g., by showing a message explaining why permission is needed
            }
        } else {
            // Location permission is not granted, handle this case
            // (e.g., request permission)
        }
    }



}
