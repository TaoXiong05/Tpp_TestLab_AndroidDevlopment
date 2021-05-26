package au.edu.utas.testlab.android_bluetooth_dev;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DataTransFragment extends Fragment {
    TextView connectNameTv;
    ListView showDataLv;
    EditText inputEt;
    Button sendBt;
    ArrayAdapter<String> dataListAdapter;

    MainActivity mainActivity;
    Handler uiHandler;

    BluetoothDevice remoteDevice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_data_trans, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        connectNameTv = (TextView) view.findViewById(R.id.device_name_tv);
        showDataLv = (ListView) view.findViewById(R.id.show_data_lv);
        inputEt = (EditText) view.findViewById(R.id.input_et);
        sendBt = (Button) view.findViewById(R.id.send_bt);
        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgSend = inputEt.getText().toString();
                Message message = new Message();
                message.what = Params.MSG_WRITE_DATA;
                message.obj = msgSend;
                uiHandler.sendMessage(message);

                inputEt.setText("");
            }
        });

        dataListAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_item_new_data);
        showDataLv.setAdapter(dataListAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        uiHandler = mainActivity.getUiHandler();
    }

    /**
     * Display connection to remote (client) device
     */
    public void receiveClient(BluetoothDevice clientDevice) {
        this.remoteDevice = clientDevice;
        connectNameTv.setText("Connect device: " + remoteDevice.getName());
    }

    /**
     * Show new message
     *
     * @param newMsg
     */
    public void updateDataView(String newMsg,int role) {

        if (role == Params.REMOTE) {
            String remoteName = remoteDevice.getName()==null ? "Unnamed device":remoteDevice.getName();
            newMsg = remoteName + " : " + newMsg;
        } else if (role == Params.ME){
            newMsg = "Me : " + newMsg;
        }
        dataListAdapter.add(newMsg);
    }

    /**
     * After the client connects to the server device, it displays
     *
     * @param serverDevice
     */
    public void connectServer(BluetoothDevice serverDevice) {
        this.remoteDevice = serverDevice;
        connectNameTv.setText("Connect device: " + remoteDevice.getName());
    }
}
