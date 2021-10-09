package cn.demomaster.huan.quickdeveloplibrary.helper.toast.compat;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author drakeet
 */
final class SafeToastContext extends ContextWrapper {

   // private @NonNull Toast toast;

    //private @Nullable BadTokenListener badTokenListener;

    SafeToastContext(@NonNull Context base, @NonNull Toast toast) {
        super(base);
       // this.toast = toast;
    }

    @Override
    public Context getApplicationContext() {
        return new ApplicationContextWrapper(getBaseContext().getApplicationContext());
    }

    public void setBadTokenListener(@NonNull BadTokenListener badTokenListener) {
       // this.badTokenListener = badTokenListener;
    }

    private static final class ApplicationContextWrapper extends ContextWrapper {

        private ApplicationContextWrapper(@NonNull Context base) {
            super(base);
        }

        @Override
        public Object getSystemService(@NonNull String name) {
            if (Context.WINDOW_SERVICE.equals(name)) {
                return new WindowManagerWrapper((WindowManager) getBaseContext().getSystemService(name));
            }
            return super.getSystemService(name);
        }
    }

    private static final class WindowManagerWrapper implements WindowManager {
        private final @NonNull
        WindowManager base;

        private WindowManagerWrapper(@NonNull WindowManager base) {
            this.base = base;
        }

        @Override
        public Display getDefaultDisplay() {
            return base.getDefaultDisplay();
        }


        @Override
        public void removeViewImmediate(View view) {
            base.removeViewImmediate(view);
        }

        @Override
        public void addView(View view, ViewGroup.LayoutParams params) {
            try {
                //QDLogger.println("WindowManager's addView(view, params) has been hooked.");
                base.addView(view, params);
            } catch (BadTokenException e) {
                QDLogger.e(e);
               /* if (badTokenListener != null) {
                    badTokenListener.onBadTokenCaught(toast);
                }*/
            } catch (Throwable throwable) {
                QDLogger.e(throwable);
                //Log.e(TAG, "[addView]", throwable);
            }
        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
            base.updateViewLayout(view, params);
        }


        @Override
        public void removeView(View view) {
            //QDLogger.e("removeView");
            base.removeView(view);
        }
    }
}
