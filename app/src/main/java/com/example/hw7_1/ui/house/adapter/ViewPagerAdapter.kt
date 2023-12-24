package com.example.hw7_1.ui.house.adapter

import com.example.hw7_1.ui.camera.CameraFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hw7_1.ui.door.DoorFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int=2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> CameraFragment()
            1->DoorFragment()
            else-> CameraFragment()

        }
    }
}