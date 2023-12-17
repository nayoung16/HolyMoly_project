package com.example.holymoly.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.holymoly.ui.tab.BucketDoFragment
import com.example.holymoly.ui.tab.BucketDoneFragment
@RequiresApi(Build.VERSION_CODES.O)
class Bucketadapter(manager: FragmentManager): FragmentPagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> BucketDoFragment()
            1 -> BucketDoneFragment()
            else -> BucketDoFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "버킷리스트"
            1 -> "완료"
            else -> null
        }
    }
}