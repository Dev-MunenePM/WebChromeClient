package com.example.primary_detail_flow_restaurant

import android.app.AlertDialog
import android.content.ClipData
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.example.primary_detail_flow_restaurant.databinding.FragmentItemDetailBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


class ItemDetailFragment : Fragment() {

    private var item: PlaceholderContent.PlaceholderItem? = null
    private var _binding: FragmentItemDetailBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            item = PlaceholderContent.ITEM_MAP[dragData as String]
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = PlaceholderContent.ITEM_MAP[it.getString(ARG_ITEM_ID).toString()]
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        val rootView = binding.root

        rootView.setOnDragListener(dragListener)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateContent()
    }

    private fun updateContent() {
        if (item != null) {
            binding.restaurantDetails.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }
            }

            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    // Permission is granted
                }
            }

            binding.restaurantDetails.webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String?,
                    callback: GeolocationPermissions.Callback?
                ) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.apply {
                            setTitle("Permission")
                            setMessage("This site wants to know your location")
                            setPositiveButton("Allow") { _, _ ->
                                callback?.invoke(origin, true, false)
                            }
                            setNegativeButton("Deny") { _, _ ->
                                callback?.invoke(origin, false, false)
                            }
                        }
                        builder.show()
                    } else {
                        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }

                    super.onGeolocationPermissionsShowPrompt(origin, callback)
                }
            }
        }

        item?.let {
            val webSettings = binding.restaurantDetails.settings
            webSettings.javaScriptEnabled = true
            webSettings.setGeolocationEnabled(true)
            binding.restaurantDetails.loadUrl(it.website_url)
        }
    }

    companion object {

        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
