package au.edu.utas.testlab.android_bluetooth_dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";

    TabLayout tabLayout;
    ViewPager viewPager;
    MyPagerAdapter pagerAdapter;
    String[] titleList=new String[]{"Device List","Message Transfer"};
    List<Fragment> fragmentList=new ArrayList<>();

    DeviceListFragment deviceListFragment;
    DataTransFragment dataTransFragment;

    BluetoothAdapter bluetoothAdapter;

    Handler uiHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case Params.MSG_REV_A_CLIENT:
                    Log.e(TAG,"--------- uihandler set device name, go to data frag");
                    BluetoothDevice clientDevice = (BluetoothDevice) msg.obj;
                    dataTransFragment.receiveClient(clientDevice);
                    viewPager.setCurrentItem(1);
                    break;
                case Params.MSG_CONNECT_TO_SERVER:
                    Log.e(TAG,"--------- uihandler set device name, go to data frag");
                    BluetoothDevice serverDevice = (BluetoothDevice) msg.obj;
                    dataTransFragment.connectServer(serverDevice);
                    viewPager.setCurrentItem(1);
                    break;
                case Params.MSG_SERVER_REV_NEW:
                    String newMsgFromClient = msg.obj.toString();
                    dataTransFragment.updateDataView(newMsgFromClient, Params.REMOTE);
                    break;
                case Params.MSG_CLIENT_REV_NEW:
                    String newMsgFromServer = msg.obj.toString();
                    dataTransFragment.updateDataView(newMsgFromServer, Params.REMOTE);
                    break;
                case Params.MSG_WRITE_DATA:
                    String dataSend = msg.obj.toString();
                    dataTransFragment.updateDataView(dataSend, Params.ME);
                    deviceListFragment.writeData(dataSend);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        initUI();
    }

    /**
     * return uiHandler
     * @return
     */
    public Handler getUiHandler(){
        return uiHandler;
    }

    /**
     * initial main ui
     */
    private void initUI() {
        tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        viewPager= (ViewPager) findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText(titleList[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titleList[1]));

        deviceListFragment=new DeviceListFragment();
        dataTransFragment=new DataTransFragment();
        fragmentList.add(deviceListFragment);
        fragmentList.add(dataTransFragment);

        pagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * ViewPager adapter
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }
    }

    /**
     * Toast show
     */
    public void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
