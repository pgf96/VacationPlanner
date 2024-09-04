package com.example.vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplanner.Entities.Excursion;
import com.example.vacationplanner.R;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;
    private String vacationStartDate;
    private String vacationEndDate;


    public class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("excursionID", current.getExcursionID());
                    intent.putExtra("excursionTitle", current.getExcursionTitle());
                    intent.putExtra("excursionDate", current.getDate());
                    intent.putExtra("vacationStartDate", vacationStartDate);
                    intent.putExtra("vacationEndDate", vacationEndDate);
                    context.startActivity(intent);
                }
            });
        }
    }

    public ExcursionAdapter(Context context, String vacationStartDate, String vacationEndDate) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
    }

    @NonNull
    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
        if (mExcursions != null) {
            Excursion current = mExcursions.get(position);
            holder.excursionItemView.setText(current.getExcursionTitle());
            holder.excursionItemView2.setText(current.getDate());
        } else {
            holder.excursionItemView.setText("No excursion title");
            holder.excursionItemView2.setText("No vacation id");
        }
    }

    public void setExcursions(List<Excursion> excursions) {
        this.mExcursions = excursions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mExcursions != null) {
            return mExcursions.size();
        } else {
            return 0;
        }
    }
}
