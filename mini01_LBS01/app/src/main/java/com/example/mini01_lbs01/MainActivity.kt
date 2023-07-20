package com.example.mini01_lbs01

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var googleMap: GoogleMap
    lateinit var userLocation: Location
    lateinit var dialogIdx: String

    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var locationListener: LocationListener? = null

    var myMarker: Marker? = null

    var latitudeList = mutableListOf<Double>()
    var longitudeList = mutableListOf<Double>()
    var nameList = mutableListOf<String>()
    var vicinityList = mutableListOf<String>()
    var placeMarkerList = mutableListOf<Marker>()

    val dialogData = arrayOf(
        "accounting", "airport", "amusement_park",
        "aquarium", "art_gallery", "atm", "bakery",
        "bank", "bar", "beauty_salon", "bicycle_store",
        "book_store", "bowling_alley", "bus_station",
        "cafe", "campground", "car_dealer", "car_rental",
        "car_repair", "car_wash", "casino", "cemetery",
        "church", "city_hall", "clothing_store", "convenience_store",
        "courthouse", "dentist", "department_store", "doctor",
        "drugstore", "electrician", "electronics_store", "embassy",
        "fire_station", "florist", "funeral_home", "furniture_store",
        "gas_station", "gym", "hair_care", "hardware_store", "hindu_temple",
        "home_goods_store", "hospital", "insurance_agency",
        "jewelry_store", "laundry", "lawyer", "library", "light_rail_station",
        "liquor_store", "local_government_office", "locksmith", "lodging",
        "meal_delivery", "meal_takeaway", "mosque", "movie_rental", "movie_theater",
        "moving_company", "museum", "night_club", "painter", "park", "parking",
        "pet_store", "pharmacy", "physiotherapist", "plumber", "police", "post_office",
        "primary_school", "real_estate_agency", "restaurant", "roofing_contractor",
        "rv_park", "school", "secondary_school", "shoe_store", "shopping_mall",
        "spa", "stadium", "storage", "store", "subway_station", "supermarket",
        "synagogue", "taxi_stand", "tourist_attraction", "train_station",
        "transit_station", "travel_agency", "university", "eterinary_care", "zoo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        // MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this@MainActivity)

        activityMainBinding.run {
            toolbarMain.run {
                title = "현재위치"
                inflateMenu(R.menu.main_menu)

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.mainMenuItem1 -> getCurrentLocation()
                        R.id.mainMenuItem2 -> {

                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle("장소 종류 선택")

                            builder.setItems(dialogData) { dialogInterface: DialogInterface, i: Int ->
                                dialogIdx = dialogData[i]
                                markChoicePlace()
                            }
                            builder.setNeutralButton("초기화") { dialogInterface: DialogInterface, i: Int ->
                                initMarkChoicePlace()
                            }
                            builder.setNegativeButton("취소", null)

                            builder.show()
                        }
                    }

                    false
                }
            }
        }

    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        map.uiSettings.isZoomControlsEnabled = true

        val a1 = ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val a2 = ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED) {

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location1 != null) {
                showCurrentLocation(location1)
            }

            getCurrentLocation()

        } else {
            requestPermissions(permissionList, 0)
        }

    }

    fun showCurrentLocation(location: Location) {

        userLocation = location

        googleMap.apply {
            val currentLatLng = LatLng(location.latitude, location.longitude)
            val bitmap = getBitmapFromVectorDrawable(this@MainActivity, R.drawable.person_pin_circle)
            val markerBitmap = BitmapDescriptorFactory.fromBitmap(bitmap)

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

    fun getCurrentLocation() {

        val a1 = ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val a2 = ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED) {

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            locationListener = LocationListener {
                showCurrentLocation(it)
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener!!
                )
            }
        }

    }

    fun markChoicePlace() {

        thread {
            val location = "${userLocation.latitude},${userLocation.longitude}"
            val radius = 50000
            val type = dialogIdx
            val key = "AIzaSyDR-oYpC_QDNnHgAt3dQPTFEFNwdpGFLiw"
            val serverAddress =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$location&language=ko&radius=$radius&types=$type&key=$key"

            initMarkChoicePlace()

            // Log.d("serverAddress", serverAddress)

            val url = URL(serverAddress)
            val httpURLConnection = url.openConnection() as HttpURLConnection

            val inputStreamReader = InputStreamReader(httpURLConnection.inputStream, "UTF-8")
            val bufferedReader = BufferedReader(inputStreamReader)

            var str: String? = null
            val stringBuffer = StringBuffer()

            do {
                str = bufferedReader.readLine()
                if (str != null) {
                    stringBuffer.append(str)
                }
            } while (str != null)

            val data = stringBuffer.toString()

            val root = JSONObject(data)
            val resultsArr = root.getJSONArray("results")

            for (idx in 0 until resultsArr.length()) {
                val resultObj = resultsArr.getJSONObject(idx)
                val geometryObj = resultObj.getJSONObject("geometry")
                val locationObj = geometryObj.getJSONObject("location")
                val lat = locationObj.getDouble("lat")
                val lng = locationObj.getDouble("lng")
                val name = resultObj.getString("name")
                val vicinity = resultObj.getString("vicinity")

                latitudeList.add(lat)
                longitudeList.add(lng)
                nameList.add(name)
                vicinityList.add(vicinity)
//                Log.d("res", lat.toString())
//                Log.d("res", lng.toString())
//                Log.d("res", name)
//                Log.d("res", vicinity)
            }

            runOnUiThread {
                for (idx in 0 until latitudeList.size) {
                    val placeLatLng = LatLng(latitudeList[idx], longitudeList[idx])
                    val bitmap = getBitmapFromVectorDrawable(this@MainActivity, R.drawable.location_on)
                    val markerBitmap = BitmapDescriptorFactory.fromBitmap(bitmap)

                    val marker = googleMap.addMarker(
                        MarkerOptions().position(placeLatLng).title(nameList[idx])
                            .snippet(vicinityList[idx]).icon(markerBitmap)
                    )
                    placeMarkerList.add(marker!!)
                }
            }
        }

    }

    fun initMarkChoicePlace() {

        runOnUiThread {
            latitudeList.clear()
            longitudeList.clear()
            nameList.clear()
            vicinityList.clear()
            for (marker in placeMarkerList) {
                marker.remove()
            }
            placeMarkerList.clear()
        }

    }

    // Function that converts the vector form to Bitmap form.
   fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}


