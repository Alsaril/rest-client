package com.natcom.fragment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.natcom.R
import com.natcom.activity.LeadController
import com.natcom.network.DenyResult
import com.natcom.network.NetworkController
import kotterknife.bindView

class LeadFragment : BoundFragment(), DenyResult {
    val company by bindView<TextView>(R.id.company)
    val address by bindView<TextView>(R.id.address)
    val date by bindView<TextView>(R.id.date)
    val mount_date by bindView<TextView>(R.id.mount_date)
    val close by bindView<Button>(R.id.close)
    val shift by bindView<Button>(R.id.shift)
    val deny by bindView<Button>(R.id.deny)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater.inflate(R.layout.lead_fragment, container, false))

        val lead = (activity as LeadController).lead()

        lead?.let {
            company.text = it.company
            address.text = it.address
            date.text = it.date
            mount_date.text = it.mountDate
        }

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.lead)

        NetworkController.denyCallback = this

        close.setOnClickListener { close() }
        shift.setOnClickListener { shift() }
        deny.setOnClickListener { deny() }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        NetworkController.denyCallback = null
    }

    fun close() {
        (activity as LeadController).closeLead()
    }

    fun shift() {
        TODO("Not implemented")
    }

    fun deny() {
        val editText = EditText(activity)
        AlertDialog.Builder(activity)
                .setMessage(R.string.comment)
                .setTitle(R.string.deny_lead)
                .setView(editText)
                .setPositiveButton(R.string.ok) { _, _ ->
                    NetworkController.deny((activity as LeadController).lead()!!.id, editText.text.toString())
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
    }

    override fun onDenyResult(success: Boolean) {
        if (!success) {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, R.string.deny_success, Toast.LENGTH_SHORT).show()
        }
        (activity as LeadController).back()
    }
}