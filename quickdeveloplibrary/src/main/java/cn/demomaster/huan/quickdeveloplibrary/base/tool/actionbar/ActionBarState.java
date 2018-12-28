package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

/**
 * @author squirrel桓
 * @date 2018/12/28.
 * description：
 */
public  class ActionBarState {
    private OnLoadingStateListener onLoadingStateListener;

    public OnLoadingStateListener getOnLoadingStateListener() {
        return onLoadingStateListener;
    }

    public void setOnLoadingStateListener(OnLoadingStateListener onLoadingStateListener) {
        this.onLoadingStateListener = onLoadingStateListener;
    }

    //加载
    public static abstract class OnLoadingStateListener implements IOnLoadingStateListener{
        private Loading result;

        public Loading getResult() {
            return result;
        }

        public void setResult(Loading result) {
            this.result = result;
        }

        @Override
        public void loading() {
            onLoading(getResult());
        }

    }

    public static interface IOnLoadingStateListener {
        void loading();
        void onLoading(Loading loading);
        //void complete();
    }

    public static abstract class Loading implements ILoading{

    }

    public static interface ILoading{
        void success();
        void fail();
        void hide();
        void success(String message);
        void fail(String message);
    }
}
