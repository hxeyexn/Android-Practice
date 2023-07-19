package com.example.mini01_lbs01

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mini01_lbs01.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var googleMap: GoogleMap

    val permissionList = arrayOf (
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // 위치 측정 리스너
    var locationListener: LocationListener? = null

    // 현재 사용자 위치에 표시되는 마커
    var myMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        // MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this@MainActivity)

        activityMainBinding.run {
            toolbarMain.run {
                title = "현재위치"
                inflateMenu(R.menu.main_menu)

                setOnMenuItemClickListener {
                    getLocation()
                    false
                }
            }
        }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        map.uiSettings.isZoomControlsEnabled = true

        val a1 = ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        val a2 = ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED) {

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            val location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location1 != null) {
                showCurrentLocation(location1)
            }

            getLocation()

        } else {
            requestPermissions(permissionList, 0)
        }
    }

    fun showCurrentLocation(location: Location) {

        googleMap.apply {
            val currentLatLng = LatLng(location.latitude, location.longitude)
            val markerBitmap = BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)

            // 기존에 표시한 마커를 제거한다.
            if (myMarker != null) {
                myMarker?.remove()
                myMarker = null
            }

            myMarker = googleMap.addMarker(
                MarkerOptions().position(currentLatLng).title("현재 위치").icon(markerBitmap)
            )

            moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
        }

        if (locationListener != null) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(locationListener!!)
            locationListener = null
        }

    }

    fun getLocation() {

        val a1 = ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        val a2 = ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED) {

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            locationListener = LocationListener {
                showCurrentLocation(it)
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener!!)
            }
        }

    }

}

