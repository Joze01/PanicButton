package com.applaudostudio.panicbutton

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.applaudostudio.panicbutton.extras.replaceFragment
import com.applaudostudio.panicbutton.fragment.ContactFragment
import com.applaudostudio.panicbutton.model.ContactModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), ContactFragment.OnFragmentInteractionListener, View.OnClickListener {

    var securityContacts: MutableList<ContactModel> = mutableListOf()
    var latitude:String=""
    var longitude:String=""
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        startActivity<AgendaListActivity>()
        return super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getLocation()

        setSupportActionBar(mainToolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val fragmentList: ContactFragment = ContactFragment.newInstance("");
                            replaceFragment(fragmentList, R.id.fragmentContainer)

                        } else {
                            toast(getString(R.string.permissionRequest))
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                        token.continuePermissionRequest();
                    }
                }).check()
    }


    override fun onClick(p0: View?) {
        when (p0) {

        }
    }


/*
     @SuppressLint("MissingPermission")
    fun getLocation():String{

        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    Log.e("LAT,LONG ======>",location?.latitude.toString()+","+location?.longitude.toString())
                    latitude=location?.latitude.toString()
                    longitude=location?.longitude.toString()
                }
         return "$latitude,$longitude"
    }
    */

}
