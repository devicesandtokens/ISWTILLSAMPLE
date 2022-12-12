package com.example.myapplication
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.testiswtillboxsdkapp.R

class DevicesAdapter(private val mList: List<BluetoothDevice>, val onSelected: (BluetoothDevice) -> Unit) : RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val device = mList[position]

        holder.item.setOnClickListener {
            onSelected(device)
        }

        // sets the image to the imageview from our itemHolder class
        holder.bleName.text = device.name

        // sets the text to the textview from our itemHolder class
        holder.textView.text = device.address

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val item: LinearLayout = itemView.findViewById(R.id.hold)
        val bleName: TextView = itemView.findViewById(R.id.bleNameView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}
