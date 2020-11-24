package com.example.route_final

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import org.json.JSONArray
import kotlin.random.Random

class MainActivity : FragmentActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        val data = generateHeatMapData()
        val heatMapProvider = HeatmapTileProvider.Builder()
                .weightedData(data) // load our weighted data
                .radius(50) // optional, in pixels, can be anything between 20 and 50
                .maxIntensity(1000.0) // set the maximum intensity
                .build()

        googleMap?.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))

        val LALatLng = LatLng(34.0522, -118.2437)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LALatLng, 5f))
    }

    private fun generateHeatMapData(): ArrayList<WeightedLatLng> {
        val data = ArrayList<WeightedLatLng>()

        val jsonData = getJsonDataFromAsset("final_dataset.json")
        jsonData?.let {
            for (i in 0 until it.length()) {
                val entry = it.getJSONObject(i)
                val lat = entry.getDouble("Lat")
                val lon = entry.getDouble("Long")
                val density= 1.0
                    val weightedLatLng = WeightedLatLng(LatLng(lat, lon), density)
                    data.add(weightedLatLng)
            }
        }

        return data
    }

    private fun getJsonDataFromAsset(fileName: String): JSONArray? {
        try {
            val jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
            return JSONArray(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
