package com.feiruirobots.jabil.p1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.adapter.MyListAdapter;
import com.feiruirobots.jabil.p1.bean.Kv;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.common.ToastUtil;
import com.feiruirobots.jabil.p1.http.CallServer;
import com.feiruirobots.jabil.p1.http.HttpResponse;
import com.feiruirobots.jabil.p1.model.BizTask;
import com.feiruirobots.jabil.p1.model.Carton;
import com.feiruirobots.jabil.p1.model.FUNCTION;
import com.feiruirobots.jabil.p1.ui.ExtendedEditText;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

public class StockInAddActivity extends BaseActivity {
    @BindView(R.id.et_box_id)
    ExtendedEditText et_box_id;
    @BindView(R.id.et_part_no)
    ExtendedEditText et_part_no;
    @BindView(R.id.et_qty)
    ExtendedEditText et_qty;
    @BindView(R.id.et_esr)
    ExtendedEditText et_esr;
    @BindView(R.id.et_grn)
    ExtendedEditText et_grn;
    @BindView(R.id.sp_work_cell)
    Spinner sp_work_cell;
    @BindView(R.id.sp_forward)
    Spinner sp_forward;
    @BindView(R.id.et_reference_id)
    ExtendedEditText et_reference_id;
    @BindView(R.id.sp_type)
    Spinner sp_type;
    @BindView(R.id.lv_carton)
    ListView lv_carton;
    @BindView(R.id.btn_add_box)
    Button btn_add_box;
    @BindView(R.id.btn_add_pallet)
    Button btn_add_pallet;
    @BindView(R.id.et_terminal)
    ExtendedEditText et_terminal;
    @BindView(R.id.tv_box_count)
    TextView tv_box_count;
    @BindView(R.id.et_rma)
    ExtendedEditText et_rma;
    @BindView(R.id.et_carton_count)
    ExtendedEditText et_carton_count;
    @BindView(R.id.et_fgtf)
    ExtendedEditText et_fgtf;
    @BindView(R.id.cb_rma)
    CheckBox cb_rma;
    @BindView(R.id.cb_special_pallet)
    CheckBox cb_special_pallet;
    @BindView(R.id.cb_batch_no)
    CheckBox cb_batch_no;
    @BindView(R.id.et_batch_no)
    ExtendedEditText et_batch_no;
    private String function;
    private Integer palletId;
    private List<Carton> cartonList = new ArrayList<>();
    private MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in_add);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        function = intent.getStringExtra("FUNCTION");
        initTitle(Objects.requireNonNull(FUNCTION.of(function)).msg + " In Stock");
        initView();
        refreshListView();
        initAdapter();
        cb_rma.setOnCheckedChangeListener((buttonView, isChecked) -> {
            et_rma.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            initView();
        });
        cb_batch_no.setOnCheckedChangeListener((buttonView, isChecked) -> {
            et_batch_no.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            initView();
        });
    }

    private void initView() {
        et_terminal.addTextChangedListener(new JumpTextWatcher(et_terminal, btn_add_pallet));
        if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value)) {
            sp_work_cell.setVisibility(View.VISIBLE);
            cb_rma.setVisibility(View.VISIBLE);
            et_fgtf.setVisibility(View.VISIBLE);
            et_part_no.setVisibility(View.VISIBLE);
            et_qty.setVisibility(View.VISIBLE);
            et_box_id.setVisibility(View.VISIBLE);
            et_fgtf.addTextChangedListener(new JumpTextWatcher(et_fgtf, et_part_no));
            et_part_no.addTextChangedListener(new JumpTextWatcher(et_part_no, et_qty));
            et_qty.addTextChangedListener(new JumpTextWatcher(et_qty, et_box_id));
            if (et_rma.getVisibility() == View.VISIBLE) {
                et_box_id.addTextChangedListener(new JumpTextWatcher(et_box_id, et_rma));
                et_rma.addTextChangedListener(new JumpTextWatcher(et_rma, btn_add_box));
            } else {
                et_box_id.addTextChangedListener(new JumpTextWatcher(et_box_id, btn_add_box));
            }
        }
        if (StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
            sp_work_cell.setVisibility(View.VISIBLE);
            et_fgtf.setVisibility(View.VISIBLE);
            cb_rma.setVisibility(View.VISIBLE);
            et_part_no.setVisibility(View.VISIBLE);
            et_qty.setVisibility(View.VISIBLE);
            et_box_id.setVisibility(View.VISIBLE);
            et_fgtf.addTextChangedListener(new JumpTextWatcher(et_fgtf, et_part_no));
            et_part_no.addTextChangedListener(new JumpTextWatcher(et_part_no, et_qty));
            et_qty.addTextChangedListener(new JumpTextWatcher(et_qty, et_box_id));
            if (et_rma.getVisibility() == View.VISIBLE) {
                et_box_id.addTextChangedListener(new JumpTextWatcher(et_box_id, et_rma));
                et_rma.addTextChangedListener(new JumpTextWatcher(et_rma, btn_add_box));
            } else {
                et_box_id.addTextChangedListener(new JumpTextWatcher(et_box_id, btn_add_box));
            }
        }
        if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
            et_reference_id.setVisibility(View.VISIBLE);
            et_part_no.setVisibility(View.GONE);
            et_grn.setVisibility(View.VISIBLE);
            et_qty.setVisibility(View.VISIBLE);
            et_reference_id.addTextChangedListener(new JumpTextWatcher(et_reference_id, et_qty));
            et_qty.addTextChangedListener(new JumpTextWatcher(et_qty, et_grn));
            if (et_batch_no.getVisibility() == View.VISIBLE) {
                et_grn.addTextChangedListener(new JumpTextWatcher(et_grn, et_batch_no));
                et_batch_no.addTextChangedListener(new JumpTextWatcher(et_batch_no, btn_add_box));
            } else {
                et_grn.addTextChangedListener(new JumpTextWatcher(et_grn, btn_add_box));
            }
        }
        if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
            sp_type.setVisibility(View.VISIBLE);
            et_esr.setVisibility(View.VISIBLE);
            et_carton_count.setVisibility(View.VISIBLE);
            et_esr.addTextChangedListener(new JumpTextWatcher(et_esr, et_carton_count));
            et_carton_count.addTextChangedListener(new JumpTextWatcher(et_carton_count, btn_add_box));
        }
        if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
            sp_type.setVisibility(View.VISIBLE);
            sp_forward.setVisibility(View.VISIBLE);
            et_esr.setVisibility(View.VISIBLE);
            et_carton_count.setVisibility(View.VISIBLE);
            et_esr.addTextChangedListener(new JumpTextWatcher(et_esr, et_carton_count));
            et_carton_count.addTextChangedListener(new JumpTextWatcher(et_carton_count, btn_add_box));
        }
    }

    private void initAdapter() {
        spTypeInit();
        spWorkCellInit();
        spForwardInit();
    }

    private void spTypeInit() {
        List<String> list = App.getInstance().getData(FUNCTION.valueOf(function), "type");
        if (list != null) {
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_type.setAdapter(typeAdapter);
            return;
        }
        List<String> types = new ArrayList<String>();
        types.add("Pallet");
        types.add("Cartion");
        App.getInstance().setData(FUNCTION.valueOf(function), "type", types);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(typeAdapter);
    }

    private void spForwardInit() {
        List<String> list = App.getInstance().getData(FUNCTION.valueOf(function), "forward");
        if (list != null) {
            ArrayAdapter<String> forwardAdapter = new ArrayAdapter<String>(StockInAddActivity.this, android.R.layout.simple_spinner_item, list);
            forwardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_forward.setAdapter(forwardAdapter);
            return;
        }
        StringRequest request = new StringRequest(App.getMethod("/stockIn/forward"), RequestMethod.POST);
        request.add("function", function);
        CallServer.getInstance().add(0, request, new OnResponseListener() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response response) {
                JSONObject json = JSONObject.parseObject(response.get().toString());
                String state = json.getString("state");
                if (!state.equals("ok")) return;
                List<String> forwards = StrUtil.split(json.getString("data"), ",");
                App.getInstance().setData(FUNCTION.valueOf(function), "forward", forwards);
                ArrayAdapter<String> forwardAdapter = new ArrayAdapter<String>(StockInAddActivity.this, android.R.layout.simple_spinner_item, forwards);
                forwardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_forward.setAdapter(forwardAdapter);
            }

            @Override
            public void onFailed(int what, Response response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void spWorkCellInit() {
        List<String> list = App.getInstance().getData(FUNCTION.valueOf(function), "workCell");
        if (list != null) {
            ArrayAdapter<String> workCellAdapter = new ArrayAdapter<String>(StockInAddActivity.this, android.R.layout.simple_spinner_item, list);
            workCellAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_work_cell.setAdapter(workCellAdapter);
            return;
        }
        StringRequest request = new StringRequest(App.getMethod("/stockIn/workCell"), RequestMethod.POST);
        request.add("function", function);
        CallServer.getInstance().add(0, request, new OnResponseListener() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response response) {
                JSONObject json = JSONObject.parseObject(response.get().toString());
                String state = json.getString("state");
                if (!state.equals("ok")) return;
                List<String> forwards = StrUtil.split(json.getString("data"), ",");
                App.getInstance().setData(FUNCTION.valueOf(function), "workCell", forwards);
                ArrayAdapter<String> workCellAdapter = new ArrayAdapter<String>(StockInAddActivity.this, android.R.layout.simple_spinner_item, forwards);
                workCellAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_work_cell.setAdapter(workCellAdapter);
            }

            @Override
            public void onFailed(int what, Response response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void palletAdd() {
        StringRequest request = null;
        if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value)) {
            String terminal = et_terminal.getText().toString();
            if (ObjectUtil.isAllNotEmpty(terminal, palletId)) {
                request = new StringRequest(App.getMethod("/stockIn/addFinishGoodsSubmit"), RequestMethod.POST);
                request.add("palletId", palletId);
                request.add("firstFloor", cb_special_pallet.isChecked() ? 1 : 0);
                request.set("terminal", et_terminal.getText().toString());
            }
        }
        if (StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
            String terminal = et_terminal.getText().toString();
            if (ObjectUtil.isAllNotEmpty(terminal, palletId)) {
                request = new StringRequest(App.getMethod("/stockIn/addSemiFGSubmit"), RequestMethod.POST);
                request.add("palletId", palletId);
                request.add("firstFloor", cb_special_pallet.isChecked() ? 1 : 0);
                request.set("terminal", et_terminal.getText().toString());
            }
        }
        if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
            String terminal = et_terminal.getText().toString();
            if (ObjectUtil.isAllNotEmpty(terminal, palletId)) {
                request = new StringRequest(App.getMethod("/stockIn/addRawMaterialSubmit"), RequestMethod.POST);
                request.add("palletId", palletId);
                request.add("firstFloor", cb_special_pallet.isChecked() ? 1 : 0);
                request.set("terminal", et_terminal.getText().toString());
            }
        }
        if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
            String terminal = et_terminal.getText().toString();
            if (ObjectUtil.isAllNotEmpty(terminal, palletId)) {
                request = new StringRequest(App.getMethod("/stockIn/addRtcSubmit"), RequestMethod.POST);
                request.add("palletId", palletId);
                request.add("firstFloor", cb_special_pallet.isChecked() ? 1 : 0);
                request.set("terminal", et_terminal.getText().toString());
            }
        }
        if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
            String terminal = et_terminal.getText().toString();
            if (ObjectUtil.isAllNotEmpty(terminal, palletId)) {
                request = new StringRequest(App.getMethod("/stockIn/addStagingSubmit"), RequestMethod.POST);
                request.add("palletId", palletId);
                request.add("firstFloor", cb_special_pallet.isChecked() ? 1 : 0);
                request.set("terminal", et_terminal.getText().toString());
            }
        }
        if (request == null) {
            ToastUtil.show(this, "Pallet ID is null,Check！");
            TTSUtil.speak("error");
            return;
        }
        CallServer.getInstance().add(0, request, new HttpResponse(StockInAddActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject object) {
                TTSUtil.speak("ok");
                StockInAddActivity.this.finish();
            }

            @Override
            public void onFail(JSONObject object) {

            }

            @Override
            public void onError() {
                TTSUtil.speak("error");
            }
        });
    }

    class CartonView {
        TextView tv_qty, tv_t1, tv_t2, tv_t3;
    }


    private void refreshListView() {
        if (adapter == null) {
            adapter = new MyListAdapter<CartonView, Carton>(StockInAddActivity.this, cartonList, R.layout.item_carton) {
                @Override
                public CartonView initView(View convertView, CartonView holder) {
                    if (holder == null) holder = new CartonView();
                    holder.tv_t1 = convertView.findViewById(R.id.tv_t1);
                    holder.tv_t2 = convertView.findViewById(R.id.tv_t2);
                    holder.tv_t3 = convertView.findViewById(R.id.tv_t3);
                    holder.tv_qty = convertView.findViewById(R.id.tv_qty);
                    if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.VISIBLE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.GONE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.GONE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                    }
                    if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
                        holder.tv_t1.setVisibility(View.VISIBLE);
                        holder.tv_t2.setVisibility(View.GONE);
                        holder.tv_qty.setVisibility(View.VISIBLE);
                    }
                    return holder;
                }

                @Override
                public void initContent(CartonView holder, final Carton data) {
                    if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value) || StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
                        holder.tv_t1.setText("BoxID:" + data.getBoxId());
                        if (StrUtil.isNotBlank(data.getRam())) {
                            holder.tv_t1.setText(StrUtil.format("PN:{}  RAM:({})", data.getPartNo(), data.getRam()));
                        }
                        holder.tv_t2.setText("WorkCell:" + data.getWorkCell());
                        holder.tv_t3.setText("PN:" + data.getPartNo());
                        holder.tv_qty.setText("Q:" + data.getQty());
                    }
                    if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
                        holder.tv_t1.setText(String.valueOf(data.getReferenceId()));
                        holder.tv_t2.setText((StrUtil.format("{}", data.getId())));
                        holder.tv_qty.setText(String.valueOf(data.getQty()));
                    }
                    if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
                        holder.tv_t1.setText("ESR:" + data.getEsr());
                        holder.tv_qty.setText("Q:" + data.getCartonCount());
                    }
                    if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
                        holder.tv_t1.setText("ESR:" + data.getEsr());
                        holder.tv_t2.setText("FW:" + data.getForward());
                        holder.tv_qty.setText("Q:" + data.getCartonCount());
                    }
                }
            };
            lv_carton.setOnItemClickListener((parent, view, position, id) -> {

            });
            lv_carton.setOnItemLongClickListener((parent, view, position, id) -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(StockInAddActivity.this);
                dialog.setTitle("Alert");//设置对话框的标题
                dialog.setMessage("Delete This!");//设置对话框的内容
                dialog.setCancelable(false);//设置对话框是否可以取消
                //确定按钮的点击事件
                dialog.setPositiveButton("OK", (dialog12, which) -> {
                    StringRequest request = new StringRequest(App.getMethod("/stockIn/delete"), RequestMethod.POST);
                    request.add("id", cartonList.get(position).getId());
                    request.add("palletId", palletId);
                    CallServer.getInstance().add(0, request, new HttpResponse(StockInAddActivity.this) {
                        @Override
                        public void onStart(int what) {

                        }

                        @Override
                        public void onOK(JSONObject object) {
                            cartonList.clear();
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (Object o : jsonArray) {
                                JSONObject jsonObject = (JSONObject) o;
                                Carton carton = Carton.parse(jsonObject);
                                if (palletId == null) {
                                    palletId = carton.getPalletId();
                                }
                                cartonList.add(carton);
                            }
                            TTSUtil.speak("Good");
                            refreshListView();
                        }

                        @Override
                        public void onFail(JSONObject object) {

                        }

                        @Override
                        public void onError() {
                            TTSUtil.speak("error");
                        }
                    });
                });
                //取消按钮的点击事件
                dialog.setNegativeButton("Cancel", (dialog1, which) -> {
                });
                dialog.show();
                return false;
            });
            lv_carton.setAdapter(adapter);
        }
        adapter.setDataList(cartonList);
        adapter.notifyDataSetChanged();
        tv_box_count.setText(String.valueOf(cartonList.size()));
    }

    private void boxAdd() {
        StringRequest request = null;
        if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value)) {
            if (!ObjectUtil.isAllNotEmpty(et_box_id.getText(), et_part_no.getText(), et_qty.getText())) {
                return;
            }
            if (sp_work_cell.getSelectedItem() == null) {
                TTSUtil.speak("please input work cell");
                return;
            }
            if (cartonList.stream().filter(x -> StrUtil.equals(x.getBoxId(), et_box_id.getText().toString())).findFirst().isPresent()) {
                TTSUtil.speak("exits box id");
                return;
            }
            request = new StringRequest(App.getMethod("/stockIn/addFinishGoodsBox"), RequestMethod.POST);
            request.add("boxId", et_box_id.getText().toString());
            if (palletId != null) {
                request.add("palletId", palletId);
            }
            if (et_rma.getVisibility() == View.VISIBLE) {
                request.add("rma", et_rma.getText().toString());
            }
            request.add("partNo", et_part_no.getText().toString());
            request.add("qty", et_qty.getText().toString());
            request.add("fgtf", et_fgtf.getText().toString());
            request.add("workCell", sp_work_cell.getSelectedItem().toString());
        }
        if (StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
            if (!ObjectUtil.isAllNotEmpty(et_box_id.getText(), et_part_no.getText(), et_qty.getText())) {
                return;
            }
            if (cartonList.stream().filter(x -> x.getBoxId().equals(et_box_id.getText().toString())).findFirst().isPresent()) {
                TTSUtil.speak("exits box id");
                return;
            }
            request = new StringRequest(App.getMethod("/stockIn/addSemiFGBox"), RequestMethod.POST);
            request.add("boxId", et_box_id.getText().toString());
            if (palletId != null) {
                request.add("palletId", palletId);
            }
            if (et_rma.getVisibility() == View.VISIBLE) {
                request.add("rma", et_rma.getText().toString());
            }
            request.add("partNo", et_part_no.getText().toString());
            request.add("fgtf", et_fgtf.getText().toString());
            request.add("qty", et_qty.getText().toString());
            request.add("workCell", sp_work_cell.getSelectedItem().toString());
        }
        if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
            request = new StringRequest(App.getMethod("/stockIn/addRawMaterialBox"), RequestMethod.POST);
            request.add("referenceId", et_reference_id.getText().toString());
            request.add("grn", et_grn.getText().toString());
            if (palletId != null) {
                request.add("palletId", palletId);
            }
        }
        if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
            if (!ObjectUtil.isAllNotEmpty(et_esr.getText())) {
                return;
            }
            request = new StringRequest(App.getMethod("/stockIn/addRtcBox"), RequestMethod.POST);
            request.add("type", sp_type.getSelectedItem().toString());
            request.add("esr", et_esr.getText().toString());
            request.add("cartonCount", et_carton_count.getText().toString());
            if (palletId != null) {
                request.add("palletId", palletId);
            }
        }
        if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
            if (!ObjectUtil.isAllNotEmpty(et_esr.getText())) {
                return;
            }
            request = new StringRequest(App.getMethod("/stockIn/addStagingBox"), RequestMethod.POST);
            request.add("type", sp_type.getSelectedItem().toString());
            request.add("esr", et_esr.getText().toString());
            request.add("forward", sp_forward.getSelectedItem().toString());
            request.add("cartonCount", et_carton_count.getText().toString());
            if (palletId != null) {
                request.add("palletId", palletId);
            }
        }
        assert request != null;
        CallServer.getInstance().add(0, request, new HttpResponse(StockInAddActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject object) {
                cartonList.clear();
                JSONArray jsonArray = object.getJSONArray("data");
                for (Object o : jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    Carton carton = Carton.parse(jsonObject);
                    if (palletId == null) {
                        palletId = carton.getPalletId();
                    }
                    cartonList.add(carton);
                }
                TTSUtil.speak("Good");
                refreshListView();
                reset();
            }

            @Override
            public void onFail(JSONObject object) {
                TTSUtil.speak("Fail");
            }

            @Override
            public void onError() {
                TTSUtil.speak("Error");
            }
        });
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
            if (editText.getId() == et_qty.getId()) {
                if (!str.startsWith("Q")) {
                    TTSUtil.speak("error");
                    et_qty.setText(null);
                    et_qty.requestFocus();
                    return;
                }
            }
            if (editText.getId() == et_part_no.getId()) {
                if (!str.startsWith("P")) {
                    TTSUtil.speak("error");
                    et_part_no.setText(null);
                    et_part_no.requestFocus();
                    return;
                }
            }
            if (editText.getId() == et_rma.getId()) {
                if (!str.startsWith("TR")) {
                    TTSUtil.speak("error");
                    et_rma.setText(null);
                    et_rma.requestFocus();
                    return;
                }
            }
            if (editText.getId() == et_box_id.getId()) {
                if (!cb_rma.isChecked() && !StrUtil.startWithAny(str, "PA134", "PV19", "PAG1", "PAS1", "PA", "PABD", "PA95", "PA124", "PA112", "PA193", "PA140", "PCT8", "PA104", "PFGT", "BL19")) {
                    TTSUtil.speak("error");
                    et_box_id.setText(null);
                    et_box_id.requestFocus();
                    return;
                }
                if (cb_rma.isChecked() && !StrUtil.startWithAny(str, "RA134", "RV19", "RAG1", "RAS1", "RA", "RA78", "RA95", "RA124", "RA112", "RA193", "RA140", "PA102", "RA104", "RA11", "RL19")) {
                    TTSUtil.speak("error");
                    et_box_id.setText(null);
                    et_box_id.requestFocus();
                    return;
                }
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
                boxAdd();
            }
            if (nextView.getId() == R.id.btn_add_pallet) {
                palletAdd();
            }
        }
    }

    private void reset() {
        et_part_no.setText(null);
        et_qty.setText(null);
        et_box_id.setText(null);
        et_grn.setText(null);
        et_reference_id.setText(null);
        et_esr.setText(null);
        et_carton_count.setText(null);
        et_rma.setText(null);
        if (StrUtil.equals(function, FUNCTION.FINISHED_GOODS.value)) {
            et_part_no.requestFocus();
        }
        if (StrUtil.equals(function, FUNCTION.SEMI_FG.value)) {
            et_part_no.requestFocus();
        }
        if (StrUtil.equals(function, FUNCTION.RAW_MATERIAL.value)) {
            et_reference_id.requestFocus();
        }
        if (StrUtil.equals(function, FUNCTION.RTV_RTC.value)) {
            et_esr.requestFocus();
        }
        if (StrUtil.equals(function, FUNCTION.STAGING.value)) {
            et_esr.requestFocus();
        }
    }

    @Override
    public void onBackPressed() {
        if (cartonList.size() > 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(StockInAddActivity.this);
            dialog.setTitle("Alert");//设置对话框的标题
            dialog.setMessage("confirm back!");//设置对话框的内容
            dialog.setCancelable(false);//设置对话框是否可以取消
            //确定按钮的点击事件
            dialog.setPositiveButton("OK", (dialog12, which) -> StockInAddActivity.this.finish());
            //取消按钮的点击事件
            dialog.setNegativeButton("Cancel", (dialog1, which) -> {
            });
            dialog.show();
            return;
        }
        this.finish();
    }
}