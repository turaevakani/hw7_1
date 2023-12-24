package com.example.hw7_1.ui.door

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.hw7_1.R
import com.example.hw7_1.databinding.FragmentDoorBinding
import com.example.hw7_1.model.DoorModel
import com.example.hw7_1.ui.door.adapter.DoorAdapter
import com.example.hw7_1.utils.SwipeItem

class DoorFragment : Fragment() {

    private var list = ArrayList<DoorModel>()
    private val doorAdapter = DoorAdapter(list)
    private lateinit var binding: FragmentDoorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoorBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipe.setOnRefreshListener {
            Handler().postDelayed({
                binding.swipe.isRefreshing = false
            }, 2000)
        }
        loadData()
        binding.rvDoor.adapter = doorAdapter
        val itemTouchHelper = ItemTouchHelper(object : SwipeItem(binding.rvDoor) {
            override fun instantiateUnderlayButton(position: Int): List<Button> {
                val favoritesButton = favoritesButton()
                return listOf(favoritesButton,EditButton())
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvDoor)
    }
    private fun favoritesButton(): SwipeItem.Button {
        return SwipeItem.Button(
            requireContext(),
            "Fav",
            20f,
            R.drawable.ic_star,
        )
    }
    private fun EditButton(): SwipeItem.Button {
        return SwipeItem.Button(
            requireContext(),
            "Edit",
            20f,
            R.drawable.ic_edit,
        )
    }

    private fun loadData() {
        list.clear()
        list.add(DoorModel("https://www.ixbt.com/img/n1/news/2023/6/1/ixbtmedia_young_caucasian_woman_looking_at_photo_on_smartphone_c9877e21-3b3a-4dcc-9bf5-52ec917f6d9a_large.png","Подъезд 1"))
        list.add(DoorModel("https://aif-s3.aif.ru/images/019/507/eeba36a2a2d37754bab8b462f4262d97.jpg","Выход на пожарную лестницу"))
        list.add(DoorModel("https://i0.wp.com/www.pressfoto.ru/blog/wp-content/uploads/2013/10/How-to-make-a-portrait-photography-1.jpg?resize=600%2C338&ssl=1","Подъезд 2"))
        list.add(DoorModel("https://content-online.ru/ckfinder/userfiles/files/Portretnoe-foto1.jpg","Домофон"))

    }

}