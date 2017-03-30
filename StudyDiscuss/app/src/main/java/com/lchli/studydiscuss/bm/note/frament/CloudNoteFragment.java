package com.lchli.studydiscuss.bm.note.frament;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.bm.note.activity.CloudNoteDetailActivity;
import com.lchli.studydiscuss.bm.note.busEvent.CloudNoteListChangedEvent;
import com.lchli.studydiscuss.bm.note.entity.Note;
import com.lchli.studydiscuss.bm.note.entity.NoteFilterData;
import com.lchli.studydiscuss.bm.note.entity.NotesResponse;
import com.lchli.studydiscuss.bm.note.entity.SortData;
import com.lchli.studydiscuss.bm.note.widget.GridDividerDecoration;
import com.lchli.studydiscuss.bm.note.widget.NoteFilterPopup;
import com.lchli.studydiscuss.bm.note.widget.SortPopupWraper;
import com.lchli.studydiscuss.bm.user.entity.User;
import com.lchli.studydiscuss.bm.user.model.UserSessionManager;
import com.lchli.studydiscuss.common.base.BaseFragment;
import com.lchli.studydiscuss.common.base.BaseRecyclerAdapter;
import com.lchli.studydiscuss.common.base.BaseResponse;
import com.lchli.studydiscuss.common.consts.LocalConst;
import com.lchli.studydiscuss.common.consts.SeverConst;
import com.lchli.studydiscuss.common.consts.UrlConst;
import com.lchli.studydiscuss.common.networkLib.AppHttpManager;
import com.lchli.studydiscuss.common.networkLib.OkError;
import com.lchli.studydiscuss.common.networkLib.OkErrorCode;
import com.lchli.studydiscuss.common.networkLib.OkUiCallback;
import com.lchli.studydiscuss.common.utils.AppDiskCacher;
import com.lchli.studydiscuss.common.utils.ContextProvider;
import com.lchli.studydiscuss.common.utils.EventBusUtils;
import com.lchli.studydiscuss.common.utils.HttpHelper;
import com.lchli.studydiscuss.common.utils.ListUtils;
import com.lchli.studydiscuss.common.utils.MapUtils;
import com.lchli.studydiscuss.common.utils.ResUtils;
import com.lchli.studydiscuss.common.utils.ToastUtils;
import com.lchli.studydiscuss.common.widget.CommonEmptyView;
import com.lchli.studydiscuss.common.widget.CommonTitleView;
import com.lchli.studydiscuss.common.widget.LoadingDialog;

import junit.framework.Assert;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lchli on 2016/8/10.
 */
public class CloudNoteFragment extends BaseFragment {


    @BindView(R2.id.common_title)
    CommonTitleView commonTitle;
    @BindView(R2.id.moduleListRecyclerView)
    RecyclerView moduleListRecyclerView;

    @BindView(R2.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R2.id.empty_widget)
    CommonEmptyView emptyWidget;

    private CloudNoteListAdapter mNotesAdapter;
    private TextView emptyTextView;
    private SortPopupWraper mSortPopup;
    private SortData currentSortData = new SortData(SeverConst.Note.SortBy_TimeAsc, "");
    private NoteFilterPopup mNoteFilterPopup;
    private NoteFilterData currentNoteFilterData = new NoteFilterData("", "");
    private LoadingDialog mLoadingDialog = new LoadingDialog();
    private static final long CLOUD_NOTES_CACHE_DURATION = 60 * 1000;

