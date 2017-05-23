package com.atguigu.tiankuo.videoplayer.pager;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.R;
import com.atguigu.tiankuo.videoplayer.activity.SystemVideoPlayerActivity;
import com.atguigu.tiankuo.videoplayer.adapter.NetVideoAdapter;
import com.atguigu.tiankuo.videoplayer.domain.MoveInfo;
import com.atguigu.tiankuo.videoplayer.fragment.BaseFragment;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


public class NetVideoPager extends BaseFragment {
    private NetVideoAdapter adapter;

    private ListView lv;
    private TextView tv_nodata;
    @Override
    public View initView() {
        Log.e("TAG","NetVideoPager-initView");
        View view = View.inflate(context, R.layout.fragment_local_video_pager,null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoveInfo.TrailersBean item = adapter.getItem(position);

                Intent intent = new Intent(context, SystemVideoPlayerActivity.class);
                intent.setDataAndType(Uri.parse(item.getUrl()),"video/*");
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","NetVideoPager-initData");
        getDataFromNet();
    }

    private void getDataFromNet() {

        final RequestParams request = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e("TAG","xUtils联网成功=="+result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","xUtils联网失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void processData(String json) {
        MoveInfo moveInfo = new Gson().fromJson(json, MoveInfo.class);
        List<MoveInfo.TrailersBean> datas = moveInfo.getTrailers();
        if(datas != null && datas.size() >0){
            tv_nodata.setVisibility(View.GONE);
            adapter = new NetVideoAdapter(context,datas);
            lv.setAdapter(adapter);
        }else{
            tv_nodata.setVisibility(View.VISIBLE);
        }

    }
}
