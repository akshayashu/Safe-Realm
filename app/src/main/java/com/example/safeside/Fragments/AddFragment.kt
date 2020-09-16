package com.example.safeside.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.safeside.Backend.AuthenticationInterface
import com.example.safeside.DataModel.Zones
import com.example.safeside.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var zoneList : List<Zones>

    var lat: String? = null
    var long: String? = null
    var loc : String? = null
    var lt = 28.557477
    var lg = 77.092012

    private val markers = ArrayList<LatLng>()

    var map : GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMarkers()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = map_view

        if (mapView != null){
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this)

        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)

        map = p0
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lt, lg), 10.0f))
        var curLatLng = LatLng(lt, lg)

        markZone.setOnClickListener {


            val markerOptions = MarkerOptions()

            markerOptions.position(curLatLng)

            markerOptions.icon(bitmapDescriptorFromVector(R.drawable.red_warning_icon))

            map?.clear()
            Toast.makeText(context, "Marked this Zone! ⚠", Toast.LENGTH_SHORT).show()

            map?.addMarker(markerOptions)
            markers.clear()
            addMarkers()
        }

        if(lat != "0.0" && long != "0.0"){
            Log.d("LOCATION", "$lat $long")

            val latLng = LatLng(lt, lg)
            val markerOptions = MarkerOptions()

            markerOptions.position(latLng)

            markerOptions.title(loc)
            markerOptions.icon(bitmapDescriptorFromVector(R.drawable.location_red_icon))

            map?.clear()

            map?.addMarker(markerOptions)
            markers.clear()
            addMarkers()
            currentLocation.text = loc
        } else{
            currentLocation.text = "Access Denied"
        }

        map?.setOnMapClickListener { latLng -> // Creating a marker
            val markerOptions = MarkerOptions()

            curLatLng = latLng

            markerOptions.position(latLng)
            try {
                val geoCoder = Geocoder(context, Locale.getDefault())
                val addresses: List<Address>? = geoCoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
                )
                if (addresses != null && addresses.isNotEmpty()) {
                    val zone : String = addresses[0].subLocality
                    Log.d("LOCATION", zone)
                    markerOptions.title(zone)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            markerOptions.icon(bitmapDescriptorFromVector(R.drawable.location_red_icon))


            map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            map?.addMarker(markerOptions)
        }

        addMarkers()
    }

    private fun addMarkers(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sheltered-beyond-13726.herokuapp.com/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()))
            .build()

        val interfaces = retrofit.create(AuthenticationInterface::class.java)

        interfaces.zones().enqueue(object : Callback<List<Zones>> {
            override fun onResponse(call: Call<List<Zones>>, response: Response<List<Zones>>) {
                Log.d("ZONELIST", response.body()?.get(0)?.lat.toString())

                for (i in response.body()!!){
                    markers.add(LatLng(i.lat.toDouble(),i.lon.toDouble()))
                }
                for( m in markers){
                    map?.addMarker(
                        MarkerOptions().position(m)
                            .icon(bitmapDescriptorFromVector(R.drawable.red_warning_icon))
                    )
                }
            }

            override fun onFailure(call: Call<List<Zones>>, t: Throwable) {
                Log.d("ZONELIST", t.localizedMessage)
            }

        })
        if(markers.size == 0){
            Log.d("MarkerSize", "00000000000")
        }
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int) : BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context!!, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicHeight,
            vectorDrawable.intrinsicWidth
        )

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.fragment_add, container, false)


        val editor = activity?.getSharedPreferences(
            "SharedPrefFile",
            AppCompatActivity.MODE_PRIVATE
        )

        lat= editor!!.getString("lat", "0.0")
        long = editor.getString("long", "0.0")
        loc = editor.getString("location", "Access Denied")
        lt = lat!!.toDouble()
        lg = long!!.toDouble()

        val callCops = views.findViewById(R.id.alertCops) as TextView

        callCops.setOnClickListener {
            val postedBy = "911"

            val uri = "tel:" + postedBy.trim()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }

        return views

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}