    public static CloudNoteFragment newInstance() {

        Bundle args = new Bundle();
        CloudNoteFragment fragment = new CloudNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_note_list, container, false);
        ButterKnife.bind(this, view);
        EventBusUtils.register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commonTitle.addRightText(ResUtils.parseString(R.string.filter), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoteFilterPopup == null) {
                    mNoteFilterPopup = new NoteFilterPopup(getActivity());
                    mNoteFilterPopup.setCallback(new NoteFilterPopup.Callback() {
                        @Override
                        public void confirm(NoteFilterData data) {
                            mNoteFilterPopup.dismiss();
                            currentNoteFilterData = data;
                            loadNetNotes(false);
                        }
                    });
                }
                mNoteFilterPopup.showAsDropDown(commonTitle);
            }
        });
        commonTitle.addRightText(ResUtils.parseString(R.string.sort), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSortPopup == null) {
                    mSortPopup = new SortPopupWraper(getActivity());
                    mSortPopup.getPopup().setItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mSortPopup.getPopup().dismiss();
                            currentSortData = (SortData) parent.getAdapter().getItem(position);
                            loadNetNotes(false);

                        }
                    });
                }
                mSortPopup.getPopup().showAsDropDown(commonTitle);
            }
        });
        commonTitle.setCenterText(ResUtils.parseString(R.string.cloud_note), null);
        emptyTextView = emptyWidget.addEmptyText("");

        moduleListRecyclerView.setHasFixedSize(true);
        moduleListRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        moduleListRecyclerView.addItemDecoration(new GridDividerDecoration(moduleListRecyclerView.getContext(), GridDividerDecoration.VERTICAL_LIST));

        mNotesAdapter = new CloudNoteListAdapter();
        moduleListRecyclerView.setAdapter(mNotesAdapter);
        moduleListRecyclerView.addOnScrollListener(mNotesAdapter.getListOnScrollListener());


    }

    @Override
    public void initLoadData() {
        loadNetNotes(false);
    }

    private void showEmpty(String msg) {
        emptyWidget.setVisibility(View.VISIBLE);
        emptyTextView.setText(msg);
        moduleListRecyclerView.setVisibility(View.GONE);
    }

    private void hideEmpty() {
        emptyWidget.setVisibility(View.GONE);
        moduleListRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadNetNotes(boolean isForceRefresh) {
        User session = UserSessionManager.getSession();
        if (session == null) {
            showEmpty(getString(R.string.not_login));
            return;
        }
        Map<String, String> params = MapUtils.stringMap();
        params.put("UserId", session.uid);
        params.put("SortBy", currentSortData.code + "");
        params.put("Title", currentNoteFilterData.title);
        params.put("Tag", currentNoteFilterData.tag);
        //check cache.
        final String url = UrlConst.GET_ALL_NOTES_URL;
        final String cacheKey = AppHttpManager.buildUrlCacheKey(params, url);

        if (!isForceRefresh) {
            String cache = AppDiskCacher.getInstance().getCache(cacheKey);
            if (!TextUtils.isEmpty(cache)) {
                NotesResponse reponseCache = new Gson().fromJson(cache, NotesResponse.class);
                //refresh ui.
                if (ListUtils.isEmpty(reponseCache.notes)) {
                    showEmpty(getString(R.string.empty_data));
                } else {
                    hideEmpty();
                    mNotesAdapter.refresh(reponseCache.notes);
                }
                return;
            }

        }
        //get from net.
        AppHttpManager.get(url, params, new OkUiCallback<NotesResponse>() {
            @Override
            public void onSuccess(NotesResponse response) {
                Assert.assertNotNull(response);
                if (response.code == OkErrorCode.SUCCESS) {
                    //save cache.
                    AppDiskCacher.getInstance().putCache(cacheKey, new Gson().toJson(response), CLOUD_NOTES_CACHE_DURATION);
                    //refresh ui.
                    if (ListUtils.isEmpty(response.notes)) {
                        showEmpty(getString(R.string.empty_data));
                    } else {
                        hideEmpty();
                        mNotesAdapter.refresh(response.notes);
                    }

                } else {
                    showEmpty(response.errorMsg);
                }

            }

            @Override
            public void onFail(OkError error) {
                showEmpty(error.errMsg);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusUtils.unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CloudNoteListChangedEvent e) {
        loadNetNotes(true);
    }


    class CloudNoteListAdapter extends BaseRecyclerAdapter<Note> {

        private final Bitmap def = BitmapFactory.decodeResource(ContextProvider.context().getResources(),
                R.drawable.ic_gf_default_photo);


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_cloud_note, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder h, int position) {
            VH holder = (VH) h;
            final Note data = mDatas.get(position);
            LogUtils.e("ShareUrl:"+data.ShareUrl);

            holder.couseTitleTextView.setText(data.title);
            holder.courseTimeTextView.setText(data.lastModifyTime);
            if (!isScrolling) {
                String url= HttpHelper.addExtraParamsToUrl(data.imagesDir +  data.thumbNail, LocalConst.getNoteServerVerifyParams());
                Glide.with(holder.itemView.getContext()).load(url).into(holder.courseThumbImageView);
            } else {
                holder.courseThumbImageView.setImageBitmap(def);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CloudNoteDetailActivity.startSelf(v.getContext(), data);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(ResUtils.parseString(R.string.delete_note_confirm_msg))
                            .setContentText(ResUtils.parseString(R.string.delete_note_warn))
                            .setConfirmText(ResUtils.parseString(R.string.confirm))
                            .setCancelText(ResUtils.parseString(R.string.cancel))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                    Map<String, String> params = MapUtils.stringMap();
                                    params.put("NoteId", data.uid);
                                    params.put("NoteDir", data.imagesDir);

                                    mLoadingDialog.show(ResUtils.parseString(R.string.loading_msg), getActivity());
                                    AppHttpManager.post(UrlConst.DELETE_NOTE_URL, params, new OkUiCallback<BaseResponse>() {
                                        @Override
                                        public void onSuccess(BaseResponse response) {
                                            mLoadingDialog.dismiss();
                                            if (response.code == OkErrorCode.SUCCESS) {
                                                ToastUtils.systemToast(ResUtils.parseString(R.string.delete_note_success));
                                                EventBusUtils.post(new CloudNoteListChangedEvent());
                                            } else {
                                                ToastUtils.systemToast(response.errorMsg);
                                            }
                                        }

                                        @Override
                                        public void onFail(OkError error) {
                                            mLoadingDialog.dismiss();
                                            ToastUtils.systemToast(error.errMsg);
                                        }
                                    });

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    return true;
                }
            });

        }

        class VH extends RecyclerView.ViewHolder {

            @BindView(R2.id.course_thumb_imageView)
            ImageView courseThumbImageView;
            @BindView(R2.id.couse_title_textView)
            TextView couseTitleTextView;
            @BindView(R2.id.course_time_textView)
            TextView courseTimeTextView;

            View itemView;

            public VH(View view) {
                super(view);
                itemView = view;
                ButterKnife.bind(this, view);
            }

        }

    }


}
