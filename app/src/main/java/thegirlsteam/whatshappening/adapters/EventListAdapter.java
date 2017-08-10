package thegirlsteam.whatshappening.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import thegirlsteam.whatshappening.tabs.Event;
import thegirlsteam.whatshappening.R;


/**
 * Created by Dona Maria on 12/1/2016.
 * List adapter to display the events. Event titles are displayed in the list
 */

public class EventListAdapter extends ArrayAdapter<Event> {
    private List<Event> events;
    private LayoutInflater layoutInflater;


    public EventListAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        this.events = events;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fresco.initialize(getContext());
        final ViewHolder viewHolder;

        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.event_list_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.eventName = (TextView)convertView.findViewById(R.id.event_name);
            viewHolder.image = (SimpleDraweeView) convertView.findViewById(R.id.eventImage);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.eventName.setText(events.get(position).getEventName());
        Uri imageUri = Uri.parse(events.get(position).getImageUrl());
        viewHolder.image.setImageURI(imageUri);



        return convertView;
    }

    class ViewHolder{
        TextView eventName;
        SimpleDraweeView image;
    }
}
