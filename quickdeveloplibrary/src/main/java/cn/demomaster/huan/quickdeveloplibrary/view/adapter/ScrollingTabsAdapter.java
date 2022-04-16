
package cn.demomaster.huan.quickdeveloplibrary.view.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cn.demomaster.huan.quickdeveloplibrary.R;


public class ScrollingTabsAdapter extends TabAdapter {

    private final Activity activity;

    public ScrollingTabsAdapter(Activity act) {
        activity = act;
    }

    Set<String> tabs_set;

    @Override
    public View getView(int position, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final Button tab = (Button) inflater.inflate(R.layout.tabs, null);

        //Get default values for tab visibility preferences
        final String[] mTitles = activity.getResources().getStringArray(R.array.tab_themt_titles);

        //Get tab visibility preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        Set<String> defaults = new HashSet<>(Arrays.asList(mTitles));
        tabs_set = sp.getStringSet("", defaults);
        //if its empty fill reset it to full defaults
        //stops app from crashing when no tabs1 are shown
        //TODO:rewrite activity to not crash when no tabs1 are chosen to show
        //or display error when no option is chosen
        if (tabs_set.size() == 0)
            tabs_set = defaults;

        //MultiSelectListPreference fails to preserve order of options chosen
        //Re-order based on order of default options array
        //This ensures titles are attached to correct tabs1/pages
        String[] tabs_new = new String[tabs_set.size()];
        int cnt = 0;
        int count = mTitles.length;
        for (String mTitle : mTitles) {
            if (tabs_set.contains(mTitle)) {
                tabs_new[cnt] = mTitle;
                cnt++;
            }
        }
        //Set the tab text
        if (position < tabs_new.length)
            tab.setText(tabs_new[position].toUpperCase());

        // Theme chooser
        //ThemeUtils.setTextColor(activity, tab, "tab_text_color");
        return tab;
    }

    @Override
    public int getViewCount() {
        return tabs_set == null ? 0 : tabs_set.size();
    }

    @Override
    public boolean onSelectedChange(int position, View view, boolean isSelected) {
        return false;
    }
}
