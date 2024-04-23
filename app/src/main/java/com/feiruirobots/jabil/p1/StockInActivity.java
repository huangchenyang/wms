package com.feiruirobots.jabil.p1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.adapter.MyListAdapter;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.http.CallServer;
import com.feiruirobots.jabil.p1.http.HttpResponse;
import com.feiruirobots.jabil.p1.model.BIZ_TASK_STATUS;
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
import butterknife.OnClick;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;

public class StockInActivity extends BaseActivity {
    private String function;
    private MyListAdapter adapter;
    private List<BizTask> bizTaskList = new ArrayList<>();

    @BindView(R.id.lv_biz_task)
    ListView lv_biz_task;
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
        initTitle(Objects.requireNonNull(FUNCTION.of(function)).msg + " In Stock List");
        initListView();
        getBizTask();
        handlerSend.sendEmptyMessageDelayed(1, 1000);
    }

    @OnClick({R.id.btn_new_pallet, R.id.btn_out_exist_pallet})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_pallet) {
            Intent intent = new Intent(StockInActivity.this, StockInAddActivity.class);
            intent.putExtra("FUNCTION", function);
            startActivity(intent);
        }
        if (view.getId() == R.id.btn_out_exist_pallet) {

        }
    }

    class BizTaskView {
        TextView tv_from_to, tv_status, tv_desc, tv_reference_id;
    }

    private void initListView() {
        adapter = new MyListAdapter<BizTaskView, BizTask>(StockInActivity.this, bizTaskList, R.layout.item_task_biz) {
            @Override
            public BizTaskView initView(View convertView, BizTaskView holder) {
                if (holder == null) holder = new BizTaskView();
                holder.tv_from_to = convertView.findViewById(R.id.tv_from_to);
                holder.tv_status = convertView.findViewById(R.id.tv_status);
                holder.tv_desc = convertView.findViewById(R.id.tv_desc);
                holder.tv_reference_id = convertView.findViewById(R.id.tv_reference_id);
                return holder;
            }

            @Override
            public void initContent(BizTaskView holder, final BizTask data) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(data.getFromId());
                if (StrUtil.isNotBlank(data.getBufferId())) {
                    stringBuilder.append(" → ");
                    stringBuilder.append(data.getBufferId());
                }
                if (StrUtil.isNotBlank(data.getDestId())) {
                    stringBuilder.append(" →  ");
                    stringBuilder.append(data.getDestId());
                }
                holder.tv_from_to.setText(stringBuilder.toString());
                holder.tv_reference_id.setText(data.getId());
                holder.tv_status.setText(BIZ_TASK_STATUS.of(data.getStatus()).msg);
                holder.tv_desc.setText(data.getName());
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
        String url = App.getMethod("/stockIn/bizList?function=" + function);
        StringRequest request = new StringRequest(url, RequestMethod.POST);
        CallServer.getInstance().add(0, request, new HttpResponse(StockInActivity.this) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject json) {
                bizTaskList.clear();
                JSONArray arrays = json.getJSONArray("data");
                for (Object array : arrays) {
                    JSONObject jsonObject = (JSONObject) array;
                    BizTask bizTask =BizTask.parse(jsonObject);
                    bizTaskList.add(bizTask);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(JSONObject object) {
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
}