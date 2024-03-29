package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Folder;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper.PHOTOHELPER_RESULT_PATHES;

/**
 * 图片选择器
 */
public class SimplePictureActivity extends QuickActivity {

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

        setTitle(getResources().getString(R.string.image_selector));
        getActionBarTool().getRightView().setText(getResources().getString(R.string.send));
        getActionBarTool().getRightView().setImageResource(0);
        getActionBarTool().getRightView().setTextSize(DisplayUtil.dp2px(mContext,16));
        getActionBarTool().getRightView().setOnClickListener(view -> {
            //拍照完成，返回对应图片路径
            Intent intent = new Intent();
            ArrayList<Image> images = mAdapter.getImages();
            Bundle bundle = new Bundle();
            bundle.putSerializable(PHOTOHELPER_RESULT_PATHES, images);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            mContext.finish();
        });
        //getActionBarTool().getActionBarTool().setBackgroundColor(getResources().getColor(R.color.white));
        //getActionBarTool().setStateBarColorAuto(true);
        init();
    }

    private TextView tvFolderName, tv_preview;
    private RecyclerView rvImage;
    private SimplePictureAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private void init() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionHelper.requestPermission(mContext, permission, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                initView();
            }

            @Override
            public void onRefused() {

            }
        });
    }

    private void initView() {
        tv_preview = findViewById(R.id.tv_preview);
        tv_preview.setOnClickListener(v -> previewImage());
        tvFolderName = findViewById(R.id.tv_folder_name);
        tv_preview.setVisibility(View.GONE);
        rl_mark = findViewById(R.id.rl_mark);
        rvImage = findViewById(R.id.recy_picture_grid);
        rvImage.setLayoutManager(mLayoutManager);
        rl_mark.setOnClickListener(v -> closeFolder());
        tvFolderName.setOnClickListener(v -> {
            if (isInitFolder) {
                if (isOpenFolder) {
                    closeFolder();
                } else {
                    openFolder();
                }
            }
        });
        PictureManager.loadImageForSDCard(mContext, folders1 -> {
            mFolders = folders1;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initAdapter();
                    initFolderList();
                }
            });
        });
    }

    private void previewImage() {
        Bundle bundle = new Bundle();
        ArrayList<Image> images = mAdapter.getImages();
        bundle.putSerializable("images", images);
        bundle.putInt("imageIndex", 0);
        startActivity(PreviewActivity.class, bundle);
    }

    private int maxCount = 3;
    public void initAdapter() {
        // 判断屏幕方向
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, 4);
        } else {
            mLayoutManager = new GridLayoutManager(this, 6);
        }
        rvImage.setLayoutManager(mLayoutManager);
        maxCount = getIntent().getIntExtra("MaxCount", 0);
        mAdapter = new SimplePictureAdapter(mContext, mFolders.get(0).getImages(), maxCount, true, rvImage);
        rvImage.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SimplePictureAdapter.OnItemClickListener() {
            @Override
            public void onItemPreview(View view, int position, Image image) {
                Bundle bundle = new Bundle();
                ArrayList<Image> images = new ArrayList<>();
                images.add(image);
                bundle.putSerializable("images", images);
                bundle.putInt("imageIndex", 0);
                startActivity(PreviewActivity.class, bundle);
            }

            @Override
            public void onItemClick(View view, int position, Map<Integer, Image> map) {
                if (map == null || map.size() == 0) {
                    tv_preview.setVisibility(View.GONE);
                } else {
                    tv_preview.setVisibility(View.VISIBLE);
                    tv_preview.setText(mContext.getResources().getString(R.string.preview) + "(" + map.size() + "/" + maxCount + ")");
                }
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
            //设置分隔线
            //rvFolder.addItemDecoration( new DividerGridItemDecoration(this ));
            //设置行级分割线使用的divider
            rvFolder.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

            FolderAdapter adapter = new FolderAdapter(mContext, mFolders);
            adapter.setOnFolderSelectListener(folder -> {
                setFolder(folder);
                closeFolder();
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
            tvFolderName.setText((TextUtils.isEmpty(folder.getName()) ? mContext.getResources().getString(R.string.image) : folder.getName()));
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
