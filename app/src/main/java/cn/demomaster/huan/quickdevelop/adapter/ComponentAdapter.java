package cn.demomaster.huan.quickdevelop.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.qdrouter_library.view.ImageTextView;


public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder> {

    private List<Class> data;
    private Context context;

    public ComponentAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void updateList(List<Class> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(context, data, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup rl_preview;
        ImageTextView item_icon;
        TextView item_name;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_preview = itemView.findViewById(R.id.rl_preview);
            item_name = itemView.findViewById(R.id.item_name);
        }

        public void onBind(Context context, List<Class> data, final int position) {
            final Class clazz = data.get(position);
            //2.1判断该方法上是否存在@Widget
            boolean annotationPresent = clazz.isAnnotationPresent(ActivityPager.class);
            if (annotationPresent) {
                //QDLogger.i(clazz.getClass().getName()+"上存在@ActivityPager注释");
            }
            //获取注释
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation a : annotations) {
                //如果是@Login注释，则强制转化，并调用username方法，和password方法。
                if (a != null && a instanceof ActivityPager) {
                    Class preViewClass = ((ActivityPager) a).preViewClass();
                    //final Class activityClass = ((ActivityPager) a).activityClass();
                    String name = ((ActivityPager) a).name();
                    ResType resType = ((ActivityPager) a).resType();
                    item_name.setText(name);
                    rl_preview.removeAllViews();
                    try {
                        if (resType == ResType.Custome) {
                            //参数类型数组
                            Class[] parameterTypes = {Context.class};
                            //根据参数类型获取相应的构造函数
                            java.lang.reflect.Constructor constructor = preViewClass.getConstructor(parameterTypes);
                            //参数数组
                            Object[] parameters = {context};
                            View view = (View) constructor.newInstance(parameters);
                            //int w = DisplayUtil.dp2px(context, context.getResources().getDimensionPixelSize(R.dimen.dp_60));
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            //int m = DisplayUtil.dp2px(context, context.getResources().getDimensionPixelSize(R.dimen.dp_5));
                            //layoutParams.setMargins(m, m, m, m);
                            view.setLayoutParams(layoutParams);
                            view.setClickable(false);
                            view.setOnTouchListener(null);
                            rl_preview.addView(view);
                        } else if (resType == ResType.Resource) {
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            //int m = DisplayUtil.dp2px(context, context.getResources().getDimensionPixelSize(R.dimen.dp_5));
                            //layoutParams.setMargins(m, m, m, m);
                            ImageView imageView = new ImageTextView(context);
                            imageView.setLayoutParams(layoutParams);
                            int id = ((ActivityPager) a).iconRes();
                            imageView.setImageResource(id);
                            rl_preview.addView(imageView);
                        }
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (Fragment.class.isAssignableFrom(clazz)) {
                                        ((QDActivity) context).startFragment((QuickFragment) clazz.newInstance(),R.id.container1,null);
                                    } else if (Activity.class.isAssignableFrom(clazz)) {
                                        ((QDActivity) context).startActivity(clazz);
                                    }
                                } catch (Exception e) {
                                    QDLogger.e(e);
                                }
                            }
                        });
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}