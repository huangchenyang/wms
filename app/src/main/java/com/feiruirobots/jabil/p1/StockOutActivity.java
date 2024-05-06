package com.feiruirobots.jabil.p1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.adapter.MyListAdapter;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.common.ToastUtil;
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
import cn.hutool.core.util.StrUtil;

public class StockOutActivity extends BaseActivity {
    private String function;
    private MyListAdapter<BizTaskView, BizTask> adapter;
    private List<BizTask> bizTaskList = new ArrayList<>();
    private String TAG = "hcy--StockOutActivity";

    @BindView(R.id.lv_biz_task)
    ListView lv_biz_task;
    @SuppressLint("HandlerLeak")
    private Handler handlerSend = new Handler() {
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
        setContentView(R.layout.activity_stock_out);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        function = intent.getStringExtra("FUNCTION");
        initTitle(Objects.requireNonNull(FUNCTION.of(function)).msg + " Out Stock List");
        initListView();
        handlerSend.sendEmptyMessageDelayed(1, 1000);
    }

    class BizTaskView {
        TextView tv_pallet_id, tv_fgtf, tv_binid, tv_box_count,tv_terminal_in;
    }

    private void initListView() {
        adapter = new MyListAdapter<BizTaskView, BizTask>(this, bizTaskList, R.layout.item_task_biz) {
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
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(data.getFromId());
//                if (StrUtil.isNotBlank(data.getBufferId())) {
//                    stringBuilder.append("  →  ");
//                    stringBuilder.append(data.getBufferId());
//                }
//                if (StrUtil.isNotBlank(data.getDestId())) {
//                    stringBuilder.append("  →  ");
//                    stringBuilder.append(data.getDestId());
//                }
                holder.tv_pallet_id.setText(data.getId());
                holder.tv_fgtf.setText(data.getId());
//                if (data.getStatus() == 3) {
//                    holder.tv_status.setText("Wait Pick");
//                } else if (data.getStatus() == 1) {
//                    holder.tv_status.setText("Wait Running");
//                } else {
//                    holder.tv_status.setText("Running");
//                }
//                holder.tv_desc.setText(data.getName());
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
                    bizTaskList.add(BizTask.parse(jsonObject));
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
}