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
import android.widget.RadioGroup;
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
import com.feiruirobots.jabil.p1.model.CycleJson;
import com.feiruirobots.jabil.p1.model.FUNCTION;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

public class ConsolidationActivity extends BaseActivity {
    private String function;
    private MyListAdapter adapter;
    private List<CycleJson> consolidationJsons = new ArrayList<>();

    private String TAG = "hcy--ConsolidationActivity";

    @BindView(R.id.lv_biz_task)
    ListView lv_biz_task;
    @BindView(R.id.lv_con_in_out_biz_task)
    ListView lv_con_in_out_biz_task;
    @BindView(R.id.et_Consolidation_id)
    EditText et_Consolidation_id;
    @BindView(R.id.et_consolidation_in_terminal)
    EditText et_consolidation_in_terminal;
    @BindView(R.id.btn_submit_consolidation)
    Button btn_submit_consolidation;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    private TextView tv_inventory_comment;
    private TextView tv_pallet_list_comment;

    private Handler handlerSend = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 重写handleMessage方法
            super.handleMessage(msg);
            if (msg.what == 1) {
                if(StrUtil.equals(function, FUNCTION.CYCLE_OUT.value) ){
                    getCycleOutList();
                }else if(StrUtil.equals(function, FUNCTION.CYCLE_IN.value)){
                    getCycleInList();
                }
                handlerSend.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidation);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        function = intent.getStringExtra("FUNCTION");
        initTitle("Inventory->"+ Objects.requireNonNull(FUNCTION.of(function)).msg+ " pallet");
        initListView();
        if(StrUtil.equals(function, FUNCTION.CONSOLIDATION_OUT.value) ){
            //getCycleOutList();
        }else if(StrUtil.equals(function, FUNCTION.CONSOLIDATION_IN.value)){
            //getCycleInList();
        }
        //handlerSend.sendEmptyMessageDelayed(1, 1000);
        initView();
    }

    private void initView(){
        tv_inventory_comment = findViewById(R.id.tv_inventory_comment);
        tv_pallet_list_comment = findViewById(R.id.tv_pallet_list_comment);
        if(StrUtil.equals(function, FUNCTION.CONSOLIDATION_OUT.value) ){
            et_Consolidation_id.setVisibility(View.VISIBLE);
            btn_submit_consolidation.setText("Submit Consolidation Out");
            et_Consolidation_id.setHint("Consolidation Out BinID");
            radio_group.setVisibility(View.VISIBLE);
            tv_pallet_list_comment.setVisibility(View.GONE);
            lv_con_in_out_biz_task.setVisibility(View.GONE);
            tv_inventory_comment.setText("Consolidation out order pallet list:");
            et_Consolidation_id.addTextChangedListener(new ConsolidationActivity.JumpTextWatcher(et_Consolidation_id,null));
        }else if(StrUtil.equals(function, FUNCTION.CONSOLIDATION_IN.value)){
            et_Consolidation_id.setVisibility(View.VISIBLE);
            et_consolidation_in_terminal.setVisibility(View.VISIBLE);
            btn_submit_consolidation.setText("Submit Consolidation In");
            et_Consolidation_id.setHint("BoxID");
            et_consolidation_in_terminal.setHint("Inventory Terminal In Platform");
            radio_group.setVisibility(View.GONE);
            tv_inventory_comment.setText("Con out All order for BoxID scanning list");
            tv_pallet_list_comment.setText("Con in/Con out Zero order pallet list");
            tv_pallet_list_comment.setVisibility(View.VISIBLE);
            lv_con_in_out_biz_task.setVisibility(View.VISIBLE);
            et_Consolidation_id.addTextChangedListener(new ConsolidationActivity.JumpTextWatcher(et_Consolidation_id, et_consolidation_in_terminal));
            et_consolidation_in_terminal.addTextChangedListener(new ConsolidationActivity.JumpTextWatcher(et_consolidation_in_terminal,null));
        }
    }

    @OnClick({R.id.btn_submit_consolidation})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit_consolidation) {
            if(StrUtil.equals(function, FUNCTION.CONSOLIDATION_OUT.value) ){
                String binId = et_Consolidation_id.getText().toString();
                if(binId==null || binId.equals("")){
                    Toast.makeText(ConsolidationActivity.this, "BinId is null", Toast.LENGTH_SHORT).show();
                }else{
                    //setCycleOut(binId);
                }
            }else if(StrUtil.equals(function, FUNCTION.CONSOLIDATION_IN.value)){
                String binId = et_Consolidation_id.getText().toString();
                String terminal = et_consolidation_in_terminal.getText().toString();
                if(binId==null || binId.equals("") || terminal==null || binId.equals("")){
                    Toast.makeText(ConsolidationActivity.this, "BinId is null or terminal is null", Toast.LENGTH_SHORT).show();
                }else{
//                    setCycleIn(binId,terminal);
                }
            }
        }
    }


    class BizTaskView {
        TextView tv_pallet_id, tv_fgtf, tv_binid, tv_box_count,tv_terminal_in;
    }

    private void initListView() {
        adapter = new MyListAdapter<BizTaskView, CycleJson>(ConsolidationActivity.this, consolidationJsons, R.layout.item_task_biz) {
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
            public void initContent(BizTaskView holder, final CycleJson data) {
                holder.tv_fgtf.setVisibility(View.VISIBLE);
                holder.tv_pallet_id.setVisibility(View.VISIBLE);
                holder.tv_binid.setVisibility(View.VISIBLE);
                holder.tv_box_count.setVisibility(View.GONE);
                holder.tv_terminal_in.setVisibility(View.GONE);
                holder.tv_pallet_id.setText("BinID: "+String.valueOf(data.getBinId()));
                holder.tv_fgtf.setText("TIN: "+data.getTerminal());
                holder.tv_binid.setText("Status: "+data.getPalletStatus());
            }
        };
        lv_biz_task.setOnItemClickListener((parent, view, position, id) -> {

        });
        lv_biz_task.setOnItemLongClickListener((parent, view, position, id) -> {
            return false;
        });
        lv_biz_task.setAdapter(adapter);
    }

    private void getCycleOutList() {
        String url = App.getMethod("/inventory/cycle_out_list");
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        CallServer.getInstance().add(0, request, new HttpResponse(ConsolidationActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"onOK:"+json.toString());
                consolidationJsons.clear();
                JSONArray arrays = json.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    CycleJson cycleJson =CycleJson.parse(jsonObject);
                    consolidationJsons.add(cycleJson);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"onFail:"+object.toString());
                TTSUtil.speak("fail");
                ToastUtil.show(ConsolidationActivity.this,"list get fail "+object.toString());
            }

            @Override
            public void onError(String error) {
                TTSUtil.speak("error");
                ToastUtil.show(ConsolidationActivity.this,"list get error"+error);
            }
        });
    }

    private void setCycleOut(String binId) {
        String url = App.getMethod("/inventory/cycle_out");
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        request.add("bin_id",binId);
        CallServer.getInstance().add(0, request, new HttpResponse(ConsolidationActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"onOK:"+json.toString());
                ToastUtil.show(ConsolidationActivity.this,"cycle out ok");
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"onFail:"+object.toString());
                TTSUtil.speak("fail");
                ToastUtil.show(ConsolidationActivity.this,"cycle out fail "+object.toString());
            }

            @Override
            public void onError(String error) {
                TTSUtil.speak("error");
                ToastUtil.show(ConsolidationActivity.this,"cycle out error"+error);
            }
        });
    }

    private void getCycleInList() {
        String url = App.getMethod("/inventory/cycle_in_list");
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        CallServer.getInstance().add(0, request, new HttpResponse(ConsolidationActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"onOK:"+json.toString());
                consolidationJsons.clear();
                JSONArray arrays = json.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    CycleJson cycleJson =CycleJson.parse(jsonObject);
                    consolidationJsons.add(cycleJson);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"onFail:"+object.toString());
                TTSUtil.speak("fail");
                ToastUtil.show(ConsolidationActivity.this,"list get fail "+object.toString());
            }

            @Override
            public void onError(String error) {
                TTSUtil.speak("error");
                ToastUtil.show(ConsolidationActivity.this,"list get error:"+error);
            }
        });
    }

    private void setCycleIn(String boxId,String terminal) {
        String url = App.getMethod("/inventory/cycle_in");
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        request.add("box_id",boxId);
        request.add("terminal_id",terminal);
        CallServer.getInstance().add(0, request, new HttpResponse(ConsolidationActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                Log.d(TAG,"onOK:"+json.toString());
                ToastUtil.show(ConsolidationActivity.this,"cycle in ok");
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"onFail:"+object.toString());
                TTSUtil.speak("fail");
                ToastUtil.show(ConsolidationActivity.this,"cycle in fail "+object.toString());
            }

            @Override
            public void onError(String error) {
                TTSUtil.speak("error");
                ToastUtil.show(ConsolidationActivity.this,"cycle in error:"+error);
            }
        });
    }

    @Override
    protected void onResume() {
        if(StrUtil.equals(function, FUNCTION.CYCLE_OUT.value) ){
            getCycleOutList();
        }else if(StrUtil.equals(function, FUNCTION.CYCLE_IN.value)){
            getCycleInList();
        }
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
            if (str == null || str.equals("")|| (str.indexOf("\r") == -1 && str.indexOf("\n") == -1)) return;
            editText.setText(str.replace("\r", "").replace("\n", "").trim());

            if (editText.getId() == et_Consolidation_id.getId()) {
                if (StrUtil.equals(function, FUNCTION.CYCLE_OUT.value)) {

                }else if (StrUtil.equals(function, FUNCTION.CYCLE_IN.value)) {
                    if (!StrUtil.startWithAny(str, "PA134", "PV19", "PAG1", "PAS1", "PA", "PABD", "PA95", "PA124", "PA112", "PA193", "PA140", "PCT8", "PA104", "PFGT", "BL19") &&
                            !StrUtil.startWithAny(str, "RA134", "RV19", "RAG1", "RAS1", "RA", "RA78", "RA95", "RA124", "RA112", "RA193", "RA140", "PA102", "RA104", "RA11", "RL19")) {
                        TTSUtil.speak("error");
                        et_Consolidation_id.setText(null);
                        et_Consolidation_id.requestFocus();
                        Toast.makeText(ConsolidationActivity.this, "invalid BoxID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            if (editText.getId() == et_consolidation_in_terminal.getId()) {

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
        }
    }

}