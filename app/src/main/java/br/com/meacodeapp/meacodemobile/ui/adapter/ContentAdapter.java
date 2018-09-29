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
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.ui.activity.CourseActivity;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> implements Filterable {

    Context context;
    List<Course> courses;

    public ContentAdapter(Context context, List<Course> courses){
        this.context = context;
        this.courses = courses;
    }

    public static class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        Context context;

        public ContentHolder(View itemView, Context context){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.course_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), CourseActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();

                List<Course> filteredCourses = courses;

                if(!query.isEmpty()){
                    for(Course course: courses){
                        if(course.getName().toLowerCase().contains(query.toLowerCase())){
                            filteredCourses.add(course);
                        }
                    }
                }

                FilterResults filteredResults = new FilterResults();
                filteredResults.values = filteredCourses;

                return filteredResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, int position) {
        holder.name.setText(courses.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_course, parent, false);
        return new ContentHolder(view, parent.getContext());
    }
}
