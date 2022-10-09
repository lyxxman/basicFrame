package com.frame.basic.base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.lang.reflect.InvocationTargetException;

/**
 * @Description: 解决键盘挡住输入框问题
 * @Author: fanj
 * @CreateDate: 2022/2/11 10:46
 * @Version: 1.0.2
 */
public class SoftHideKeyBoardUtil {
    public static void assistActivity(Activity activity, boolean isFullScreen){
        new SoftHideKeyBoardUtil(activity, isFullScreen);
    }
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private SoftHideKeyBoardUtil(Activity activity, boolean isFullScreen){
        //1.找到Activity的最外层布局控件，他其实是一个DecorView，它所用的控件就是FrameLayout
        FrameLayout content = activity.findViewById(android.R.id.content);
        //2.获取到setContentView放进去的View
        mChildOfContent = content.getChildAt(0);
        if (mChildOfContent != null){
            //3.给Activity的xml布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                //4.软键盘弹起会使GlobalLayout发生变化
                @Override
                public void onGlobalLayout() {
                    //5.当前布局发生变化时，对Activity的xml布局进行重绘
                    possiblyResizeChildOfContent(activity, isFullScreen);
                }
            });
            //6.获取到Activity的xml布局的放置参数
            frameLayoutParams = (FrameLayout.LayoutParams)mChildOfContent.getLayoutParams();
        }
    }
    //获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度
    private void possiblyResizeChildOfContent(Activity activity, boolean isFullScreen){
        //1.获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        int useableHeightNow = computeUsableHeight(isFullScreen);
        //2.如果当前可用高度和原始值不一样
        if(useableHeightNow != usableHeightPrevious){
            //3.获取Activity中xml中的布局在当前界面显示的高度
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            //4.Activity中xml布局的高度-当前可用高度
            int heightDifference = usableHeightSansKeyboard - useableHeightNow;
            //5.高度差>屏幕1/4时，说明键盘弹出
            if (heightDifference > (usableHeightSansKeyboard/4)){
                //6.键盘弹出了，Activity的xml布局高度应当减去键盘高度
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            }else{
                //6.键盘收取了，Activity的xml布局高度应当和可用高度一样
                frameLayoutParams.height = usableHeightSansKeyboard-getNavigationBarHeight(activity, activity.getWindow());
            }
            //7.重绘Activity的xml布局
            mChildOfContent.requestLayout();
            usableHeightPrevious = useableHeightNow;
        }
    }

    private int computeUsableHeight(boolean isFullScreen) {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        //全屏模式下：直接返回r.bottom, r.top其实是状态栏的高度
        if (isFullScreen){
            return (r.bottom);
        }else{
            return (r.bottom - r.top);
        }
    }

    private int getNavigationBarHeight(Context context, Window window){
        /*boolean isShowNavigation = checkNavigationBarShow(context, window);
        Point point = getNavigationBarSize(context);
        return point.y;*/ //该方法部分机器失效
        Display display = window.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        View decorView = window.getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            return Math.abs(point.x - contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            return Math.abs(rect.bottom - point.y);
        }
    }

    private Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        return size;
    }
    /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @param window  当前窗口
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    public static boolean checkNavigationBarShow(Context context, Window window) {
        boolean show;
        Display display = window.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        View decorView = window.getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            show = (point.x != contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            show = (rect.bottom != point.y);
        }
        return show;
    }

    public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom);
        }
        return false;
    }
}
