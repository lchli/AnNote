package com.lchli.studydiscuss.bm.note.frament;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchli.pinedrecyclerlistview.library.ListSectionData;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerAdapter;
import com.lchli.pinedrecyclerlistview.library.pinnedRecyclerView.PinnedRecyclerView;
import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.StudyApp;
import com.lchli.studydiscuss.bm.note.activity.EditNoteActivity;
import com.lchli.studydiscuss.bm.note.activity.LocalNoteDetailActivity;
import com.lchli.studydiscuss.bm.note.busEvent.CloudNoteListChangedEvent;
import com.lchli.studydiscuss.bm.note.busEvent.LocalNoteListChangedEvent;
import com.lchli.studydiscuss.bm.note.entity.Note;
import com.lchli.studydiscuss.bm.note.entity.NoteDao;
import com.lchli.studydiscuss.bm.note.entity.NoteFilterData;
import com.lchli.studydiscuss.bm.note.entity.SortData;
import com.lchli.studydiscuss.bm.note.widget.GridDividerDecoration;
import com.lchli.studydiscuss.bm.note.widget.NoteFilterPopup;
import com.lchli.studydiscuss.bm.note.widget.SortPopupWraper;
import com.lchli.studydiscuss.bm.user.entity.User;
import com.lchli.studydiscuss.common.base.BaseFragment;
import com.lchli.studydiscuss.common.base.BaseResponse;
import com.lchli.studydiscuss.common.consts.SeverConst;
import com.lchli.studydiscuss.common.consts.UrlConst;
import com.lchli.studydiscuss.common.networkLib.AppHttpManager;
import com.lchli.studydiscuss.common.networkLib.OkError;
import com.lchli.studydiscuss.common.networkLib.OkErrorCode;
import com.lchli.studydiscuss.common.networkLib.OkUiCallback;
import com.lchli.studydiscuss.common.utils.AppListItemAnimatorUtils;
import com.lchli.studydiscuss.common.utils.ContextProvider;
import com.lchli.studydiscuss.common.utils.EventBusUtils;
import com.lchli.studydiscuss.common.utils.ListUtils;
import com.lchli.studydiscuss.common.utils.MapUtils;
import com.lchli.studydiscuss.common.utils.ResUtils;
import com.lchli.studydiscuss.common.utils.ToastUtils;
import com.lchli.studydiscuss.common.widget.CommonEmptyView;
import com.lchli.studydiscuss.common.widget.CommonTitleView;
import com.lchli.studydiscuss.common.widget.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.io.FileUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.lchli.studydiscuss.bm.user.model.UserSessionManager.getSession;

/**
 * Created by lchli on 2016/8/10.
 */
public class LocalNoteFragment extends BaseFragment {


    @BindView(R2.id.common_title)
    CommonTitleView commonTitle;
    @BindView(R2.id.moduleListRecyclerView)
    PinnedRecyclerView moduleListRecyclerView;
    @BindView(R2.id.fab)
    FloatingActionButton fab;
    @BindView(R2.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R2.id.empty_widget)
    CommonEmptyView emptyWidget;

    private LocalNoteListAdapter mNotesAdapter;
    private TextView emptyTextView;
    private SortPopupWraper mSortPopup;
    private SortData currentSortData = new SortData(SeverConst.Note.SortBy_TimeAsc, "");
    private NoteFilterPopup mNoteFilterPopup;
    private NoteFilterData currentFilterData = new NoteFilterData("", "");
    private LoadingDialog mLoadingDialog = new LoadingDialog();


