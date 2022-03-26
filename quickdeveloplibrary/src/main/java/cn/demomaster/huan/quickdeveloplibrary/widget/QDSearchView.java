package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.TintTypedArray;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class QDSearchView extends LinearLayout {
    public QDSearchView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QDSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public QDSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }
    EditText abc_et_input;
    CharSequence mQueryHint;
    private SearchView.OnQueryTextListener mOnQueryChangeListener;
    @SuppressLint("RestrictedApi")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) {
            return;
        }

       @SuppressLint("RestrictedApi") final TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context,
                attrs, R.styleable.SearchView, R.attr.searchViewStyle, 0);
        //TypedArray typedArray = context.obtainStyledAttributes(attrs, androidx.appcompat.R.styleable.SearchView, defStyleAttr, 0);
        //TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        //mDefaultQueryHint = a.getText(R.styleable.SearchView_defaultQueryHint);
        //mQueryHint = typedArray.getText(androidx.appcompat.R.styleable.SearchView_queryHint);
        mQueryHint = typedArray.getString(androidx.appcompat.R.styleable.SearchView_queryHint);
        Drawable drawable = typedArray.getDrawable(R.styleable.SearchView_searchIcon);
        final LayoutInflater inflater = LayoutInflater.from(context);
       /* final int layoutResId = a.getResourceId(
                R.styleable.SearchView_layout, R.layout.quick_search_view);*/
        View view = inflater.inflate(R.layout.quick_search_view, null, true);
        addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        abc_et_input = findViewById(R.id.abc_et_input);
        //mSearchSrcTextView.setSearchView(this);
        abc_et_input.setHint(mQueryHint);
        abc_et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mOnQueryChangeListener!=null){
                    mOnQueryChangeListener.onQueryTextChange(s.toString());
                }
            }
        });
        ImageView search_mag_icon = findViewById(R.id.search_mag_icon);
        search_mag_icon.setImageDrawable(drawable);
        ImageView mCloseButton = findViewById(R.id.abc_search_close_btn);
        mCloseButton.setImageDrawable(typedArray.getDrawable(R.styleable.SearchView_closeIcon));
        mCloseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                abc_et_input.setText("");
            }
        });
        typedArray.recycle();
    }

    public void setOnQueryTextListener(SearchView.OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }
}
