package com.frame.basic.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class AppUtils {
    public static final int uninstall = 10000;// 没有安装

    /**
     * 获取版本名称
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 描述：打开并安装文件
     *
     * @param context the context
     * @param file    apk文件路径
     */
    public static void installApk(Activity context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            installApk_24(file, context, context.getPackageName() + ".fileprovider");
        } else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            context.startActivityForResult(intent, uninstall);
        }
    }

    /**
     * 检查APK是否有效
     *
     * @param context
     * @param filePath
     * @return
     */
    public static boolean checkApk(Context context, @Nullable String filePath) {
        PackageManager pm = context.getPackageManager();
        if (pm == null || TextUtils.isEmpty(filePath)) {
            return false;
        }

        try {
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            return info != null;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }

    /**
     * 获取安装的Intent
     *
     * @param context
     * @param file
     * @param provider
     * @return
     */
    public static Intent getInstallIntent(Activity context, File file, String provider) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 24) {
                Uri apkUri = FileProvider.getUriForFile(context, provider, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            return intent;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return null;
    }

    /**
     * 描述：打开并安装文件
     *
     * @param context the context
     * @param file    apk文件路径
     */
    public static void installApk(Activity context, File file, String provider) {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                installApk_24(file, context, provider);
            } else {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                context.startActivityForResult(intent, uninstall);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * 安装APK for API24及以上
     *
     * @param var0
     * @param var1
     */
    private static void installApk_24(File var0, Activity var1, String provider) {
        Uri apkUri = FileProvider.getUriForFile(var1, provider, var0);//在AndroidManifest中的android:authorities值
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        var1.startActivity(install);
    }

    /**
     * APP是否已安装
     *
     * @param context
     * @param packageName 需检测的App的包名
     * @return
     */
    public static boolean appIsInstalled(Context context, @Nullable String packageName) {
        PackageManager pm = context.getPackageManager();
        if (TextUtils.isEmpty(packageName) || pm == null) {
            return false;
        }

        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return appInfo != null;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }

    /**
     * 打开指定APP
     *
     * @param context
     * @param packageName
     */
    public static void openApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return;
        }
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            intent = new Intent();
        }
        intent.setPackage(packageName);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * APP是否在运行中
     *
     * @param context
     * @param packageName 需检测的App的包名
     * @return
     */
    @Deprecated
    public static boolean appIsRunning(Context context, @Nullable String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null || TextUtils.isEmpty(packageName)) {
            return false;
        }

        List<RunningTaskInfo> taskInfo = am.getRunningTasks(Integer.MAX_VALUE);
        if (taskInfo == null || taskInfo.size() == 0) {
            return false;
        }

        for (RunningTaskInfo aInfo : taskInfo) {
            if (aInfo.baseActivity != null && packageName.equals(aInfo.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    /**
     * 描述：卸载程序.
     *
     * @param context     the context
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param ctx       the ctx
     * @param className 判断的服务名字 "com.xxx.xx..XXXService"
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        Iterator<RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            RunningServiceInfo si = l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 进程是否已存在
     *
     * @param context
     * @param processName
     * @return
     */
    public static boolean isRunningTaskExist(Context context, String processName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null) {
            for (ActivityManager.RunningAppProcessInfo info : processList) {
                if (info.processName.equals(processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 停止服务.
     *
     * @param ctx       the ctx
     * @param className the class name
     * @return true, if successful
     */
    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    // Check if filename is "cpu", followed by a single digit
                    // number
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    /**
     * 描述：判断网络是否有效.
     *
     * @param context the context
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Gps是否打开 需要<uses-permission
     * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
     *
     * @param context the context
     * @return true, if is gps enabled
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;

    }

    /**
     * 判断当前网络是否是wifi网络.
     *
     * @param context the context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断当前网络是否是3G网络.
     *
     * @param context the context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 设置是否打开Wifi
     *
     * @param context
     * @param enabled
     */
    public static void openWiFi(Context context, boolean enabled) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }

    /**
     * 设置是否打开数据网络
     *
     * @param context
     * @param enabled
     */
    public static void open3G(Context context, boolean enabled) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // ConnectivityManager类
        Class<?> connectivityManagerClass = null;
        // ConnectivityManager类中的字段
        Field connectivityManagerField = null;

        // IConnectivityManager接口
        Class<?> iConnectivityManagerClass = null;
        // IConnectivityManager接口的对象
        Object iConnectivityManagerObject = null;
        // IConnectivityManager接口的对象的方法
        Method setMobileDataEnabledMethod = null;

        try {
            // 取得ConnectivityManager类
            connectivityManagerClass = Class.forName(connectivityManager
                    .getClass().getName());
            // 取得ConnectivityManager类中的字段mService
            connectivityManagerField = connectivityManagerClass
                    .getDeclaredField("mService");
            // 取消访问私有字段的合法性检查
            // 该方法来自java.lang.reflect.AccessibleObject
            connectivityManagerField.setAccessible(true);

            // 实例化mService
            // 该get()方法来自java.lang.reflect.Field
            // 一定要注意该get()方法的参数:
            // 它是mService所属类的对象
            // 完整例子请参见:
            // http://blog.csdn.net/lfdfhl/article/details/13509839
            iConnectivityManagerObject = connectivityManagerField
                    .get(connectivityManager);
            // 得到mService所属接口的Class
            iConnectivityManagerClass = Class
                    .forName(iConnectivityManagerObject.getClass().getName());
            // 取得IConnectivityManager接口中的setMobileDataEnabled(boolean)方法
            // 该方法来自java.lang.Class.getDeclaredMethod
            setMobileDataEnabledMethod = iConnectivityManagerClass
                    .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            // 取消访问私有方法的合法性检查
            // 该方法来自java.lang.reflect.AccessibleObject
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConnectivityManagerObject,
                    enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改文件权限
     *
     * @param permission
     * @param path
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
            Log.i("修改文件权限", "修改权限成功");
        } catch (IOException e) {
            Log.e("修改文件权限", "修改权限失败");
            e.printStackTrace();
        }
    }

    /**
     * 获得屏幕宽高
     *
     * @param context
     * @return [0]width [1]height
     */
    public static int[] getScreenHW(Context context) {
        int[] screenHW = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenHW[1] = dm.heightPixels + getStatusBarHeight(context);
        screenHW[0] = dm.widthPixels;
        return screenHW;
    }

    /**
     * 判断应用是否已经在前台了
     *
     * @param context
     * @return
     */
    @Deprecated
    public static boolean IsForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 屏幕是否亮屏
     *
     * @return true为打开，false为关闭
     */
    public static boolean isLight(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
    }

    /**
     * 屏幕是否解锁
     *
     * @param context
     */
    public static boolean isLock(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    /**
     * 获得控件宽高
     *
     * @param view 控件
     * @return 0：w   1:h
     */
    public static int[] getViewWH(View view) {
        int[] wh = new int[2];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        wh[0] = width;
        wh[1] = height;
        return wh;
    }

    /**
     * 檢查是否有某個權限
     *
     * @param permission
     * @param context
     * @return
     */
    public static boolean checkPermissiont(String permission, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean per = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permission, context.getPackageName()));
        return per;
    }

    // 判断权限集合
    public static boolean checkPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (checkPermission(permission, context)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    public static boolean checkPermission(String permission, Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(context);
        } else {
            return checkPermissiont(permission, context);
        }
    }

    /**
     * 根据名称获取控件id
     *
     * @param context
     * @param viewName
     * @return
     * @throws Exception
     */
    public static int getViewResourceId(Context context, String viewName) throws Exception {
        return context.getResources().getIdentifier(
                context.getPackageName() + ":id/" + viewName, null, null);
    }

    /**
     * 根据名称获取mipmap的id
     *
     * @param context
     * @param mipmapName
     * @return
     */
    public static int getMipmapResourseId(Context context, String mipmapName) {
        Resources resources = context.getResources();
        return resources.getIdentifier(context.getPackageName() + ":mipmap/" + mipmapName, null, null);
    }

    /**
     * 根据名称获取drawable的id
     *
     * @param context
     * @param drawableName
     * @return
     */
    public static int getDrawableResourseId(Context context, String drawableName) {
        Resources resources = context.getResources();
        return resources.getIdentifier(context.getPackageName() + ":drawable/" + drawableName, null, null);
    }

    /**
     * 判断是否安装了某个应用
     *
     * @param context
     * @param packagename
     * @return
     */
    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = context.getResources();
        }
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    /**
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 获取根View
     *
     * @param activity
     * @return
     */
    public static View getRootView(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 关闭键盘
     */
    public static void closeSoftInputWindow(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 自动关闭软键盘
     *
     * @param rootView 根view
     */
    public static void autoCloseSoftInputWindow(View rootView) {

        if (rootView != null) {
            if (!(rootView instanceof EditText)) {
                if (rootView != null) {
                    rootView.setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            closeSoftInputWindow(view);
                            return false;
                        }
                    });
                }
            }

            if (rootView instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
                    View inView = ((ViewGroup) rootView).getChildAt(i);
                    autoCloseSoftInputWindow(inView);
                }
            }
        } else {
            throw new NullPointerException("root view is null");
        }
    }

    /**
     * 是否是主进程
     * 避免多进程造成Application多次启动，进而造成Service重复启动的问题
     *
     * @return
     */
    public static boolean isMainProcess(Application application) {
        String processName = getProcessName(application);
        String packageName = application.getPackageName();
        if (packageName.equals(processName)) {
            return true;
        }
        return false;
    }

    /**
     * 获取进程名称
     *
     * @param application
     * @return
     */
    @Nullable
    public static String getProcessName(Application application) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 全屏
     *
     * @param isFull
     * @param activity
     */
    public static void fullScreen(boolean isFull, Activity activity) {//控制是否全屏显示
        if (isFull) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                activity.getWindow().setAttributes(lp);
                // 设置页面全屏显示
                final View decorView = activity.getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            showNavigationBar(activity);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void hideNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static void showNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 设置虚拟键的背景色
     *
     * @param activity
     * @param color
     */
    public static void setNavigationBarColor(Activity activity, int color) {
        // 设置虚拟按键背景颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * 打开应用通知设置页面
     *
     * @param context
     */
    public static void gotoNotificationSetting(Context context) {
        try {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", context.getPackageName());
                intent.putExtra("app_uid", context.getApplicationInfo().uid);
            } else {
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 通知是否被打开
     *
     * @param context
     * @return
     */
    public static boolean notificationOpened(Context context) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOpened;
    }

    /**
     * 获取正在运行的APP列表
     *
     * @param context
     * @return
     */
    public static List<String> getRunningApps(Context context) {
        PackageManager localPackageManager = context.getPackageManager();
        List<PackageInfo> localList = localPackageManager.getInstalledPackages(0);
        List<String> runningApps = new ArrayList<>();
        for (int i = 0; i < localList.size(); i++) {
            PackageInfo localPackageInfo1 = localList.get(i);
            String str1 = localPackageInfo1.packageName.split(":")[0];
            if (((ApplicationInfo.FLAG_SYSTEM & localPackageInfo1.applicationInfo.flags) == 0) && ((ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & localPackageInfo1.applicationInfo.flags) == 0) && ((ApplicationInfo.FLAG_STOPPED & localPackageInfo1.applicationInfo.flags) == 0)) {
                runningApps.add(str1);
            }
        }
        return runningApps;
    }

    /**
     * 判断某个APP是否在运行
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isRunningApp(Context context, String packageName) {
        return getRunningApps(context).contains(packageName);
    }

    /**
     * 打开应用市场中当前应用的页面
     *
     * @param context
     */
    public static boolean openAppMarket(Context context) {
        return openAppMarket(context, context.getPackageName());
    }

    /**
     * 打开应用市场
     *
     * @param context
     */
    public static boolean openAppMarket(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * APP进行隐式跳转或跳转到第三方APP页面时使用此方法
     *
     * @param context
     * @param intent
     */
    public static void startActivitySafely(Context context, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 判断Context是否是有效的
     *
     * @param context
     * @return
     */
    public static boolean contextIsEnabled(@Nullable Context context) {
        if (context == null) {
            return false;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return false;
            }
        }

        return true;
    }
}
