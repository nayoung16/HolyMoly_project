package com.example.holymoly

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var selectedPage = 0

    // 로그아웃 구현을 위한 변수
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient


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

        // Hide TabLayout and ViewPager when navigating to certain fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in setOf(
                    R.id.nav_md_pick,
                    R.id.nav_bucket_list,
                    R.id.nav_home,
                    R.id.nav_calendar,
                    R.id.nav_flight
                )
            ) {
                binding.tabLayout.visibility = View.VISIBLE
                binding.pager.visibility = View.VISIBLE
            } else {
                binding.tabLayout.visibility = View.GONE
                binding.pager.visibility = View.GONE
            }

            when (destination.id) {
                R.id.nav_md_pick -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
                R.id.nav_bucket_list -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
                R.id.nav_home -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(2))
                R.id.nav_calendar -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(3))
                R.id.nav_flight -> binding.tabLayout.selectTab(binding.tabLayout.getTabAt(4))
            }
        }

        //로그아웃
        // 구글 로그아웃을 위해 로그인 세션 가져오기
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // firebaseauth를 사용하기 위한 인스턴스 get
        auth = FirebaseAuth.getInstance()

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Get the header view from the NavigationView
        val headerView: View = navigationView.getHeaderView(0)

        // Find the ImageView inside the header view
        val signOutImageView: ImageView = headerView.findViewById(R.id.sign_out)
        signOutImageView.setOnClickListener{
            Firebase.auth.signOut()
            googleSignInClient?.signOut()

            googleSignInClient!!.signOut().addOnCompleteListener(this) {
                startActivity(Intent(this, AuthActivity::class.java))
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