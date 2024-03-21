import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.primary_detail_flow_restaurant.R
import com.example.primary_detail_flow_restaurant.databinding.FragmentItemDetailBinding
import com.google.android.gms.location.LocationServices

object PlaceholderContent {

    val ITEMS: MutableList<PlaceholderItem> = ArrayList()

    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()
    lateinit var context: Context
    init {
        addItem(
            PlaceholderItem(
                "1",
                "Rasushi's Menu",
                "https://rasushi.com/main-menu/"
            )
        )
        addItem(
            PlaceholderItem(
                "2",
                "Make a Reservation",
                "https://rasushi.com/reservations/"
            )
        )
        addItem(
            PlaceholderItem(
                "3",
                "My Location",
                "https://www.google.com/maps/place",


            )
        )


    }

    private fun addItem(item: PlaceholderItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    data class PlaceholderItem(val id: String, val website_name: String, val website_url: String) {
        override fun toString(): String = website_name
    }


}
