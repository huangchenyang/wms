package com.feiruirobots.jabil.p1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.adapter.MyListAdapter;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.http.CallServer;
import com.feiruirobots.jabil.p1.http.HttpResponse;
import com.feiruirobots.jabil.p1.model.BIZ_TASK_STATUS;
import com.feiruirobots.jabil.p1.model.BizTask;
import com.feiruirobots.jabil.p1.model.Carton;
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
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;

public class StockInActivity extends BaseActivity {
    private String function;
    private MyListAdapter adapter;
    private List<BizTask> bizTaskList = new ArrayList<>();
    public static BizTask existPallet= new BizTask();
    public static List<Carton> existCartonList = new ArrayList<>();

    private String TAG = "hcy--StockInActivity";

    @BindView(R.id.lv_biz_task)
    ListView lv_biz_task;
    @BindView(R.id.et_box_id)
    EditText et_box_id;

    private Handler handlerSend = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 重写handleMessage方法
            super.handleMessage(msg);
            if (msg.what == 1) {
                getBizTask();
                handlerSend.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        function = intent.getStringExtra("FUNCTION");
        initTitle("Stock In->"+ Objects.requireNonNull(FUNCTION.of(function)).msg);
        initListView();
        getBizTask();
        handlerSend.sendEmptyMessageDelayed(1, 1000);
        et_box_id.addTextChangedListener(new StockInActivity.JumpTextWatcher(et_box_id));
    }

    @OnClick({R.id.btn_new_pallet, R.id.btn_out_exist_pallet})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_pallet) {
            Intent intent = new Intent(StockInActivity.this, StockInAddActivity.class);
            intent.putExtra("FUNCTION", function);
            intent.putExtra("AddType","New Packet");
            startActivity(intent);
        }
        if (view.getId() == R.id.btn_out_exist_pallet) {

        }
    }

    private void startActivityExist(){
        Intent intent = new Intent(StockInActivity.this, StockInAddActivity.class);
        intent.putExtra("FUNCTION", function);
        intent.putExtra("AddType","Exist Packet");
        startActivity(intent);
    }

    class BizTaskView {
        TextView tv_pallet_id, tv_fgtf, tv_binid, tv_box_count,tv_terminal_in;
    }

    private void initListView() {
        adapter = new MyListAdapter<BizTaskView, BizTask>(StockInActivity.this, bizTaskList, R.layout.item_task_biz) {
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
            public void initContent(BizTaskView holder, final BizTask data) {
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append(data.getFromId());
//                if (StrUtil.isNotBlank(data.getBufferId())) {
//                    stringBuilder.append(" → ");
//                    stringBuilder.append(data.getBufferId());
//                }
//                if (StrUtil.isNotBlank(data.getDestId())) {
//                    stringBuilder.append(" →  ");
//                    stringBuilder.append(data.getDestId());
//                }
                holder.tv_pallet_id.setText(String.valueOf(data.getId()));
                holder.tv_fgtf.setText("");
                holder.tv_binid.setText(data.getBinId());
                holder.tv_box_count.setText(String.valueOf(data.getBoxCount()));
                holder.tv_terminal_in.setText("");
//                holder.tv_pallet_id.setText("Pallet ID:"+String.valueOf(data.getId()));
//                holder.tv_fgtf.setText("Pallet ID:");
//                holder.tv_binid.setText("BinID ID:"+data.getBinId());
//                holder.tv_box_count.setText("Box Count:"+String.valueOf(data.getBoxCount()));
//                holder.tv_terminal_in.setText("Terminal In ID:");
            }
        };
        lv_biz_task.setOnItemClickListener((parent, view, position, id) -> {

        });
        lv_biz_task.setOnItemLongClickListener((parent, view, position, id) -> {
            return false;
        });
        lv_biz_task.setAdapter(adapter);
    }

    private void getBizTask() {
        String url = App.getMethod("/stockIn/list?function=" + function);
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        CallServer.getInstance().add(0, request, new HttpResponse(StockInActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"onOK:"+json.toString());
                bizTaskList.clear();
                JSONArray arrays = json.getJSONArray("data");
                for (Object array : arrays) {
                    Log.d(TAG,"array:"+array.toString());
                    JSONObject jsonObject = (JSONObject) array;
                    BizTask bizTask =BizTask.parse(jsonObject);
                    bizTaskList.add(bizTask);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"onFail:"+object.toString());
                TTSUtil.speak("task get failed");
            }

            @Override
            public void onError() {
                TTSUtil.speak("task get error");
            }
        });
    }

    @Override
    protected void onResume() {
        getBizTask();
        super.onResume();
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
            if (str.equals("") || str == null || (str.indexOf("\r") == -1 && str.indexOf("\n") == -1)) return;
            editText.setText(str.replace("\r", "").replace("\n", ""));
            Log.d(TAG,"afterTextChanged:"+str);
            if (!StrUtil.startWithAny(str, "PA134", "PV19", "PAG1", "PAS1", "PA", "PABD", "PA95", "PA124", "PA112", "PA193", "PA140", "PCT8", "PA104", "PFGT", "BL19")) {
                TTSUtil.speak("error");
                et_box_id.setText(null);
                et_box_id.requestFocus();
                Toast.makeText(StockInActivity.this, "invalid BoxID", Toast.LENGTH_SHORT).show();
                return;
            }
            getPalletInfo(str);
        }
    }

    private void getPalletInfo(String boxid) {
        String url = App.getMethod("/stockIn/palletInfo?function=" + function);
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        CallServer.getInstance().add(0, request, new HttpResponse(StockInActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"palletInfo onOK:"+json.toString());
                et_box_id.setText(null);
                JSONObject dataObject = json.getJSONObject("data");
                JSONArray boxsArray = dataObject.getJSONArray("boxs");
                JSONObject palletObject = dataObject.getJSONObject("pallet");
                existPallet =BizTask.parse(palletObject);
                existCartonList.clear();

                for (Object o : boxsArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    Carton carton = Carton.parse(jsonObject);
                    existCartonList.add(carton);
                }
                startActivityExist();
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"palletInfo onFail:"+object.toString());
                TTSUtil.speak("task get failed");
                et_box_id.setText(null);
            }

            @Override
            public void onError() {
                TTSUtil.speak("task get error");
            }
        });
    }


}