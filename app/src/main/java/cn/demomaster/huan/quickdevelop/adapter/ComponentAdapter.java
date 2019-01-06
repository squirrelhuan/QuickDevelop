package cn.demomaster.huan.quickdevelop.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

/**
 * Created by amitshekhar on 14/01/17.
 */

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
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_preview;
        ImageTextView item_icon;
        TextView item_name;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_preview = itemView.findViewById(R.id.rl_preview);
            item_name = itemView.findViewById(R.id.item_name);
        }

        public void onBind(final int position) {
            final Class clazz = data.get(position);
            //2.1判断该方法上是否存在@Widget
            boolean annotationPresent = clazz.isAnnotationPresent(ActivityPager.class);
            if (annotationPresent) {
                System.out.println("info方法上存在@Login注释");
            } else {
                System.out.println("info方法上不存在@Login注释");
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
                            Class[] parameterTypes={Context.class};
                            //根据参数类型获取相应的构造函数
                            java.lang.reflect.Constructor constructor=preViewClass.getConstructor(parameterTypes);
                            //参数数组
                            Object[] parameters={context};
                            View view = (View) constructor.newInstance(parameters);

                            //view.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                            int w = DisplayUtil.dp2px(context,60);
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w,w);
                            int m = DisplayUtil.dp2px(context,5);
                            layoutParams.setMargins(m,m,m,m);
                            view.setLayoutParams(layoutParams);
                            //view.setOnClickListener(null);
                            view.setClickable(false);
                            rl_preview.addView(view);
                        }
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    try {
                                        ((BaseActivityParent)context).startActivity(clazz);
                                    }catch (Exception e){
                                        throw new IllegalArgumentException("activityClass must be extends activity!");
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
                System.out.println(a);
            }


        }
    }

}