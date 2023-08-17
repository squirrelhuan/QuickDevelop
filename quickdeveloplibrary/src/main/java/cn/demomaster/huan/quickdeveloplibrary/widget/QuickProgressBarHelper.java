package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.drawable.WrappedDrawable;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class QuickProgressBarHelper {
    private static final int[] TINT_ATTRS = {
            android.R.attr.indeterminateDrawable,
            android.R.attr.progressDrawable
    };

    private final ProgressBar mView;
    private Bitmap mSampleTile;

    QuickProgressBarHelper(ProgressBar view) {
        mView = view;
    }

    @SuppressLint("RestrictedApi")
    void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        @SuppressLint("RestrictedApi") TintTypedArray a = TintTypedArray.obtainStyledAttributes(mView.getContext(), attrs,
                TINT_ATTRS, defStyleAttr, 0);

        @SuppressLint("RestrictedApi") Drawable drawable = a.getDrawableIfKnown(0);
        if (drawable != null) {
           // mView.setIndeterminateDrawable(tileifyIndeterminate(drawable));
            Drawable drawable1 = mView.getContext().getResources().getDrawable(R.drawable.btn_green_bg01);
            mView.setIndeterminateDrawable(drawable1);
        }

        drawable = a.getDrawableIfKnown(1);
        if (drawable != null) {
            mView.setProgressDrawable(tileify(drawable, false));
        }

        a.recycle();
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    @SuppressLint("RestrictedApi")
    private Drawable tileify(Drawable drawable, boolean clip) {
        if (drawable instanceof WrappedDrawable) {
            @SuppressLint("RestrictedApi") Drawable inner = ((WrappedDrawable) drawable).getWrappedDrawable();
            if (inner != null) {
                inner = tileify(inner, clip);
                ((WrappedDrawable) drawable).setWrappedDrawable(inner);
            }
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable background = (LayerDrawable) drawable;
            final int N = background.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];

            for (int i = 0; i < N; i++) {
                int id = background.getId(i);
                outDrawables[i] = tileify(background.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }
            LayerDrawable newBg = new LayerDrawable(outDrawables);

            for (int i = 0; i < N; i++) {
                newBg.setId(i, background.getId(i));
            }

            return newBg;

        } else if (drawable instanceof BitmapDrawable) {
            final BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            final Bitmap tileBitmap = bitmapDrawable.getBitmap();
            if (mSampleTile == null) {
                mSampleTile = tileBitmap;
            }

            final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
            final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader(bitmapShader);
            shapeDrawable.getPaint().setColorFilter(bitmapDrawable.getPaint().getColorFilter());
            return clip ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL) : shapeDrawable;
        }

        return drawable;
    }

    /**
     * Convert a AnimationDrawable for use as a barberpole animation.
     * Each frame of the animation is wrapped in a ClipDrawable and
     * given a tiling BitmapShader.
     */
    private Drawable tileifyIndeterminate(Drawable drawable) {
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable background = (AnimationDrawable) drawable;
            final int N = background.getNumberOfFrames();
            AnimationDrawable newBg = new AnimationDrawable();
            newBg.setOneShot(background.isOneShot());

            for (int i = 0; i < N; i++) {
                Drawable frame = tileify(background.getFrame(i), true);
                frame.setLevel(10000);
                newBg.addFrame(frame, background.getDuration(i));
            }
            newBg.setLevel(10000);
            drawable = newBg;
        }
        return drawable;
    }

    private Shape getDrawableShape() {
        final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
        return new RoundRectShape(roundedCorners, null, null);
    }

    Bitmap getSampleTile() {
        return mSampleTile;
    }
}
