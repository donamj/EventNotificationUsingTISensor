package thegirlsteam.whatshappening.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import thegirlsteam.whatshappening.tabs.Lecture;
import thegirlsteam.whatshappening.R;


/**
 * Created by Dona Maria on 12/1/2016.
 * List adapter to display the lectures. Lecture titles are displayed in the list.
 */

public class LectureListAdapter extends ArrayAdapter<Lecture> {
    private List<Lecture> lectures;
    private LayoutInflater layoutInflater;



    public LectureListAdapter(Context context, int resource, List<Lecture> lectures) {

        super(context, resource);
        this.lectures = lectures;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lectures.size();
    }

    @Nullable
    @Override
    public Lecture getItem(int position) {
        return lectures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LectureListAdapter.ViewHolder viewHolder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.lecture_list_layout,null);
            viewHolder=new LectureListAdapter.ViewHolder();
            viewHolder.course = (TextView)convertView.findViewById(R.id.lecture_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (LectureListAdapter.ViewHolder)convertView.getTag();
        }

        System.out.println("View of lecture : "+ lectures.get(position).getCourse() );
        viewHolder.course.setText(lectures.get(position).getCourse());




        return convertView;
    }

    class ViewHolder{
        TextView course;


    }

}
