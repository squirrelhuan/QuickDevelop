package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Folder;

public class SimplePictureActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_picture);

        actionBarLayout.setTitle("图片选择器");
        init();
    }

    private RecyclerView recy_picture_grid;
    private SimplePictureAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Folder> folders = new ArrayList<>();

    private void init() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionManager.chekPermission(mContext, permission, new PermissionManager.OnCheckPermissionListener() {
            @Override
            public void onPassed() {
               initView();
            }

            @Override
            public void onNoPassed() {

            }
        });


    }

    private void initView() {

        PictureManager.loadImageForSDCard(mContext, new PictureManager.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Folder> folders1) {
                folders = folders1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter();
                    }
                });
            }
        });



    }

   public void initAdapter(){
       recy_picture_grid = findViewById(R.id.recy_picture_grid);
       mLayoutManager = new GridLayoutManager(this, 3);
       recy_picture_grid.setLayoutManager(mLayoutManager);
       adapter = new SimplePictureAdapter(mContext, folders.get(0).getImages(), 1, true,true);
       recy_picture_grid.setAdapter(adapter);

       adapter.setOnItemClickListener(new SimplePictureAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {

           }
       });
    }
}