    public static LocalNoteFragment newInstance() {
        Bundle args = new Bundle();
        LocalNoteFragment fragment = new LocalNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_note_list, container, false);
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
                            currentFilterData = data;
                            loadLocalNotes();
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
                            loadLocalNotes();

                        }
                    });
                }
                mSortPopup.getPopup().showAsDropDown(commonTitle);
            }
        });
        commonTitle.setCenterText(ResUtils.parseString(R.string.note), null);

        emptyTextView = emptyWidget.addEmptyText("");

        moduleListRecyclerView.setHasFixedSize(true);
        moduleListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        moduleListRecyclerView.addItemDecoration(new GridDividerDecoration(moduleListRecyclerView.getContext(), GridDividerDecoration.VERTICAL_LIST));
        mNotesAdapter = new LocalNoteListAdapter();
        moduleListRecyclerView.setPinnedAdapter(mNotesAdapter);

        loadLocalNotes();
    }


    static class HeadData {

    }

    static class PinedData extends ListSectionData {

        String noteType;

        public PinedData(int viewType, String noteType) {
            super(viewType);
            this.noteType = noteType;
        }
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

    private void loadLocalNotes() {
        new AsyncTask<Void, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(Void... params) {
                QueryBuilder<Note> queryBuilder =  StudyApp.instance().getDaoSession().getNoteDao().queryBuilder();

                if (!TextUtils.isEmpty(currentFilterData.title)) {
                    queryBuilder.where(NoteDao.Properties.Title.like("%" + currentFilterData.title + "%"));
                }
                if (!TextUtils.isEmpty(currentFilterData.tag)) {
                    queryBuilder.where(NoteDao.Properties.Type.eq(currentFilterData.tag));
                }

                if (currentSortData.code == SeverConst.Note.SortBy_TimeAsc) {
                    queryBuilder.orderAsc(NoteDao.Properties.Type).orderAsc(NoteDao.Properties.LastModifyTime);
                } else {
                    queryBuilder.orderAsc(NoteDao.Properties.Type).orderDesc(NoteDao.Properties.LastModifyTime);
                }
                List<Object> all = new ArrayList<>();
                all.add(new HeadData());

                List<Note> notes = queryBuilder.build().list();
                if (notes == null || notes.isEmpty()) {
                    return all;
                }
                String preType = "";
                for (int i = 0; i < notes.size(); i++) {
                    String currentType = notes.get(i).type;
                    if (!preType.equals(currentType)) {
                        all.add(new PinedData(LocalNoteListAdapter.VIEW_TYPE_PINED, currentType));
                        preType = currentType;
                    }
                    all.add(notes.get(i));
                }
                return all;
            }

            @Override
            protected void onPostExecute(List<Object> notes) {
                if (ListUtils.isEmpty(notes)) {
                    showEmpty(getString(R.string.empty_data));
                } else {
                    hideEmpty();
                    mNotesAdapter.refresh(notes);
                }
            }
        }.execute();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusUtils.unregister(this);
    }

    @OnClick(R2.id.fab)
    public void onClick() {
        EditNoteActivity.startSelf(getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocalNoteListChangedEvent e) {
        loadLocalNotes();
    }


    class LocalNoteListAdapter extends PinnedRecyclerAdapter {

        private final Bitmap def = BitmapFactory.decodeResource(ContextProvider.context().getResources(),
                R.drawable.ic_gf_default_photo);

        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_ITEM = 1;
        private static final int VIEW_TYPE_PINED = 2;


        @Override
        public int getItemViewType(int position) {
            Object object = mDatas.get(position);
            if (object instanceof ListSectionData) {
                ListSectionData pinedViewData = (ListSectionData) object;
                return pinedViewData.sectionViewType;
            }
            if (object instanceof HeadData) {
                return VIEW_TYPE_HEADER;
            }
            return VIEW_TYPE_ITEM;


        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    view = LayoutInflater.from(getActivity())
                            .inflate(R.layout.local_note_list_header, parent, false);
                    return new HeaderViewHolder(view);
                case VIEW_TYPE_PINED:
                    view = LayoutInflater.from(getActivity())
                            .inflate(R.layout.local_note_list_pined_item, parent, false);
                    return new PinedViewHolder(view);

                default:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.local_note_list_item, parent, false);
                    return new ViewHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder h, int position) {
            int viewtype = getItemViewType(position);
            Object o = getItem(position);

            if (viewtype == VIEW_TYPE_PINED) {
                PinedViewHolder holder = (PinedViewHolder) h;
                PinedData pinedData = (PinedData) o;
                holder.pinedHeader.setText(pinedData.noteType + "");
                return;
            }

            if (viewtype == VIEW_TYPE_HEADER) {
                // HeaderViewHolder holder = (HeaderViewHolder) h;
                return;
            }

            ViewHolder holder = (ViewHolder) h;
            final Note data = (Note) o;
            holder.couseTitleTextView.setText(data.title);
            holder.courseTimeTextView.setText(data.lastModifyTime);
            if (!isScrolling) {
                Glide.with(holder.itemView.getContext()).load(data.imagesDir + "/" + data.thumbNail).into(holder.courseThumbImageView);
            } else {
                holder.courseThumbImageView.setImageBitmap(def);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalNoteDetailActivity.startSelf(v.getContext(), data);
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
                                     StudyApp.instance().getDaoSession().getNoteDao().deleteByKey(data.uid);
                                    FileUtils.deleteQuietly(new File(data.imagesDir));
                                    EventBusUtils.post(new LocalNoteListChangedEvent());
                                    sweetAlertDialog.dismissWithAnimation();
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

            holder.courseUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User session = getSession();
                    if (session == null) {
                        ToastUtils.systemToast(R.string.not_login);
                        return;
                    }

                    Map<String, String> params = MapUtils.stringMap();
                    params.put("Uid", data.uid);
                    params.put("Title", data.title);
                    params.put("UserId", session.uid);
                    params.put("ImagesDir", data.imagesDir);
                    params.put("LastModifyTime", data.lastModifyTime);
                    params.put("Type", data.type);
                    params.put("ThumbNail", data.thumbNail);
                    params.put("Content", data.content);

                    File[] files = new File(data.imagesDir).listFiles();
                    List<File> fileList = null;
                    if (files != null && files.length != 0) {
                        fileList = Arrays.asList(files);
                    }

                    mLoadingDialog.show(ResUtils.parseString(R.string.loading_msg), getActivity());
                    AppHttpManager.uploadFile(UrlConst.UPLOAD_NOTE_URL, params, fileList, "files", new OkUiCallback<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse response) {
                            mLoadingDialog.dismiss();
                            if (response.code == OkErrorCode.SUCCESS) {
                                ToastUtils.systemToast(ResUtils.parseString(R.string.upload_note_success));
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
            });

            AppListItemAnimatorUtils.startAnim(holder.itemView);
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R2.id.course_thumb_imageView)
            ImageView courseThumbImageView;
            @BindView(R2.id.couse_title_textView)
            TextView couseTitleTextView;
            @BindView(R2.id.course_time_textView)
            TextView courseTimeTextView;
            @BindView(R2.id.course_upload)
            TextView courseUpload;

            public View itemView;

            public ViewHolder(View view) {
                super(view);
                itemView = view;
                ButterKnife.bind(this, view);
            }

        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {

            public View itemView;

            public HeaderViewHolder(View view) {
                super(view);
                itemView = view;
                ButterKnife.bind(this, view);
            }

        }

        class PinedViewHolder extends RecyclerView.ViewHolder {

            public View itemView;
            @BindView(R2.id.pinedHeader)
            TextView pinedHeader;

            public PinedViewHolder(View view) {
                super(view);
                itemView = view;
                ButterKnife.bind(this, view);
            }

        }

    }


}
