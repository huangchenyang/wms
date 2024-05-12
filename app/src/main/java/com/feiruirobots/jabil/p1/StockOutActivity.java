package com.feiruirobots.jabil.p1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.adapter.MyListAdapter;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.common.ToastUtil;
import com.feiruirobots.jabil.p1.http.CallServer;
import com.feiruirobots.jabil.p1.http.HttpResponse;
import com.feiruirobots.jabil.p1.model.BIZ_TASK_STATUS;
import com.feiruirobots.jabil.p1.model.BillData;
import com.feiruirobots.jabil.p1.model.BizTask;
import com.feiruirobots.jabil.p1.model.FUNCTION;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hutool.core.util.StrUtil;

public class StockOutActivity extends BaseActivity {
    private String function;
    private MyListAdapter<BizTaskView, BillData> adapter;
    private List<BillData> bizTaskList = new ArrayList<>();
    private String TAG = "hcy--StockOutActivity";

    @BindView(R.id.lv_biz_task)
    ListView lv_biz_task;
    @BindView(R.id.et_perference)
    EditText et_perference;
    @SuppressLint("HandlerLeak")
    private Handler handlerSend = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 重写handleMessage方法
            super.handleMessage(msg);
            if (msg.what == 1) {
//                getBizTask();
                handlerSend.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        function = intent.getStringExtra("FUNCTION");
        initTitle("Retrieve->"+Objects.requireNonNull(FUNCTION.of(function)).msg);
        initListView();
        handlerSend.sendEmptyMessageDelayed(1, 1000);
        et_perference.addTextChangedListener(new StockOutActivity.JumpTextWatcher(et_perference));
        getBizTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁之前进行清理工作
        handlerSend.removeCallbacksAndMessages(null);
    }

    class BizTaskView {
        TextView tv_pallet_id, tv_fgtf, tv_binid, tv_box_count,tv_terminal_in;
    }

    private void initListView() {
        adapter = new MyListAdapter<BizTaskView, BillData>(this, bizTaskList, R.layout.item_task_biz) {
            @Override
            public BizTaskView initView(View convertView, BizTaskView holder) {
                if (holder == null) holder = new BizTaskView();
                holder.tv_pallet_id = convertView.findViewById(R.id.tv_pallet_id);
                holder.tv_fgtf = convertView.findViewById(R.id.tv_fgtf);
                holder.tv_binid = convertView.findViewById(R.id.tv_binid);
                holder.tv_box_count = convertView.findViewById(R.id.tv_box_count);
                holder.tv_terminal_in = convertView.findViewById(R.id.tv_terminal_in);
                return holder;
            }

            @Override
            public void initContent(BizTaskView holder, final BillData data) {
                holder.tv_pallet_id.setText("RF: "+data.getBillNo());
                holder.tv_fgtf.setText("WC: "+data.getWorkCell());
                holder.tv_binid.setText("BinID: "+data.getFromStation());
                holder.tv_box_count.setVisibility(View.GONE);
                holder.tv_terminal_in.setVisibility(View.GONE);
            }
        };
        lv_biz_task.setOnItemClickListener((parent, view, position, id) -> {
//            if (adapter.getItem(position).getStatus() == 3) {
//                Intent intent = new Intent(StockOutActivity.this, StockOutScanActivity.class);
//                intent.putExtra("FUNCTION", function);
//                intent.putExtra("BizTaskId", (adapter.getItem(position)).getId());
//                startActivity(intent);
//                return;
//            }
            TTSUtil.speak("pallet not arrived");
            ToastUtil.show(StockOutActivity.this, "pallet not arrived");
        });
        lv_biz_task.setOnItemLongClickListener((parent, view, position, id) -> {
            return false;
        });
        lv_biz_task.setAdapter(adapter);
    }

    private void getBizTask() {
        String url = App.getMethod("/retrive/retrieveList?function=" + function);
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        CallServer.getInstance().add(0, request, new HttpResponse(StockOutActivity.this) {
            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"getBizTask onOK:"+json.toString());
                bizTaskList.clear();
                JSONArray arrays = json.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    if(!jsonObject.getString("status").equals("1")){
                        bizTaskList.add(BillData.parse(jsonObject));
                    }else{
                        Log.d(TAG,"had retrieve billNo");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(JSONObject object) {
                TTSUtil.speak("task get failed");
                Log.d(TAG,"getBizTask onFail:"+object.toString());
            }

            @Override
            public void onError() {
                TTSUtil.speak("task get error");
            }
        });
    }

    private class JumpTextWatcher implements TextWatcher {
        private EditText editText;

        public JumpTextWatcher(EditText editText) {
            this.editText = editText;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            Log.d(TAG,"str:"+str);
            if (str.equals("") || str == null || (str.indexOf("\r") == -1 && str.indexOf("\n") == -1)) return;
            editText.setText(str.replace("\r", "").replace("\n", ""));
            Log.d(TAG,"afterTextChanged:"+str);
            for (BillData billData:bizTaskList){
                Log.d(TAG,"getBillNo:"+billData.getBillNo());
                if(billData.getBillNo().equals(editText.getText().toString())){
                    Log.d(TAG,"start StockOutScanActivity");
                    et_perference.setText("");
                    Intent intent = new Intent(StockOutActivity.this, StockOutScanActivity.class);
                    intent.putExtra("FUNCTION", function);
                    intent.putExtra("BizTaskId", billData.getBillNo());
                    startActivity(intent);
                    break;
                }
            }

        }
    }
}