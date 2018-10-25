package com.applaudostudio.panicbutton.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applaudostudio.panicbutton.R
import com.applaudostudio.panicbutton.adapter.ContactListAdapter
import com.applaudostudio.panicbutton.extras.database
import com.applaudostudio.panicbutton.model.ContactDBModel
import com.applaudostudio.panicbutton.model.ContactModel
import kotlinx.android.synthetic.main.fragment_agenda_list.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AgendaListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AgendaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AgendaListFragment : Fragment(), ContactListAdapter.ItemInteractions {


    var contactAdapter: ContactListAdapter = ContactListAdapter(mutableListOf(), this)
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var contactList: MutableList<ContactModel> = mutableListOf()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contactAdapter.setData(mutableListOf())
        recyclerContacts.layoutManager = LinearLayoutManager(getActivity())
        recyclerContacts.adapter = contactAdapter
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onResume() {
        super.onResume()
        getContacts()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            Log.e("DATA=====>", "HOLA")
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
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
         * @return A new instance of fragment AgendaListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AgendaListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun getContacts() {
        if (activity != null) {
            val phones = activity!!.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf("display_name", "data1"), null, null, "display_name")
            while (phones.moveToNext()) {
                val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                var contactModel = ContactModel(name,phoneNumber)
                contactList.add(contactModel)
                contactAdapter.setData(contactList)
                contactAdapter.notifyDataSetChanged()
            }
            phones.close()
        }
    }

    override fun ContactClickListener(item: ContactModel) {
        val db = context?.database?.writableDatabase//writeableDataBase
        db?.insert(ContactDBModel.TABLENAME,
                ContactDBModel.COLUMN_NAME to item.name,
                ContactDBModel.COLUMN_PHONE to item.phone
        )
        view?.snackbar("Agregado a sus contactos de seguridad")

    }


}






