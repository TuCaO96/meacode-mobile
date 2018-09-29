package br.com.meacodeapp.meacodemobile.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.ui.activity.CourseActivity;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> implements Filterable {

    Context context;
    List<Course> courses;

    public CourseAdapter(Context context, List<Course> courses){
        this.context = context;
        this.courses = courses;
    }

    public static class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView image;
        Course course;
        Context context;

        public CourseHolder(View itemView, Context context){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.course_name);
            image = (ImageView) itemView.findViewById(R.id.course_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), CourseActivity.class);
            intent.putExtra("course", JsonConverter.toJson(course));
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
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        holder.name.setText(courses.get(position).getName());
        holder.course = courses.get(position);
//        Glide.with(context).load(courses.get(position).getImage().getUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_course, parent, false);
        return new CourseHolder(view, parent.getContext());
    }
}
