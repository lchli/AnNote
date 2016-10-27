package com.lchli.studydiscuss.bm.note.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.common.base.AbsAdapter;
import com.lchli.studydiscuss.common.consts.SeverConst;
import com.lchli.studydiscuss.common.widget.ListPopup;
import com.lchli.studydiscuss.bm.note.entity.SortData;

import java.util.ArrayList;
import java.util.List;

import com.lchli.studydiscuss.common.utils.ContextProvider;
import com.lchli.studydiscuss.common.utils.ResUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.inflate;

/**
 * Created by lchli on 2016/8/16.
 */

public class SortPopupWraper {

    private final ListPopup sortPop;

    public SortPopupWraper(Context context) {
        sortPop = new ListPopup(context);
        sortPop.getListView().setDivider(new ColorDrawable(ResUtils.parseColor(R.color.list_divider_color)));
        sortPop.getListView().setDividerHeight(ResUtils.parseDimenPix(R.dimen.list_divider_height));
        SortAdapter sortAdapter = new SortAdapter();
        sortPop.setAdapter(sortAdapter);
        sortAdapter.refresh(buildSortDatas());

    }

    private List<SortData> buildSortDatas() {
        List<SortData> datas = new ArrayList<>();
        datas.add(new SortData(SeverConst.Note.SortBy_TimeAsc, ResUtils.parseString(R.string.sort_time_asc)));
        datas.add(new SortData(SeverConst.Note.SortBy_TimeDesc, ResUtils.parseString(R.string.sort_time_desc)));
        return datas;

    }

    public ListPopup getPopup() {
        return sortPop;
    }


    class SortAdapter extends AbsAdapter<SortData> {


        @Override
        public SortAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(viewType);
        }

        @Override
        public void onBindViewHolder(AbsViewHolder vh, int position) {
            ViewHolder holder = (ViewHolder) vh;
            final SortData data = getItem(position);
            holder.text_widget.setText(data.text);

        }

        class ViewHolder extends AbsViewHolder {
            @Bind(R.id.text_widget)
            TextView text_widget;
            View view;

            public ViewHolder(int viewType) {
                super(viewType);
                view = inflate(ContextProvider.context(), R.layout.list_item_note_sort, null);
                ButterKnife.bind(this, view);
            }


            @Override
            protected View getItemView() {
                return view;
            }
        }
    }
}
