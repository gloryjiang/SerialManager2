package kg.delletenebre.serialmanager2

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewConfiguration
import kg.delletenebre.serialmanager2.fragments.CommandsFragment
import kg.delletenebre.serialmanager2.fragments.LogsFragment
import kg.delletenebre.serialmanager2.fragments.WidgetsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!App.getInstance().isSystemOverlaysPermissionGranted) {
            App.getInstance().requestSystemOverlaysPermission()
        }

        try {
            val config = ViewConfiguration.get(this)
            val menuKeyField = ViewConfiguration::class.java.getDeclaredField("sHasPermanentMenuKey")
            if (menuKeyField != null) {
                menuKeyField.isAccessible = true
                menuKeyField.setBoolean(config, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setupViewPager(xViewPager)
        xTabLayout.setupWithViewPager(xViewPager)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))

            R.id.restart_service -> {
                val communicationServiceIntent = Intent(this, CommunicationService::class.java)
                stopService(communicationServiceIntent)
                startService(communicationServiceIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val commandsFragmentInstance = CommandsFragment()

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(commandsFragmentInstance, "Commands")
        adapter.addFragment(WidgetsFragment(), "Widgets")
        adapter.addFragment(LogsFragment(), "Logs")
        viewPager.adapter = adapter

//        supportFragmentManager.addOnBackStackChangedListener {
//            val topFragment = getCurrentTopFragment(supportFragmentManager)
//            if (topFragment != null) {
//                if (topFragment is CommandsFragment) {
//                    commandsFragmentInstance.updateItemsList()
//                } else {
//
//                }
//            }
//        }
    }

    private fun getCurrentTopFragment(fm: FragmentManager): Fragment? {
        val stackCount = fm.backStackEntryCount

        if (stackCount > 0) {
            val backEntry: FragmentManager.BackStackEntry = fm.getBackStackEntryAt(stackCount - 1)
            return fm.findFragmentByTag(backEntry.name)
        } else {
            val fragments = fm.fragments;
            if (fragments != null && fragments.size > 0) {
                fragments
                        .filter { it != null && !it.isHidden }
                        .forEach { return it }
            }
        }
        return null
    }



    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        private val mFragmentList = mutableListOf<Fragment>()
        private val mFragmentTitleList = mutableListOf<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

    }
}