package com.example.cloudreaderkotloin.bussiness.home.adapter

import android.text.Html
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class WanFragmentPagerAdapter : FragmentStatePagerAdapter{
    private lateinit var fm: FragmentManager
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var titles: List<String>
    private val registFragment = SparseArray<Fragment>()

    constructor(  fm: FragmentManager,fragments: ArrayList<Fragment>,titles: List<String>):super(fm){
        this.fm = fm
        this.fragments = fragments
        this.titles = titles

    }

    override fun getItem(position: Int): Fragment {
       return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registFragment.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}