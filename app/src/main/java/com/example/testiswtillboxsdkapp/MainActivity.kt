package com.example.testiswtillboxsdkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isw_smart_till.enums.TillAccountTypes
import com.example.isw_smart_till.enums.TillPaymentOptions
import com.example.isw_smart_till.enums.TillReturnCodes
import com.example.isw_smart_till.interfaces.TillCommand
import com.example.isw_smart_till.models.requests.TransactionRequest
import com.example.isw_smart_till.service.TillBluetoothManager
import com.example.myapplication.DevicesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var connectedDevice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val man = TillBluetoothManager(this, listener)
        man.startService()

        findViewById<ImageButton>(R.id.devicesList).setOnClickListener {
            val dialog = BottomSheetDialog(this)

            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

            // getting the recyclerview by its id
            val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)

            // this creates a vertical layout Manager
            recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)

            val data = man.getPairedDevice()

            val adapter = DevicesAdapter(data) {
                dialog.dismiss()
                man.connect(it)
            }

            recyclerview.adapter = adapter
            dialog.setContentView(view)
            dialog.show()
        }

        findViewById<Button>(R.id.buttonPay).setOnClickListener {
            val amount =  findViewById<EditText>(R.id.amountTx).text
            val request = TransactionRequest(
                amount = (amount.toString().toDouble() * 100).toString(),
                paymentOption = TillPaymentOptions.CARD,
                accountType = TillAccountTypes.SAVINGS
            )
            val command = TillCommand.TransactionRequestCommand(request)
            man.sendCommand(command)
        }

        findViewById<ImageButton>(R.id.disconnectButton).setOnClickListener {
            man.disconnect()
        }

    }

    private fun updateUI() {

        if (connectedDevice != null) {
            findViewById<TextView>(R.id.connectedDeviceName).text = connectedDevice
            findViewById<ImageButton>(R.id.devicesList).visibility = View.GONE
            findViewById<ImageButton>(R.id.disconnectButton).visibility = View.VISIBLE
            findViewById<ImageButton>(R.id.connectionImage).setImageResource(R.drawable.ic_baseline_bluetooth_24_connected)
        }else {
            findViewById<TextView>(R.id.connectedDeviceName).text = ""
            findViewById<ImageButton>(R.id.devicesList).visibility = View.VISIBLE
            findViewById<ImageButton>(R.id.disconnectButton).visibility = View.GONE
            findViewById<ImageButton>(R.id.connectionImage).setImageResource(R.drawable.ic_baseline_bluetooth_24)
        }

    }

    override fun onResume() {
        super.onResume()
    }

    val listener = object: com.example.isw_smart_till.interfaces.TillCallBackListener {

        override fun onMessageReceived(message: String) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }

        override fun onCommandReceived(command: TillCommand) {
            println("the command has come in $command")
            when(command) {
                is TillCommand.TransactionRequestCommand -> {
//                    Toast.makeText(this@MainActivity, "The transactiion response is ${command.request}", Toast.LENGTH_SHORT).show()
                }
                is TillCommand.TransactionResponseCommand -> {
                    Toast.makeText(this@MainActivity, "The transaction response is ${command.response.RespMessage}", Toast.LENGTH_SHORT).show()
                }
                is TillCommand.RawResponseCommand -> {
                    Toast.makeText(this@MainActivity, command.response, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }

        override fun onConnected(device: String) {
            connectedDevice = device
            updateUI()
            Toast.makeText(this@MainActivity, "Connected to $device", Toast.LENGTH_SHORT).show()
        }

        override fun onStateChanged() {
            TODO("Not yet implemented")
        }

        override fun onError(error: TillReturnCodes, message: String?) {
            TODO("Not yet implemented")
        }

        override fun onDisConnected() {

            connectedDevice = null
            updateUI()
            Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        println("I got this position ${p2}")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}