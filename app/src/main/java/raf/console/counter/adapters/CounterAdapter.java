package raf.console.counter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import raf.console.counter.R;
import raf.console.counter.databinding.CounterItemElementBinding;
import raf.console.counter.domain.models.CounterItem;


public class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.MyViewHolder> {

    private Context context;
    private List<CounterItem> counterList;
    HandleCounterClick clickListener;


    public CounterAdapter(Context context, HandleCounterClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCounterList(List<CounterItem> counterList) {
        this.counterList = counterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.counter_item_element, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.titleId.setText(this.counterList.get(position).title);
        holder.binding.targetId.setText(new StringBuilder()
                        .append("Цель: ")
                .append(this.counterList.get(position).target).toString());
        holder.binding.progressId
                .setText(new StringBuilder()
                        .append("Прогресс: ")
                        .append(this.counterList.get(position).progress)
                        .append("/")
                        .append(this.counterList.get(position).target)
                        .toString());

        holder.itemView.setOnClickListener(v -> {
            clickListener.itemClick(counterList.get(position));
        });

        holder.binding.deleteDBCounterItem.setOnClickListener(v -> {
            clickListener.deleteItem(counterList.get(position));
        });

        holder.binding.editDBCounterItem.setOnClickListener(v -> {
            clickListener.editItem(counterList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (counterList == null || counterList.size() == 0) {
            return 0;
        } else {
            return counterList.size();
        }
    }

    public void setFilteredList(List<CounterItem> filteredList) {
        this.counterList = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CounterItemElementBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CounterItemElementBinding.bind(itemView);
        }
    }

    public interface HandleCounterClick {
        void itemClick(CounterItem counterItem);
        void deleteItem(CounterItem counterItem);
        void editItem(CounterItem counterItem);
    }
}
