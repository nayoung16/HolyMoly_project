package com.example.holymoly

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.holymoly.databinding.ActivityMainBinding
import com.example.holymoly.ui.FlightIconUpdateListener
import com.example.holymoly.ui.tab.BucketListFragment
import com.example.holymoly.ui.tab.CalendarFragment
import com.example.holymoly.ui.tab.FlightFragment
import com.example.holymoly.ui.tab.HomeFragment
import com.example.holymoly.ui.tab.MDFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


var tabIconList = mutableListOf<Int>()
var tabTitles = listOf<String>()

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity(), FlightIconUpdateListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var selectedPage = 0

    // 로그아웃 구현을 위한 변수
    private lateinit var googleSignInClient : GoogleSignInClient


    //firestore
    private val firestoreHelper = FirestoreHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val addActivity = AddActivity()
        addActivity.setFlightIconUpdateListener(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firestore initialize
        FirebaseApp.initializeApp(this)

        // FirebaseAuth 인스턴스 가져오기
        val auth = Firebase.auth

        // 현재 로그인된 사용자 가져오기
        val currentUser = auth.currentUser

        // 현재 로그인된 사용자의 이메일 가져오기
        val userEmail = currentUser?.email

        if (userEmail != null) {
            firestoreHelper.addUserToFirestore(userEmail)
        }

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
        tabTitles = listOf("MD's Pick", "Bucket List", "Home", "Calendar", "Flight")
        tabIconList = mutableListOf(
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

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Get the header view from the NavigationView
        val headerView: View = navigationView.getHeaderView(0)

        //이메일과 사용자 정보 가져오기
        val user: FirebaseUser? = auth.currentUser
        val headerNameView: TextView = headerView.findViewById(R.id.nav_header_title)
        val headerEmailView: TextView = headerView.findViewById(R.id.nav_header_subtitle)
        val headerImageView: ImageView = headerView.findViewById(R.id.nav_imageView)
        if(user!=null) {
            headerNameView.text = user.displayName
            headerEmailView.text = user.email
            //profile image
            val photoUrl: Uri? = user.photoUrl
            if (photoUrl != null) {
                Picasso.get().load(photoUrl).into(headerImageView)
            }
        }

        //오늘 날짜 표시하기
        val localDate = LocalDate.now()
        val dayOfWeekDisplayName = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
        val today_text = "${localDate.year}년 ${localDate.monthValue}월 ${localDate.dayOfMonth}일 ($dayOfWeekDisplayName)"

        val menu: Menu = navigationView.menu

        // Menu에서 원하는 MenuItem 가져오기 (예: nav_signout)
        val todayMenuItem: MenuItem = menu.findItem(R.id.nav_today)

        // 동적으로 제목 설정
        todayMenuItem.title = today_text
        //로그아웃
        // 구글 로그아웃을 위해 로그인 세션 가져오기
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_signout -> {
                    Firebase.auth.signOut()
                    googleSignInClient?.signOut()

                    googleSignInClient!!.signOut().addOnCompleteListener(this) {
                        startActivity(Intent(this, AuthActivity::class.java))
                    }
                }

                R.id.nav_my_holidays -> {
                    navController.navigate(R.id.nav_my_holidays)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.nav_my_flight -> {
                    navController.navigate(R.id.nav_my_flight)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.nav_today -> {
                    Toast.makeText(this, "행복한 하루 되세요 :)", Toast.LENGTH_SHORT).show()
                }
            }
            // 메뉴 아이템을 클릭한 후에 Navigation Drawer를 닫고 싶다면 true를 반환합니다.
            // 그렇지 않으면 false를 반환하면서 Drawer가 열려있게 됩니다.
            true
        }
    }

    override fun onUpdateFlightIcon(newFlightIcon: Int) {   // flight update
        if (newFlightIcon != 0) {
            // 탭 레이아웃의 아이콘 변경
            //tabIconList[4] = newFlightIcon
            //binding.tabLayout.getTabAt(4)?.setIcon(newFlightIcon)
            tabTitles = listOf("MD's Pick", "Bucket List", "Home", "Calendar", "Flight")
            tabIconList = mutableListOf(
                R.drawable.ic_tablayout_mdpick,
                R.drawable.ic_tablayout_bucketlist,
                R.drawable.ic_tablayout_home,
                R.drawable.ic_tablayout_calendar,
                R.drawable.ic_book
            )

            Log.d("jy", "djskdk")

            TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
                tab.text = tabTitles[position]
                tab.setIcon(tabIconList[position])
            }.attach()
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

