package com.example.hw7_1.ui.camera

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.hw7_1.R
import com.example.hw7_1.databinding.FragmentCameraBinding
import com.example.hw7_1.model.CameraModel
import com.example.hw7_1.ui.camera.adapter.CameraAdapter
import com.example.hw7_1.utils.SwipeItem

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private var list = ArrayList<CameraModel>()
    private var cameraAdapter = CameraAdapter(list)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.rvCamera.adapter = cameraAdapter
        binding.swipe.setOnRefreshListener {
            Handler().postDelayed({
                binding.swipe.isRefreshing = false
            }, 2000)
        }
        val itemTouchHelper = ItemTouchHelper(object : SwipeItem(binding.rvCamera) {
            override fun instantiateUnderlayButton(position: Int): List<Button> {
                val favoritesButton = favoritesButton()
                return listOf(favoritesButton)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvCamera)
    }
    private fun favoritesButton(): SwipeItem.Button {
        return SwipeItem.Button(
            requireContext(),
            "Fav",
            20f,
            R.drawable.ic_star,
        )
    }
    private fun loadData() {
        list.add(
            CameraModel(
                "Камера 1",
                "https://vproekte.com/wp-content/uploads/2020/12/LR_8-Copy-1024x768.jpg"
            )
        )
        list.add(
            CameraModel(
                "Камера 2",
                "https://pechenyi.com/info/gostinaya-49-1.jpg"))
        list.add(
            CameraModel(
                "Камера 3",
                "https://remont-f.ru/upload/iblock/f37/interer-gostinoy-v-sovremennom-stile-ar-deko-foto-00.jpg"
            )
        )
        list.add(
            CameraModel(
                "Камера 4",
                "https://n1s1.hsmedia.ru/1a/d2/86/1ad2865de680543dd973fcb7bface602/2560x1706_0xac120003_7874170071658520764.jpeg"
            )
        )
    }
}