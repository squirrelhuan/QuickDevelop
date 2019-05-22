package cn.demomaster.huan.quickdevelop.fragment.helper.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseActivity;

/**
 * @ClassName: BaseActivity
 * @Description:Acitivity基类
 * @author 陈恒飞
 * @date 2014年4月12日 上午11:54:43
 * 
 */
public abstract class LBaseActivity extends QDBaseActivity {
	//protected View mView;
	protected CustomProgressDialog progressDialog;

/*

	*/
/**
	 * @Title: getContentViewId
	 * @Description: 布局文件Id
	 *//*

	protected abstract int getContentViewId();
*/

	/**
	 * @Title: IniView
	 * @Description: 初始化View
	 */
	protected abstract void IniView();

	/**
	 * @Title: IniLister
	 * @Description: 初始化接口
	 */
	protected abstract void IniLister();

	/**
	 * @Title: IniData
	 * @Description: 初始化数据
	 */
	protected abstract void IniData();

	/**
	 * thisFinish 当前关闭
	 * 
	 */
	protected abstract void thisFinish();

	@Override
	public void onBackPressed() {
		thisFinish();
	}

	/**
	 * showProgressDialog 显示等待框
	 * 
	 * @param text
	 *            显示文字
	 * 
	 */
	public void showProgressDialog(String text) {
		if (progressDialog != null) {
			progressDialog.show();
			progressDialog.setMessage(text);
		}
	}

	/**
	 * cancelProgressDialog 取消等待框
	 * 
	 */
	public void cancelProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
