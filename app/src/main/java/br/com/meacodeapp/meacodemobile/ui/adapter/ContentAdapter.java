package br.com.meacodeapp.meacodemobile.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.ui.activity.ContentActivity;
import br.com.meacodeapp.meacodemobile.ui.activity.CourseActivity;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> implements Filterable {

    List<Content> contents;

    public ContentAdapter(List<Content> contents){
        this.contents = contents;
    }

    public static class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        Content content;

        public ContentHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.content_adapter_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ContentActivity.class);
            intent.putExtra("content", JsonConverter.toJson(content));
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();

                List<Content> filteredContents = contents;

                if(!query.isEmpty()){
                    for(Content content: contents){
                        if(content.getTitle().toLowerCase().contains(query.toLowerCase())){
                            filteredContents.add(content);
                        }
                    }
                }

                FilterResults filteredResults = new FilterResults();
                filteredResults.values = filteredContents;

                return filteredResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, int position) {
        holder.name.setText((position + 1) + " - " + contents.get(position).getTitle());
        holder.content = contents.get(position);
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_content, parent, false);
        return new ContentHolder(view);
    }
}
