package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Scroller;

/**
 * Created by dengjx on 2017/12/26.
 */


public class AutoLocateHorizontalView extends RecyclerView
       {
    private static final String TAG = "AutoLocate";
    /**
     * 一个屏幕中显示多少个item，必须为奇数
     */
    private int itemCount = 3;
    /**
     * 初始时选中的位置
     */
    private int initPos = 2;

    private int deltaX;
    private WrapperAdapter wrapAdapter;
    private Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isInit;
    private OnSelectedPositionChangedListener listener;
    private boolean isFirstPosChanged = true;        //刚初始化时是否触发位置改变的监听
    private int oldSelectedPos = initPos;   //记录上次选中的位置
    /**
     * 当前被选中的位置
     */
    private int selectPos = initPos;

    private Scroller mScroller;

    /**
     * 当要调用moveToPosition()方法时要先记录已经移动了多少位置
     */
    private int oldMoveX;

    private boolean isMoveFinished = true;

    public AutoLocateHorizontalView(Context context) {
        super(context);
    }

    public AutoLocateHorizontalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLocateHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        mScroller = new Scroller(getContext());
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isInit) {
                    if (initPos >= adapter.getItemCount()) {
                        initPos = adapter.getItemCount() - 1;
                    }
                    if (isFirstPosChanged && listener != null) {
                        listener.selectedPositionChanged(initPos);
                    }
                    linearLayoutManager.scrollToPositionWithOffset(0, -initPos * (wrapAdapter.getItemWidth()));
                    isInit = false;
                }
            }
        });
    }

    /**
     * 设置初始化时选中的位置,该方法必须在{@link AutoLocateHorizontalView#setAdapter(android.support.v7.widget.RecyclerView.Adapter) }之前调用
     *
     * @param initPos 初始位置，如果位置超过了item的数量则默认选中最后一项item
     */
    public void setInitPos(int initPos) {
        if (adapter != null) {
            throw new RuntimeException("This method should be called before setAdapter()!");
        }
        this.initPos = initPos;
        selectPos = initPos;
        oldSelectedPos = initPos;
    }

    /**
     * 设置每次显示多少个item,该方法必须在{@link AutoLocateHorizontalView#setAdapter(android.support.v7.widget.RecyclerView.Adapter) }之前调用
     *
     * @param itemCount 必须为奇数，否则默认会设置成小于它的最大奇数
     */
    public void setItemCount(int itemCount) {
        if (adapter != null) {
            throw new RuntimeException("This method should be called before setAdapter()!");
        }
        if (itemCount % 2 == 0) {
            this.itemCount = itemCount - 1;
        } else {
            this.itemCount = itemCount;
        }
    }

    /**
     * 删除item后偏移距离可能需要重新计算，从而保证selectPos的正确
     *
     * @param adapter
     */
    private void correctDeltax(Adapter adapter) {
        if (adapter.getItemCount() <= selectPos) {
            deltaX -= wrapAdapter.getItemWidth() * (selectPos - adapter.getItemCount() + 1);
        }
        calculateSelectedPos();
    }



    @Override
    public void setAdapter(final Adapter adapter) {
        this.adapter = adapter;
        this.wrapAdapter = new WrapperAdapter(adapter, getContext(), itemCount);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                wrapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                wrapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                wrapAdapter.notifyDataSetChanged();
            }
        });
        deltaX = 0;
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getContext());
        }
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        super.setLayoutManager(linearLayoutManager);
        super.setAdapter(this.wrapAdapter);
        isInit = true;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof LinearLayoutManager)) {
            throw new IllegalStateException("The LayoutManager here must be LinearLayoutManager!");
        }
        this.linearLayoutManager = (LinearLayoutManager) layout;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == SCROLL_STATE_IDLE) {
            if (wrapAdapter == null) {
                return;
            }
            int itemWidth = wrapAdapter.getItemWidth();
            int headerFooterWidth = wrapAdapter.getHeaderFooterWidth();
            if (itemWidth == 0 || headerFooterWidth == 0) {
                //此时adapter还没有准备好，忽略此次调用
                return;
            }
            //超出上个item的位置
            int overLastPosOffset = deltaX % itemWidth;
            Log.d(TAG,"overLastPosOffset:" +overLastPosOffset);
            Log.d(TAG,"itemWidth:" +itemWidth);
            if (overLastPosOffset == 0) {
                //刚好处于一个item选中位置，无需滑动偏移纠正
            } else if (Math.abs(overLastPosOffset) <= itemWidth / 2) {
                scrollBy(-overLastPosOffset, 0);
            } else if (overLastPosOffset > 0) {
                scrollBy((itemWidth - overLastPosOffset), 0);
            } else {
                scrollBy(-(itemWidth + overLastPosOffset), 0);
            }
            calculateSelectedPos();
            Log.d(TAG,"selectPos:" +selectPos);
            //此处通知刷新是为了重新绘制之前被选中的位置以及刚刚被选中的位置
            wrapAdapter.notifyItemChanged(oldSelectedPos + 1);
            wrapAdapter.notifyItemChanged(selectPos + 1);
            oldSelectedPos = selectPos;
            if (listener != null) {
                listener.selectedPositionChanged(selectPos);
            }
        }
    }
    // 点击更新数据
       public void updateData(int pos){
           int itemWidth = wrapAdapter.getItemWidth();
           if(pos > oldSelectedPos){
               scrollBy((pos - oldSelectedPos) * itemWidth, 0);
           }else if(pos < oldSelectedPos){
               scrollBy((pos - oldSelectedPos) * itemWidth, 0);
           }
           selectPos = pos;
           Log.d(TAG,"selectPos:" +selectPos);
           //此处通知刷新是为了重新绘制之前被选中的位置以及刚刚被选中的位置
           wrapAdapter.notifyItemChanged(oldSelectedPos + 1);
           wrapAdapter.notifyItemChanged(selectPos + 1);
           oldSelectedPos = selectPos;
           if (listener != null) {
               listener.selectedPositionChanged(selectPos);
           }
       }

    public void moveToPosition(int position) {
        if(position < 0 || position > adapter.getItemCount() - 1){
            throw new IllegalArgumentException("Your position should be from 0 to "+(adapter.getItemCount()-1));
        }
        oldMoveX = 0;
        isMoveFinished = false;
        int itemWidth = wrapAdapter.getItemWidth();
        if (position != selectPos) {
            int deltx = (position - selectPos) * itemWidth;
            mScroller.startScroll(getScrollX(), getScrollY(), deltx, 0);
            postInvalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX() - oldMoveX;
            oldMoveX += x;
            scrollBy(x, 0);
        } else if (mScroller.isFinished()) {
            //此处通知刷新是为了重新绘制之前被选中的位置以及刚刚被选中的位置
            if (isMoveFinished) {
                return;
            }
            wrapAdapter.notifyItemChanged(oldSelectedPos + 1);
            wrapAdapter.notifyItemChanged(selectPos + 1);
            oldSelectedPos = selectPos;
            if (listener != null) {
                listener.selectedPositionChanged(selectPos);
            }
            isMoveFinished = true;
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        deltaX += dx;
        calculateSelectedPos();
    }

    private void calculateSelectedPos() {
        int itemWidth = wrapAdapter.getItemWidth() / 2 + 10;
        if (deltaX > 0) {
            selectPos = (deltaX) / itemWidth + initPos;
        } else {
            selectPos = initPos + (deltaX) / itemWidth;
        }

    }


    class WrapperAdapter extends RecyclerView.Adapter {
        private Context context;
        private RecyclerView.Adapter adapter;
        private int itemCount;
        private static final int HEADER_FOOTER_TYPE = -1;
        private View itemView;
        /**
         * 头部或尾部的宽度
         */
        private int headerFooterWidth;

        /**
         * 每个item的宽度
         */
        private int itemWidth;

        public WrapperAdapter(Adapter adapter, Context context, int itemCount) {
            this.adapter = adapter;
            this.context = context;
            this.itemCount = itemCount;
            if (adapter instanceof IAutoLocateHorizontalView) {
                itemView = ((IAutoLocateHorizontalView) adapter).getItemView();
            } else {
                throw new RuntimeException(adapter.getClass().getSimpleName() + " should implements com.jianglei.view.AutoLocateHorizontalView.IAutoLocateHorizontalView !");
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER_FOOTER_TYPE) {
                View view = new View(context);
                headerFooterWidth = (parent.getMeasuredWidth() / itemCount) ;
                Log.d(TAG,"headerFooterWidth:"+headerFooterWidth);
                RecyclerView.LayoutParams params = new LayoutParams(headerFooterWidth, 100);
                view.setLayoutParams(params);
                return new HeaderFooterViewHolder(view);
            }
            ViewHolder holder = adapter.onCreateViewHolder(parent, viewType);
            itemView = ((IAutoLocateHorizontalView) adapter).getItemView();
            int headandfootWidth = parent.getMeasuredWidth() / itemCount;
            Log.d(TAG,"itemwidth:"+headandfootWidth);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams
                    (headandfootWidth, 100);
            itemView.setLayoutParams(layoutParams);
            itemWidth = headandfootWidth;
            return holder;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (!isHeaderOrFooter(position)) {
                adapter.onBindViewHolder(holder, position - 1);
                if (selectPos == position - 1) {
                    ((IAutoLocateHorizontalView) adapter).onViewSelected(true, position - 1, holder, itemWidth);
                } else {
                    ((IAutoLocateHorizontalView) adapter).onViewSelected(false, position - 1, holder, itemWidth);
                }
            }
        }


        @Override
        public int getItemCount() {
            return adapter.getItemCount() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == getItemCount() - 1) {
                return HEADER_FOOTER_TYPE;
            }
            return adapter.getItemViewType(position - 1);
        }


        private boolean isHeaderOrFooter(int pos) {
            if (pos == 0 || pos == getItemCount() - 1) {
                return true;
            }
            return false;
        }

        public int getHeaderFooterWidth() {
            return headerFooterWidth;
        }

        public int getItemWidth() {
            return itemWidth;
        }

        class HeaderFooterViewHolder extends RecyclerView.ViewHolder {

            HeaderFooterViewHolder(View itemView) {
                super(itemView);
            }
        }


    }


    public interface IAutoLocateHorizontalView {
        /**
         * 获取item的根布局
         */
        View getItemView();

        /**
         * 当item被选中时会触发这个回调，可以修改被选中时的样式
         *
         * @param isSelected 是否被选中
         * @param pos        当前view的位置
         * @param holder
         * @param itemWidth  当前整个item的宽度
         */
        void onViewSelected(boolean isSelected, int pos, ViewHolder holder, int itemWidth);
    }

    /***
     * 选中位置改变时的监听
     */
    public interface OnSelectedPositionChangedListener {
        void selectedPositionChanged(int pos);
    }

    public void setOnSelectedPositionChangedListener(OnSelectedPositionChangedListener listener) {
        this.listener = listener;
    }
}
