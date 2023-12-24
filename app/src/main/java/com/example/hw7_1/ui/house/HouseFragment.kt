package com.example.hw7_1.ui.house

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hw7_1.databinding.FragmentHouseBinding
import com.example.hw7_1.ui.house.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HouseFragment : Fragment() {
    private lateinit var binding: FragmentHouseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHouseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabLayout
        val viewPager = binding.pager
        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Камеры"
                1 -> tab.text = "Двери"
            }
        }.attach()
    }
}