package com.example.holymoly

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.holymoly.databinding.ActivityMainBinding
import com.example.holymoly.ui.tab.BucketListFragment
import com.example.holymoly.ui.tab.CalendarFragment
import com.example.holymoly.ui.tab.FlightFragment
import com.example.holymoly.ui.tab.HomeFragment
import com.example.holymoly.ui.tab.MDFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var selectedPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState != null) {
            selectedPage = savedInstanceState.getInt(KEY_SELECTED_PAGE, 0)
            binding.pager.currentItem = selectedPage
        }

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.toolbar.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_md_pick,
                R.id.nav_bucket_list,
                R.id.nav_home,
                R.id.nav_calendar,
                R.id.nav_flight
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //뷰페이저, 어댑터
        class MyPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
            val fragments: List<Fragment> = listOf(
                    MDFragment(), BucketListFragment(), HomeFragment(),
                    CalendarFragment(), FlightFragment()
                )
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        // 뷰페이저에 어댑터 연결
        binding.pager.adapter = MyPagerAdapter(this)

        //tab title, icon 설정
        val tabTitles = listOf("MD's Pick", "Bucket List", "Home", "Calendar", "Flight")
        val tabIconList = listOf(
            R.drawable.ic_tablayout_mdpick,
            R.drawable.ic_tablayout_bucketlist,
            R.drawable.ic_tablayout_home,
            R.drawable.ic_tablayout_calendar,
            R.drawable.ic_tablayout_flight
        )

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabTitles[position]
            tab.setIcon(tabIconList[position])
        }.attach()



        // tabLayout과 viewpager 연결
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectedPage = position

                when (position) {
                    0 -> navController.navigate(R.id.nav_md_pick)
                    1 -> navController.navigate(R.id.nav_bucket_list)
                    2 -> navController.navigate(R.id.nav_home)
                    3 -> navController.navigate(R.id.nav_calendar)
                    4 -> navController.navigate(R.id.nav_flight)
                }

                binding.tabLayout.setScrollPosition(position, 0f, true)

            }
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_md_pick -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
                R.id.nav_bucket_list -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
                R.id.nav_home -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(2))
                R.id.nav_calendar -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(3))
                R.id.nav_flight -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(4))
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_PAGE, selectedPage)
    }

    companion object {
        private const val KEY_SELECTED_PAGE = "selected_page"
    }
}