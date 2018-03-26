package app.gl.com.shili;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn_main_start;
    private Button btn_main_stop;
    private ListView lv_main_lv;
    private EditText et_main_position;
    private TextView tv_main_tv;
    private EditText et_main_coding;




    private PersonAdapter adapter;
    private List<Person> data;
    private PersonDao pDao;
    private ServiceConnection conn;//与服务器的连接
    private MyService.Binder myBinder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_main_lv = findViewById(R.id.lv_main_lv);
        tv_main_tv = findViewById(R.id.tv_main_tv);
        btn_main_start = findViewById(R.id.btn_main_start);
        btn_main_stop = findViewById(R.id.btn_main_stop);
        //获得地址和编号
        et_main_position = findViewById(R.id.et_main_position);
        et_main_coding = findViewById(R.id.et_main_coding);
        String position = et_main_position.getText().toString();
        String coding = et_main_coding.getText().toString();
        //pDao = new PersonDao(this,position,coding);
        pDao = new PersonDao(this);
        data = pDao.queryAll();
        btn_main_start.setOnClickListener(this);
        btn_main_stop.setOnClickListener(this);
        lv_main_lv.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        //创建适配器adapter
        adapter = new PersonAdapter();
        Intent intent = new Intent(this, MyService.class);
        switch (view.getId()){
            case R.id.btn_main_start://启动并绑定服务
                //intent.putExtra("data", tv_main_text1.getText().toString());
                //启动服务
                startService(intent);
                //绑定服务
                bindService();
                break;
            case R.id.btn_main_stop://终止服务
                stopService(intent);
                //清空适配器adapter
                adapter = null;
                //解除绑定
                if (conn != null) {
                    unbindService(conn);
                    conn = null;
                } else {
                    Toast.makeText(this, "还没有绑定", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    public void bindService(){
        Intent intent = new Intent(this, MyService.class);
        //创建连接对象
        if (conn == null) {
            conn = new ServiceConnection() {
                //一旦绑定成功就会执行该函数
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    //获得service传来的IBinder
                    myBinder = (MyService.Binder) iBinder;
                    // myBinder.getMyService()==ServiceCeShi
                    myBinder.getMyService().setCallback(new MyService.Callback(){
                        @Override
                        public void onDataChange(String data) {//data为service传来的数据
                            Message msg = new Message();
                            Bundle b = new Bundle();
                            b.putString("data",data);
                            msg.setData(b);
                            hander.sendMessage(msg);
                        }
                    });

                }

                private Handler hander = new Handler(){

                    @Override
                    public void handleMessage(Message msg) {//添加数据
                        super.handleMessage(msg);

                        //加载适配器adapter
                        lv_main_lv.setAdapter(adapter);
                        //保存数据
                        String name = msg.getData().getString("data");
                        //获得所有用户信息
                        List<Person> persons = pDao.queryAll();
                        //判断用户名是否存在
                        boolean b = true;
                        for (Person per : persons){
                            if (per.getName().equals(name)){
                                b = false;
                            }
                        }
                        if (b){
                            Person person = new Person(-1,name);
                            pDao.add(person);//调用add后id发生变化
                            //倒序保存数据到list
                            data.add(0,person);
                            //更新界面的列表
                            adapter.notifyDataSetChanged();
                        }
                        tv_main_tv.setText(msg.getData().getString("data"));
                    }
                };

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                }
            };
            //绑定service
            bindService(intent, conn, BIND_AUTO_CREATE);
        } else {
            Toast.makeText(this, "已经绑定", Toast.LENGTH_SHORT);
        }

        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    //展示数据到ListView
    class PersonAdapter extends BaseAdapter {

        //获得条数
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(MainActivity.this, android.R.layout.simple_list_item_1,null);
            }
            Person person =  data.get(i);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(person.getName());
            return view;
        }
    }
}