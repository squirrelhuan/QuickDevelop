package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class FragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentsList;

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
		super(fm);
		this.fragmentsList = fragmentsList;
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentsList.get(arg0);
	}

}
