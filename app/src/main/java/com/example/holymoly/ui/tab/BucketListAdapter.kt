package com.example.holymoly.ui.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class BucketListAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // 각 탭에 해당하는 Fragment를 반환
        return when (position) {
            0 -> FirstBucketList()
            1 -> SecondBucketList()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        // 전체 탭의 개수
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // 각 탭의 타이틀
        return when (position) {
            0 -> "First Tab"
            1 -> "Second Tab"
            else -> null
        }
    }
}