package com.applaudostudio.panicbutton.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applaudostudio.panicbutton.R
import com.applaudostudio.panicbutton.model.ContactModel
import kotlinx.android.synthetic.main.contact_card.view.*
import java.text.FieldPosition


class ContactListAdapter(private var mDataSet: List<ContactModel>?, var callBack: ItemInteractions) : RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>() {
    lateinit var context: Context

    fun setData(mDataSet: List<ContactModel>) {
        this.mDataSet = mDataSet
        this.notifyDataSetChanged()
    }

    /***
     * Constructor for the view Holder of the recycler view=
     * @param parent the parent viewGroup
     * @param viewType Type of view to be render
     * @return returns a RadioViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.contact_card, parent, false)
        return ContactListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, i: Int) {
        holder.bindData(mDataSet?.get(i)!!)
    }

    override fun getItemCount(): Int {
        if (mDataSet != null) {
            return mDataSet!!.size
        }
        return 0
    }

    /***
     * Class for the news View holder
     */
    inner class ContactListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contactNameTxt = itemView.txtName
        var contactPhoneTxt = itemView.txtPhone
        var container = itemView.contactContainerCard

        fun bindData(item: ContactModel) {
            contactNameTxt.text = item.name
            contactPhoneTxt.text = item.phone
            container.setOnClickListener(View.OnClickListener {
                callBack.ContactClickListener(item)
            })
        }


    }

    interface ItemInteractions {
        fun ContactClickListener(item: ContactModel)
    }

    companion object {
        val VIEW_TYPE_FULL = 2
        val VIEW_TYPE_GRID = 1
    }
}


