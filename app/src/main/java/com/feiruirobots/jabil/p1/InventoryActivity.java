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
import android.widget.Button;
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
import com.feiruirobots.jabil.p1.model.BizTask;
import com.feiruirobots.jabil.p1.model.Carton;
import com.feiruirobots.jabil.p1.model.FUNCTION;
import com.feiruirobots.jabil.p1.model.ListJson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

public class InventoryActivity extends BaseActivity {
    private String function;
    private MyListAdapter adapter;
    private List<BizTask> bizTaskList = new ArrayList<>();
    private List<ListJson> listJsons = new ArrayList<>();
    public static BizTask existPallet= new BizTask();
    public static List<Carton> existCartonList = new ArrayList<>();

    private String TAG = "hcy--InventoryActivity";

    @BindView(R.id.lv_biz_task)
    ListView lv_biz_task;
    @BindView(R.id.et_cycle_id)
    EditText et_cycle_id;
    @BindView(R.id.et_cycle_in_terminal)
    EditText et_cycle_in_terminal;
    @BindView(R.id.btn_submit_cycle)
    Button btn_submit_cycle;

    private Handler handlerSend = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 重写handleMessage方法
            super.handleMessage(msg);
            if (msg.what == 1) {
                getListTask();
                handlerSend.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        function = intent.getStringExtra("FUNCTION");
        initTitle("Inventory->"+ Objects.requireNonNull(FUNCTION.of(function)).msg+ " pallet");
        initListView();
        //getListTask();
        handlerSend.sendEmptyMessageDelayed(1, 1000);
        initView();
    }

    private void initView(){
        if(StrUtil.equals(function, FUNCTION.CYCLE_OUT.value) ){
            et_cycle_id.setVisibility(View.VISIBLE);
            btn_submit_cycle.setText("Submit Cycle Out");
            et_cycle_id.setHint("Cycle out BinID");
            et_cycle_id.addTextChangedListener(new InventoryActivity.JumpTextWatcher(et_cycle_id,null));
        }else if(StrUtil.equals(function, FUNCTION.CYCLE_IN.value)){
            et_cycle_id.setVisibility(View.VISIBLE);
            et_cycle_in_terminal.setVisibility(View.VISIBLE);
            btn_submit_cycle.setText("Submit Cycle In");
            et_cycle_id.setHint("BoxID");
            et_cycle_id.addTextChangedListener(new InventoryActivity.JumpTextWatcher(et_cycle_id, et_cycle_in_terminal));
            et_cycle_in_terminal.addTextChangedListener(new InventoryActivity.JumpTextWatcher(et_cycle_in_terminal,null));
        }
    }

    @OnClick({R.id.btn_submit_cycle})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit_cycle) {
//            Intent intent = new Intent(InventoryActivity.this, StockInAddActivity.class);
//            intent.putExtra("FUNCTION", function);
//            intent.putExtra("AddType","New Packet");
//            startActivity(intent);
        }

    }

    private void startActivityExist(){
        Intent intent = new Intent(InventoryActivity.this, StockInAddActivity.class);
        intent.putExtra("FUNCTION", function);
        intent.putExtra("AddType","Exist Packet");
        startActivity(intent);
    }

    class BizTaskView {
        TextView tv_pallet_id, tv_fgtf, tv_binid, tv_box_count,tv_terminal_in;
    }

    private void initListView() {
        adapter = new MyListAdapter<BizTaskView, ListJson>(InventoryActivity.this, listJsons, R.layout.item_task_biz) {
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
            public void initContent(BizTaskView holder, final ListJson data) {
                if(StrUtil.equals(function, FUNCTION.CYCLE_OUT.value)){
                    holder.tv_pallet_id.setText("BinID: "+String.valueOf(data.getId()));
                }else{
                    holder.tv_pallet_id.setText("BoxID: "+String.valueOf(data.getId()));
                }
                holder.tv_fgtf.setVisibility(View.GONE);
                holder.tv_binid.setText("Status: "+"");
                holder.tv_box_count.setVisibility(View.GONE);
                holder.tv_terminal_in.setVisibility(View.GONE);
            }
        };
        lv_biz_task.setOnItemClickListener((parent, view, position, id) -> {

        });
        lv_biz_task.setOnItemLongClickListener((parent, view, position, id) -> {
            return false;
        });
        lv_biz_task.setAdapter(adapter);
    }

    private void getListTask() {
        String url = App.getMethod("/stockIn/list?function=" + function);
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        CallServer.getInstance().add(0, request, new HttpResponse(InventoryActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"onOK:"+json.toString());
                listJsons.clear();
                JSONArray arrays = json.getJSONArray("data");
                for (Object array : arrays) {
//                    Log.d(TAG,"array:"+array.toString());
                    JSONObject jsonObject = (JSONObject) array;
                    ListJson listJson =ListJson.parse(jsonObject);
                    listJsons.add(listJson);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"onFail:"+object.toString());
                TTSUtil.speak("fail");
                ToastUtil.show(InventoryActivity.this,"list get fail "+object.toString());
            }

            @Override
            public void onError() {
                TTSUtil.speak("error");
                ToastUtil.show(InventoryActivity.this,"list get error");
            }
        });
    }

    @Override
    protected void onResume() {
        getListTask();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁之前进行清理工作
        handlerSend.removeCallbacksAndMessages(null);
    }

    private class JumpTextWatcher implements TextWatcher {
        private EditText editText;
        private View nextView;

        public JumpTextWatcher(EditText editText, View nextView) {
            this.editText = editText;
            if (null != nextView) {
                this.nextView = nextView;
            }
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
            if (str == null || (str.indexOf("\r") == -1 && str.indexOf("\n") == -1)) return;
            editText.setText(str.replace("\r", "").replace("\n", ""));


            if (editText.getId() == et_cycle_id.getId()) {
                if (StrUtil.equals(function, FUNCTION.CYCLE_OUT.value)) {

                }else if (StrUtil.equals(function, FUNCTION.CYCLE_IN.value)) {
                    if (!StrUtil.startWithAny(str, "PA134", "PV19", "PAG1", "PAS1", "PA", "PABD", "PA95", "PA124", "PA112", "PA193", "PA140", "PCT8", "PA104", "PFGT", "BL19") &&
                            !StrUtil.startWithAny(str, "RA134", "RV19", "RAG1", "RAS1", "RA", "RA78", "RA95", "RA124", "RA112", "RA193", "RA140", "PA102", "RA104", "RA11", "RL19")) {
                        TTSUtil.speak("error");
                        et_cycle_id.setText(null);
                        et_cycle_id.requestFocus();
                        Toast.makeText(InventoryActivity.this, "invalid BoxID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            if (editText.getId() == et_cycle_in_terminal.getId()) {

            }

            if (nextView == null) {
                return;
            }
            nextView.requestFocus();
            //如果想下一个节点是EditText
            if (nextView instanceof EditText) {
                EditText et = (EditText) nextView;
                //光标移动到标记框内部的末尾
                et.setSelection(et.getText().length());
            }
            if (nextView.getId() == R.id.btn_add_box) {
//                boxAdd();
            }
//            if (nextView.getId() == R.id.btn_add_pallet) {
//                palletAdd();
//            }
        }
    }

}