package com.applaudostudio.panicbutton.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applaudostudio.panicbutton.R
import com.applaudostudio.panicbutton.adapter.ContactListAdapter
import com.applaudostudio.panicbutton.extras.database
import com.applaudostudio.panicbutton.model.ContactDBModel
import com.applaudostudio.panicbutton.model.ContactModel
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.db.*
import android.telephony.SmsManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_contacts_list.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_LAT = "param1"
private const val ARG_LON = "param2"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ContactFragment : Fragment(), ContactListAdapter.ItemInteractions, View.OnClickListener {


    private var contactList: MutableList<ContactModel> = mutableListOf()
    var contactAdapter: ContactListAdapter = ContactListAdapter(mutableListOf(), this)
    private var coordenates: String? = null
    private var lon: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            coordenates = it.getString(ARG_LAT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        floatingActionButtonSend.setOnClickListener(this)
        contactAdapter.setData(mutableListOf())
        securityListRecycler.layoutManager = LinearLayoutManager(getActivity())
        securityListRecycler.adapter = contactAdapter
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        loadData()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): String? {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    Log.e("LAT,LONG ======>", location?.latitude.toString() + "," + location?.longitude.toString())
                    coordenates = location?.latitude.toString()
                    coordenates += ",${location?.longitude.toString()}"
                    var notify:Boolean=false
                    for (item in contactList) {
                        if(!notify){
                            context?.alert("Se ha notificado a sus contactos de seguridad ") {
                            }?.show()
                            notify=true
                        }
                        val sms = SmsManager.getDefault()
                        sms.sendTextMessage(item.phone, null, "Estoy en problemas necesito ayuda: http://maps.google.com/maps?q=$coordenates&z=17", null, null);
                    }
                }

        return coordenates
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                ContactFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_LAT, param1)
                    }
                }
    }


    fun loadData() {
        context?.database?.use {
            select(ContactDBModel.TABLENAME, ContactDBModel.COLUMN_NAME, ContactDBModel.COLUMN_PHONE).exec {
                contactList.addAll(parseList<ContactModel>(classParser()))
                contactAdapter.setData(contactList)
                securityListRecycler.layoutManager = LinearLayoutManager(getActivity())
                securityListRecycler.adapter = contactAdapter
            }
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onClick(p0: View?) {
        when (p0) {
            floatingActionButtonSend -> {
               getLocation()

            }
        }

    }


    override fun ContactClickListener(item: ContactModel) {
        context?.alert("Esta Seguro que desea quitar este contacto de seguridad?") {
            yesButton {
                context?.database?.use{
                    transaction {
                        delete(ContactDBModel.TABLENAME,ContactDBModel.COLUMN_NAME+"=?", arrayOf(item.name))
                    }
                    contactList.remove(item)
                    contactAdapter.setData(contactList)
                    contactAdapter.notifyDataSetChanged()
                    view?.snackbar("Borrado Exitosamente")
                }
            }
            noButton {}
        }?.show()

    }
}
