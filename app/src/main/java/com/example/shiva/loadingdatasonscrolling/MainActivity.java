package com.example.shiva.loadingdatasonscrolling;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shiva.loadingdatasonscrolling.listener.OnLoadMoreListener;
import com.example.shiva.loadingdatasonscrolling.model.NotificationDataList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/* Its a Simple Demo.. it can also be used for loading datas from server... just wrap it with the request while using volley */

public class MainActivity extends AppCompatActivity {

    Activity activity;
    Context context;
    RecyclerView recyclerView;
    List<NotificationDataList> dataList = new ArrayList<>();
    RecyclerAdapter RecyclerAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        context = getApplicationContext();


        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.status_bar_color));
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        RecyclerAdapter = new RecyclerAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setAdapter(RecyclerAdapter);

        //LOADING DATAS FROM SERVER INITIALLY
        initView();

        //AGAIN LOADING DATAS FROM SERVER WHILE SCROLLING
        RecyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                dataList.add(null);
                RecyclerAdapter.notifyItemInserted(dataList.size() - 1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dataList.remove(dataList.size() - 1);
                        RecyclerAdapter.notifyItemRemoved(dataList.size());

                        int index = dataList.size();
                        int end = index + 20;

                        for (int i = index; i < end; i++) {

                            NotificationDataList data = new NotificationDataList();

                            data.setsNotificationName("Shiva Snape"+" "+String.valueOf(i));
                            data.setsMessage("New Notification Created by You");
                            data.setsDate("23/08/2017");
                            data.setsTime("09:30 AM");
                            data.setsAging(String.valueOf(i) + "D");

                            dataList.add(data);

                        }
                        RecyclerAdapter.notifyDataSetChanged();
                        RecyclerAdapter.setLoaded();

                    }}, 3000);

            }
        });
    }

    private void initView() {

        for (int i = 0; i < 25; i++) {

            NotificationDataList data = new NotificationDataList();

            data.setsNotificationName("Shiva Snape"+" "+String.valueOf(i));
            data.setsMessage("New Notification Created by You");
            data.setsDate("23/08/2017");
            data.setsTime("09:30 AM");
            data.setsAging(String.valueOf(i) + "D");

            dataList.add(data);
        }

        RecyclerAdapter.notifyDataSetChanged();

    }


    //VIEWHOLDERS
    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        public TextView tNotification, tDateTime, tAging, tImageText;
        CircleImageView circleImageView;

        public NotificationViewHolder(View view) {
            super(view);
//            tvName = (TextView) itemView.findViewById(R.id.tvName);
//            tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);

            tNotification = (TextView) view.findViewById(R.id.txt_notification1);
            tDateTime = (TextView) view.findViewById(R.id.txt_notification2);
            circleImageView = (CircleImageView) view.findViewById(R.id.myImageView_notification);
            tAging = (TextView) view.findViewById(R.id.txt_notification3);
            tImageText = (TextView) view.findViewById(R.id.myImageViewText_notification);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


    //ADAPTER
    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private OnLoadMoreListener mOnLoadMoreListener;
        private boolean isLoading;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        List<NotificationDataList> dataLists = new ArrayList<>();
        Context context;
        String firstLetter, secondLetter;
        String string1;

        public RecyclerAdapter(List<NotificationDataList> dataList) {

            this.dataLists = dataList;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });

        }


        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return dataLists.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_recycle_items, parent, false);
                return new NotificationViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_loading, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof NotificationViewHolder) {

                Typeface tf = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");

                NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
                NotificationDataList data = dataLists.get(position);

                notificationViewHolder.tNotification.setTypeface(tf);
                notificationViewHolder.tDateTime.setTypeface(tf);
                notificationViewHolder.tAging.setTypeface(tf);
                notificationViewHolder.tImageText.setTypeface(tf);

                String name = data.getsNotificationName();
                //for getting the first two letter from the Name
                String[] myName = name.split(" ");
                int arrayLength = myName.length;

                if (arrayLength == 1) {
                    for (int i = 0; i < 1; i++) {
                        String s = myName[0].toUpperCase();
                        firstLetter = String.valueOf(s.charAt(0));
                    }
                } else if (arrayLength == 2) {
                    for (int i = 0; i < 2; i++) {
                        String s = myName[0].toUpperCase();
                        String s1 = myName[1].toUpperCase();
                        firstLetter = String.valueOf(s.charAt(0));
                        secondLetter = String.valueOf(s1.charAt(0));//
                    }
                }

                String age = data.getsAging();

                String[] agin = age.split(" ");
                for (int k = 0; k < agin.length; k++) {
                    String aS1 = agin[0];
                    string1 = aS1;
                }

                //SETTING RANDOM COLORS
                int[] cardColors = context.getResources().getIntArray(R.array.roundcolors);
                int randomRoundColor = cardColors[new Random().nextInt(cardColors.length)];

                if (arrayLength == 1) {
                    notificationViewHolder.tImageText.setText(firstLetter);

                } else if (arrayLength == 2)

                {
                    notificationViewHolder.tImageText.setText(firstLetter + secondLetter);

                }
                notificationViewHolder.tNotification.setText(data.getsMessage());
                notificationViewHolder.tDateTime.setText(data.getsDate() + " " + data.getsTime());
                notificationViewHolder.tAging.setText(string1 + " " + "ago");
                notificationViewHolder.circleImageView.setImageDrawable(new ColorDrawable(randomRoundColor));


            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return dataLists == null ? 0 : dataLists.size();
        }

        public void setLoaded() {
            isLoading = false;
        }
    }
}
