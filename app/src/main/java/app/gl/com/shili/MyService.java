package app.gl.com.shili;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private Boolean myflags = false;
    private String data = "服务执行";
    private static final String TAG = "ActivityTest";
    private Callback callback;
    public DBHelper dbHelper;


    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "服务器已绑定");
        return new Binder();
    }


    public class Binder extends android.os.Binder{
        public void setData(String data){
            MyService.this.data = data;
        }
        public MyService getMyService(){
            return MyService.this;
        }
    }
    @Override
    public void onCreate() {
        Log.e("aaaa","创建");
        super.onCreate();
        myflags = true;
        new Thread(){//创建新的线程
            @Override
            public void run() {//运行线程
                super.run();
                int i =1;
                while(myflags){
                    //System.out.println("程序正在运行.....");
                    try {
                        String str = i + ":" +data;
                        Log.d(TAG, str);
                        if (callback != null){
                            callback.onDataChange(str);
                        }
                        sleep(1000);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Toast.makeText(MyService.this, "出错了", Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d(TAG, "服务器已停止");
            }

        }.start();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("aaaa","启动");
        //data = intent.getStringExtra("data");
        Log.e("service",data);
        //flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("aaa","销毁");
        myflags = false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("aaaa","解绑");
        return super.onUnbind(intent);
    }



    public static interface Callback{
        void onDataChange(String data);
    }

}
