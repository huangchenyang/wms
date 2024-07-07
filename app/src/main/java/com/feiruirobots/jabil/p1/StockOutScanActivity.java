package com.feiruirobots.jabil.p1;

import static com.feiruirobots.jabil.p1.StockOutActivity.allList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.adapter.MyListAdapter;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.common.ToastUtil;
import com.feiruirobots.jabil.p1.http.CallServer;
import com.feiruirobots.jabil.p1.http.HttpResponse;
import com.feiruirobots.jabil.p1.model.BillData;
import com.feiruirobots.jabil.p1.model.Carton;
import com.feiruirobots.jabil.p1.model.FUNCTION;
import com.feiruirobots.jabil.p1.ui.ExtendedEditText;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

public class StockOutScanActivity extends BaseActivity {
    private String function;
    private String bizTaskId;
    @BindView(R.id.et_scan_text)
    ExtendedEditText et_scan_text;
    @BindView(R.id.et_back_terminal)
    ExtendedEditText et_back_terminal;
//    @BindView(R.id.lv_scan_out)
//    ListView lv_scan_out;
    @BindView(R.id.lv_all)
    ListView lv_all;
//    private MyListAdapter<CartonView, Carton> outAdapter;
    private MyListAdapter<CartonView, BillData> allAdapter;
//    private List<Carton> outList = new ArrayList<>();
//    private List<BillData> allList = new ArrayList<>();
    private String TAG="hcy--StockOutScanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out_scan);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        function = intent.getStringExtra("FUNCTION");
        bizTaskId = intent.getStringExtra("BizTaskId");
        et_scan_text.addTextChangedListener(new JumpTextWatcher(et_scan_text, null));
        et_back_terminal.addTextChangedListener(new JumpTextWatcher(et_back_terminal, null));
        et_scan_text.setVisibility(View.VISIBLE);
        initTitle("Retrieve->"+Objects.requireNonNull(FUNCTION.of(function)).msg+"->Retrieve BoxID");
//        getOutScan(null);
//        ScanOutListView();
        AllListView();
//        getAll();
        if(StrUtil.equals(function, FUNCTION.RTV_RTC.value) || StrUtil.equals(function, FUNCTION.STAGING.value)){
            et_scan_text.setHint("ESR");
        }else if(StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)){
            initTitle("Retrieve->"+Objects.requireNonNull(FUNCTION.of(function)).msg+"->Retrieve GRN");
            et_scan_text.setHint("GRN");
        }
    }

    class CartonView {
        TextView tv_t1, tv_t2, tv_t3, tv_qty, tv_finish;
    }

    /*
    private void ScanOutListView() {
        if (outAdapter == null) {
            outAdapter = new MyListAdapter<CartonView, Carton>(StockOutScanActivity.this, outList, R.layout.item_carton) {
                @Override
                public CartonView initView(View convertView, CartonView holder) {
                    if (holder == null) holder = new CartonView();
                    holder.tv_t3 = convertView.findViewById(R.id.tv_t3);
                    holder.tv_t2 = convertView.findViewById(R.id.tv_t2);
                    holder.tv_t1 = convertView.findViewById(R.id.tv_t1);
                    holder.tv_qty = convertView.findViewById(R.id.tv_qty);
                    holder.tv_finish = convertView.findViewById(R.id.tv_finish);
                    if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.VISIBLE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
                        holder.tv_qty.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.GONE);
                        holder.tv_qty.setVisibility(View.GONE);
                    }
                    return holder;
                }

                @Override
                public void initContent(CartonView holder, Carton data) {
                    if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
                        holder.tv_t1.setText("BoxID:" + data.getBoxId());
                        holder.tv_t2.setText("PN:" + data.getPartNo());
                        holder.tv_qty.setText("Q:" + data.getQty());
                        if (data.getStatus() == 0)
                            holder.tv_finish.setVisibility(View.GONE);
                        else
                            holder.tv_finish.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
                        holder.tv_t1.setText(String.valueOf(data.getReferenceId()));
                        holder.tv_qty.setText(String.valueOf(data.getQty()));
                        if (data.getStatus() == 0)
                            holder.tv_finish.setVisibility(View.GONE);
                        else
                            holder.tv_finish.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
                        holder.tv_t1.setText("ESR:" + data.getEsr());
                        holder.tv_qty.setText("Q:" + data.getQty());
                        if (data.getStatus() == 0)
                            holder.tv_finish.setVisibility(View.GONE);
                        else
                            holder.tv_finish.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
                        holder.tv_t1.setText("ESR:" + data.getEsr());
                        holder.tv_qty.setText("Q:" + data.getQty());
                        if (data.getStatus() == 0)
                            holder.tv_finish.setVisibility(View.GONE);
                        else
                            holder.tv_finish.setVisibility(View.VISIBLE);
                    }
                }
            };
            lv_scan_out.setAdapter(outAdapter);
        }
        outAdapter.setDataList(outList);
        outAdapter.notifyDataSetChanged();
    }
     */

    private void AllListView() {
        if (allAdapter == null) {
            allAdapter = new MyListAdapter<CartonView, BillData>(StockOutScanActivity.this, allList, R.layout.item_carton) {
                @Override
                public CartonView initView(View convertView, CartonView holder) {
                    if (holder == null) holder = new CartonView();
                    holder.tv_t3 = convertView.findViewById(R.id.tv_t3);
                    holder.tv_t2 = convertView.findViewById(R.id.tv_t2);
                    holder.tv_t1 = convertView.findViewById(R.id.tv_t1);
                    holder.tv_qty = convertView.findViewById(R.id.tv_qty);
                    holder.tv_qty.setTextColor(Color.BLACK);
                    holder.tv_qty.setTextSize(14);
                    holder.tv_finish = convertView.findViewById(R.id.tv_finish);
                    if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.VISIBLE);
                        holder.tv_t3.setVisibility(View.VISIBLE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                        holder.tv_finish.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.VISIBLE);
                        holder.tv_t3.setVisibility(View.VISIBLE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                        holder.tv_finish.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.VISIBLE);
                        holder.tv_t3.setVisibility(View.VISIBLE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                        holder.tv_finish.setVisibility(View.VISIBLE);
                    }
                    return holder;
                }

                @Override
                public void initContent(CartonView holder, BillData data) {
                    if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
                        holder.tv_t1.setText("RF: " + data.getBillNo());
                        holder.tv_t2.setText("BinID: " + data.getFromStation());
                        holder.tv_t3.setText("BoxID: " + data.getBoxId());
                        if(data.getStatus().equals("0")){
                            holder.tv_finish.setText("");
                        }else if(data.getStatus().equals("1")){
                            holder.tv_finish.setText("✔");
                        }else if(data.getStatus().equals("6")){
                            holder.tv_finish.setText("✔✔");
                        }
                        holder.tv_qty.setText("TIN: " + data.getTerminal());
                    }else if(StrUtil.equals(function, FUNCTION.RTV_RTC.value) || StrUtil.equals(function, FUNCTION.STAGING.value)){
                        holder.tv_t1.setText("RF: " + data.getBillNo());
                        holder.tv_t2.setText("BinID: " + data.getFromStation());
                        holder.tv_t3.setText("ESR: " + data.getEsr());
                        if(data.getStatus().equals("0")){
                            holder.tv_finish.setText("");
                        }else if(data.getStatus().equals("1")){
                            holder.tv_finish.setText("✔");
                        }else if(data.getStatus().equals("6")){
                            holder.tv_finish.setText("✔✔");
                        }
                        holder.tv_qty.setText("TIN: " + data.getTerminal());
                    }
                }
            };
            lv_all.setAdapter(allAdapter);
        }
        allAdapter.setDataList(allList);
        allAdapter.notifyDataSetChanged();
    }

    private void getAll() {
        StringRequest request = new StringRequest(App.getMethod("/retrive/retrieveBoxList"), RequestMethod.POST);
        request.add("billId", bizTaskId);
        CallServer.getInstance().add(0, request, new HttpResponse(StockOutScanActivity.this) {
            @Override
            public void onOK(JSONObject object) {
                allList.clear();
                Log.d(TAG,"getAll onOK:"+object.toString());
                JSONArray arrays = object.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    BillData billData = BillData.parse(jsonObject);
                    allList.add(billData);
                }
                AllListView();
            }

            @Override
            public void onFail(JSONObject object) {
                TTSUtil.speak("fail");
                ToastUtil.show(StockOutScanActivity.this,"retrieve box list fail "+object.toString());
            }

            @Override
            public void onError() {
                TTSUtil.speak("error");
                ToastUtil.show(StockOutScanActivity.this,"retrieve box list error");

            }
        });
    }

    private void scanBox(String boxId) {
        StringRequest request = new StringRequest(App.getMethod("/retrive/scanBox"), RequestMethod.POST);
        request.add("billId", bizTaskId);
        if(StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)){
            request.add("boxId", boxId);
        }else if(StrUtil.equals(function, FUNCTION.RTV_RTC.value) || StrUtil.equals(function, FUNCTION.STAGING.value)){
            request.add("esr", boxId);
        }else if(StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)){
            request.add("grn", boxId);
        }

        request.add("function", function);
        CallServer.getInstance().add(0, request, new HttpResponse(StockOutScanActivity.this) {
            @Override
            public void onOK(JSONObject object) {
                boolean isFinish =true;
                allList.clear();
                Log.d(TAG,"scanBox onOK:"+object.toString());
                JSONArray arrays = object.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    BillData billData = BillData.parse(jsonObject);
                    allList.add(billData);
                    if(billData.getStatus().equals("0") || billData.getStatus().equals("1")){
                        isFinish = false;
                    }
                }
                AllListView();
                if(isFinish){
                    showConfirmationDialog();
                }
            }

            @Override
            public void onFail(JSONObject object) {
                Log.d(TAG,"scan box fail:"+object.toString());
                TTSUtil.speak("fail");
                ToastUtil.show(StockOutScanActivity.this,"scan box fail "+object.toString());
            }

            @Override
            public void onError() {
                TTSUtil.speak("error");
                ToastUtil.show(StockOutScanActivity.this,"scan box error");
            }
        });
    }

    /*
    private void getOutScan(String code) {
        StringRequest request = new StringRequest(App.getMethod("/retrive/outScan"), RequestMethod.POST);
        request.add("bizTaskId", bizTaskId);
        request.add("code", code);
        CallServer.getInstance().add(0, request, new HttpResponse(StockOutScanActivity.this) {
            @Override
            public void onOK(JSONObject object) {
                outList.clear();
                et_scan_text.setText(null);
                Integer remainScanQty = object.getInteger("remain_scan_qty");//等待扫描的数量
                Integer remainQty = object.getInteger("remain_qty");//托盘剩余的箱子数量
                JSONArray arrays = object.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    Carton carton = Carton.parse(jsonObject);
                    outList.add(carton);
                }
                ScanOutListView();
                getAll();
                if (remainQty == 0) {
                    TTSUtil.speak("Out Finish");
                    ToastUtil.show(StockOutScanActivity.this, "Out Finish");
                    et_scan_text.setVisibility(View.GONE);
                    return;
                }
                if (remainScanQty == 0) {
                    TTSUtil.speak("Pick Finish");
                    ToastUtil.show(StockOutScanActivity.this, "Pick Finish,Please Back");
                    et_scan_text.setVisibility(View.GONE);
                    et_back_terminal.setVisibility(View.VISIBLE);
                    et_back_terminal.requestFocus();
                    return;
                }
                et_scan_text.setVisibility(View.VISIBLE);
                TTSUtil.speak("OK");
            }

            @Override
            public void onFail(JSONObject object) {
                TTSUtil.speak("Error");
                outList.clear();
                et_scan_text.setText(null);
                JSONArray arrays = object.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    Carton carton = Carton.parse(jsonObject);
                    outList.add(carton);
                }
                ScanOutListView();
                getAll();
            }

            @Override
            public void onError() {

            }
        });
    }
     */

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
            if (str.equals("") || str == null || (str.indexOf("\r") == -1 && str.indexOf("\n") == -1)) return;
            String str2 = str.replace("\r", "").replace("\n", "");
            if(StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)){
                if (!StrUtil.startWithAny(str2, "PA134", "PV19", "PAG1", "PAS1", "PA", "PABD", "PA95", "PA124", "PA112", "PA193", "PA140", "PCT8", "PA104", "PFGT", "BL19") &&
                        !StrUtil.startWithAny(str, "RA134", "RV19", "RAG1", "RAS1", "RA", "RA78", "RA95", "RA124", "RA112", "RA193", "RA140", "PA102", "RA104", "RA11", "RL19")) {
                    TTSUtil.speak("error");
                    et_scan_text.setText(null);
                    et_scan_text.requestFocus();
                    Toast.makeText(StockOutScanActivity.this, "invalid BoxID", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            editText.setText(null);
            scanBox(str2);

//            if (editText.getId() == R.id.et_scan_text) {
//                if (str2.length() > 5) {
//                    editText.setText(str2);
////                    getOutScan(str2);
//                    et_scan_text.setText(null);
//                    return;
//                }
//                editText.setText(null);
//                ToastUtil.show(StockOutScanActivity.this, "BoxId Input Error");
//            }
//            if (editText.getId() == R.id.et_back_terminal) {
//                if (str2.startsWith("Terminal")) {
//                    editText.setText(str2);
//                    back();
//                    return;
//                }
//                editText.setText(null);
//                ToastUtil.show(StockOutScanActivity.this, "Terminal Input Error");
//            }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(StockOutScanActivity.this);
        dialog.setTitle("Alert");//设置对话框的标题
        dialog.setMessage("confirm back!");//设置对话框的内容
        dialog.setCancelable(false);//设置对话框是否可以取消
        //确定按钮的点击事件
        dialog.setPositiveButton("OK", (dialog12, which) -> StockOutScanActivity.this.finish());
        //取消按钮的点击事件
        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
        });
        dialog.show();
    }

    public void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Reference "+bizTaskId+" Retrieve BoxlD have been all scanned");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}