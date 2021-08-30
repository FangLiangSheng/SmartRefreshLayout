package com.scwang.refreshlayout.activity.kuma;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.example.CustomExampleActivity;
import com.scwang.refreshlayout.adapter.BaseRecyclerAdapter;
import com.scwang.refreshlayout.adapter.SmartViewHolder;
import com.scwang.smart.drawable.ProgressDrawable;
import com.scwang.smart.refresh.classics.ArrowDrawable;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.util.SmartUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Arrays;
import java.util.Collection;

public class KumaGoodsActivity extends AppCompatActivity {

    private class Model {
        int imageId;
        int avatarId;
        String name;
        String nickname;
    }


    private BaseRecyclerAdapter<KumaGoodsActivity.Model> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuma_goods);

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(true);
//        refreshLayout.setEnableFooterFollowWhenNoMoreData(true);

        //初始化列表和监听
        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<KumaGoodsActivity.Model>(loadModels(), R.layout.item_practice_repast) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, KumaGoodsActivity.Model model, int position) {
                    holder.text(R.id.name, model.name);
                    holder.text(R.id.nickname, model.nickname);
                    holder.image(R.id.image, model.imageId);
                    holder.image(R.id.avatar, model.avatarId);
                }
            });

            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.finishRefresh();
                            refreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
                        }
                    }, 2000);
                }
                @Override
                public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdapter.getCount() > 12) {
                                Toast.makeText(getBaseContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                                refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                            } else {
                                mAdapter.loadMore(loadModels());
                                refreshLayout.finishLoadMore();
                            }
                        }
                    }, 1000);
                }
            });

            refreshLayout.setRefreshHeader(new KumaGoodsActivity.ClassicsHeader(this));
            refreshLayout.setHeaderHeight(60);

            refreshLayout.setRefreshFooter(new KUMAGoodsFooter(this));
            refreshLayout.setFooterHeight(60);

//            refreshLayout.getLayout().postDelayed(new Runnable() {
//                @Override
//                public void run() {
////                    refreshLayout.setHeaderInsetStart(SmartUtil.px2dp(toolbar.getHeight()));
//                    refreshLayout.setHeaderInsetStart(SmartUtil.px2dp(0));
//                }
//            }, 500);
        }
    }


    /**
     * 模拟数据
     */
    private Collection<KumaGoodsActivity.Model> loadModels() {
        return Arrays.asList(
                new KumaGoodsActivity.Model() {{
                    this.name = "但家香酥鸭";
                    this.nickname = "爱过那张脸";
                    this.imageId = R.mipmap.image_practice_repast_1;
                    this.avatarId = R.mipmap.image_avatar_1;
                }}, new KumaGoodsActivity.Model() {{
                    this.name = "香菇蒸鸟蛋";
                    this.nickname = "淑女算个鸟";
                    this.imageId = R.mipmap.image_practice_repast_2;
                    this.avatarId = R.mipmap.image_avatar_2;
                }}, new KumaGoodsActivity.Model() {{
                    this.name = "花溪牛肉粉";
                    this.nickname = "性感妩媚";
                    this.imageId = R.mipmap.image_practice_repast_3;
                    this.avatarId = R.mipmap.image_avatar_3;
                }}, new KumaGoodsActivity.Model() {{
                    this.name = "破酥包";
                    this.nickname = "一丝丝纯真";
                    this.imageId = R.mipmap.image_practice_repast_4;
                    this.avatarId = R.mipmap.image_avatar_4;
                }}, new KumaGoodsActivity.Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new KumaGoodsActivity.Model() {{
                    this.name = "米豆腐";
                    this.nickname = "宝宝树人";
                    this.imageId = R.mipmap.image_practice_repast_6;
                    this.avatarId = R.mipmap.image_avatar_6;
                }});
    }

    public static class ClassicsHeader extends LinearLayout implements RefreshHeader {

        private TextView mHeaderText;//标题文本
        private ImageView mArrowView;//下拉箭头
        private ImageView mProgressView;//刷新动画视图
        private ProgressDrawable mProgressDrawable;//刷新动画

        public ClassicsHeader(Context context) {
            this(context, null);
        }
        public ClassicsHeader(Context context, AttributeSet attrs) {
            super(context, attrs, 0);
            setGravity(Gravity.CENTER);
            mHeaderText = new TextView(context);
            mProgressDrawable = new ProgressDrawable();
            mArrowView = new ImageView(context);
            mProgressView = new ImageView(context);
            mProgressView.setImageDrawable(mProgressDrawable);
            mArrowView.setImageDrawable(new ArrowDrawable());
            addView(mProgressView, com.scwang.smart.refresh.layout.util.SmartUtil.dp2px(20), com.scwang.smart.refresh.layout.util.SmartUtil.dp2px(20));
            addView(mArrowView, com.scwang.smart.refresh.layout.util.SmartUtil.dp2px(20), com.scwang.smart.refresh.layout.util.SmartUtil.dp2px(20));
            addView(new Space(context), com.scwang.smart.refresh.layout.util.SmartUtil.dp2px(20), com.scwang.smart.refresh.layout.util.SmartUtil.dp2px(20));
            addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            setMinimumHeight(SmartUtil.dp2px(60));
        }
        @NonNull
        public View getView() {
            return this;//真实的视图就是自己，不能返回null
        }
        @NonNull
        @Override
        public SpinnerStyle getSpinnerStyle() {
            return SpinnerStyle.Translate;//指定为平移，不能null
        }

        @Override
        public void onStartAnimator(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout layout, int height, int maxDragHeight) {
            mProgressDrawable.start();//开始动画
        }

        @Override
        public int onFinish(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout layout, boolean success) {
            mProgressDrawable.stop();//停止动画
            mProgressView.setVisibility(GONE);//隐藏动画
            if (success){
                mHeaderText.setText("刷新完成");
            } else {
                mHeaderText.setText("刷新失败");
            }
            return 500;//延迟500毫秒之后再弹回
        }

        @Override
        public void onStateChanged(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
            switch (newState) {
                case None:
                case PullDownToRefresh:
                    mHeaderText.setText("下滑查看上一级分类");
                    mArrowView.setVisibility(VISIBLE);//显示下拉箭头
                    mProgressView.setVisibility(GONE);//隐藏动画
                    mArrowView.animate().rotation(0);//还原箭头方向
                    break;
                case Refreshing:
                    mHeaderText.setText("正在获取最新商品信息");
                    mProgressView.setVisibility(VISIBLE);//显示加载动画
                    mArrowView.setVisibility(GONE);//隐藏箭头
                    break;
                case ReleaseToRefresh:
                    mHeaderText.setText("释放查看上一级分类");
                    mArrowView.animate().rotation(180);//显示箭头改为朝上
                    break;
            }
        }

        @Override
        public void setPrimaryColors(int... colors) {

        }

        @Override
        public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

        }

        @Override
        public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

        }

//        @Override
//        public void onPulling(float percent, int offset, int height, int maxDragHeight) {
//
//        }
//        @Override
//        public void onReleasing(float percent, int offset, int height, int maxDragHeight) {
//
//        }

        @Override
        public void onReleased(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout, int height, int maxDragHeight) {

        }

        @Override
        public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

        }

        @Override
        public boolean isSupportHorizontalDrag() {
            return false;
        }
    }
}