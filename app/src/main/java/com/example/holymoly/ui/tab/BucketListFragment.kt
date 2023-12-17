package com.example.holymoly.ui.tab

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.holymoly.R
import com.example.holymoly.ui.Bucketadapter
import com.google.android.material.tabs.TabLayout

@RequiresApi(Build.VERSION_CODES.O)
class BucketListFragment : Fragment() {
    lateinit var myFragment: View
    lateinit var viewPagers: ViewPager
    lateinit var tabLayouts: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myFragment = inflater.inflate(R.layout.fragment_bucket_list, container, false)
        return myFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager()
    }


    private fun setUpViewPager() {
        viewPagers = myFragment.findViewById(R.id.viewPager)
        tabLayouts = myFragment.findViewById(R.id.tabLayout)

        var adapter = Bucketadapter(childFragmentManager)
        viewPagers.adapter = adapter
        tabLayouts.setupWithViewPager(viewPagers)
        tabLayouts.tabMode = TabLayout.MODE_FIXED
    }
}