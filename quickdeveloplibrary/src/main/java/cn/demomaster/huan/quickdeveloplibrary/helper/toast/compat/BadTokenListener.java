package cn.demomaster.huan.quickdeveloplibrary.helper.toast.compat;

import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * @author drakeet
 */
public interface BadTokenListener {

  void onBadTokenCaught(@NonNull Toast toast);
}
