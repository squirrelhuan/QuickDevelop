package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Folder;

public class SimplePictureActivity extends BaseActivityParent {

    /**
     * 横竖屏切换处理
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mLayoutManager != null && mAdapter != null) {
            //切换为竖屏
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mLayoutManager.setSpanCount(4);
            }
            //切换为横屏
            else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mLayoutManager.setSpanCount(6);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_picture);

        actionBarLayout.setTitle("图片选择器");
        init();
    }

    private TextView tvFolderName;
    private RecyclerView rvImage;
    private SimplePictureAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

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

        tvFolderName = (TextView) findViewById(R.id.tv_folder_name);
        rl_mark =  findViewById(R.id.rl_mark);
        rvImage = findViewById(R.id.recy_picture_grid);
        rvImage.setLayoutManager(mLayoutManager);
        rl_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFolder();
            }
        });
        tvFolderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitFolder) {
                    if (isOpenFolder) {
                        closeFolder();
                    } else {
                        openFolder();
                    }
                }
            }
        });
        PictureManager.loadImageForSDCard(mContext, new PictureManager.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Folder> folders1) {
                mFolders = folders1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter();
                        initFolderList();
                    }
                });
            }
        });
    }

   public void initAdapter(){
       // 判断屏幕方向
       Configuration configuration = getResources().getConfiguration();
       if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
           mLayoutManager = new GridLayoutManager(this, 4);
       } else {
           mLayoutManager = new GridLayoutManager(this, 6);
       }
       rvImage.setLayoutManager(mLayoutManager);
       mAdapter = new SimplePictureAdapter(mContext, mFolders.get(0).getImages(), 3,true);
       rvImage.setAdapter(mAdapter);

       mAdapter.setOnItemClickListener(new SimplePictureAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {

           }
       });
    }

    private ArrayList<Folder> mFolders;
    private Folder mFolder;
    private boolean isInitFolder;
    private boolean isOpenFolder;
    private RecyclerView rvFolder;
    private RelativeLayout rl_mark;
    /**
     * 初始化图片文件夹列表
     */
    private void initFolderList() {
        rvFolder = findViewById(R.id.rv_folder);
        if (mFolders != null && !mFolders.isEmpty()) {
            isInitFolder = true;
            rvFolder.setLayoutManager(new LinearLayoutManager(mContext));
            FolderAdapter adapter = new FolderAdapter(mContext, mFolders);
            adapter.setOnFolderSelectListener(new FolderAdapter.OnFolderSelectListener() {
                @Override
                public void OnFolderSelect(Folder folder) {
                    setFolder(folder);
                    closeFolder();
                }
            });
            rvFolder.setAdapter(adapter);
        }
    }

    /**
     * 设置选中的文件夹，同时刷新图片列表
     *
     * @param folder
     */
    private void setFolder(Folder folder) {
        if (folder != null && mAdapter != null && !folder.equals(mFolder)) {
            mFolder = folder;
            tvFolderName.setText((TextUtils.isEmpty(folder.getName())?mContext.getResources().getString(R.string.image):folder.getName()));
            rvImage.scrollToPosition(0);
            mAdapter.refresh(folder.getImages(), false);
        }
    }

    /**
     * 弹出文件夹列表
     */
    private void openFolder() {
        if (!isOpenFolder) {
            rl_mark.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY",
                    rvFolder.getHeight(), 0).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    rvFolder.setVisibility(View.VISIBLE);
                }
            });
            animator.start();
            isOpenFolder = true;
        }
    }
    /**
     * 收起文件夹列表
     */
    private void closeFolder() {
        if (isOpenFolder) {
            rl_mark.setVisibility(View.GONE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY",
                    0, rvFolder.getHeight()).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rvFolder.setVisibility(View.GONE);
                }
            });
            animator.start();
            isOpenFolder = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && isOpenFolder) {
            closeFolder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
