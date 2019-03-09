package com.example.blog.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blog.Model.Blog;
import com.example.blog.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_row, viewGroup , false);

        return new ViewHolder(view , context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        Blog blog = blogList.get(i);
        String imageUrl = null;
        holder.title.setText(blog.getTitle());
        holder.description.setText(blog.getDescription());


        java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
        String formattedDate =   dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
        //April 17 2017
        holder.timestamp.setText(blog.getTimestamp());
        imageUrl = blog.getImage();

        //TODO: Use picasso library to load  image

        Picasso.with(context)
                .load(imageUrl)
                .into(holder.image);




    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,description,timestamp;
        public ImageView image;
        public String userid;

        public ViewHolder(@NonNull View itemView , Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.postTitleList);
            description = (TextView)itemView.findViewById(R.id.postTextList);
            image = (ImageView)itemView.findViewById(R.id.postImageList);
            timestamp = (TextView)itemView.findViewById(R.id.timeStampList);

            userid = null;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next Activity...........

                }
            });

        }
    }
}
