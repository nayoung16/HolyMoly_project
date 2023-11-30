package com.example.holymoly.ui.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.holymoly.R
import com.google.android.material.tabs.TabLayout


class BucketListFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bucket_list, container, false)
        // Inflate the layout for this fragment

        val viewPager: ViewPager = view.findViewById(R.id.bucket_viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.bucket_tabLayout)

        val innerPagerAdapter = BucketListAdapter(childFragmentManager)

        tabLayout.setupWithViewPager(viewPager)

        return view

    }

}