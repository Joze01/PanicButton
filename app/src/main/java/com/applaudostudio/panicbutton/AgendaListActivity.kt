package com.applaudostudio.panicbutton

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.applaudostudio.panicbutton.extras.replaceFragment
import com.applaudostudio.panicbutton.fragment.AgendaListFragment
import com.applaudostudio.panicbutton.fragment.ContactFragment
import kotlinx.android.synthetic.main.activity_main.*

class AgendaListActivity : AppCompatActivity(), AgendaListFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda_list)
        val fragmentList: AgendaListFragment = AgendaListFragment.newInstance("Abstract Adapter","Hola");
        this.replaceFragment(fragmentList,R.id.contactsContainer)
        setSupportActionBar(mainToolbar)

    }
}
