package thegirlsteam.whatshappening.tabs;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

/**
 * Created by Dona Maria on 12/1/2016.
 * Provides sorting functionality to the tabs
 */

public class SortActionProvider extends ActionProvider implements OnMenuItemClickListener{
    private Context mContext;

    public SortActionProvider(Context context) {
        super(context);
        mContext = context;
    }
    @Override
    public View onCreateActionView(){
        return null;
    }
    @Override
    public boolean hasSubMenu(){
        return true;
    }
    @Override
    public void onPrepareSubMenu(SubMenu subMenu){
        subMenu.clear();
        subMenu.add("By distance - Ascending").setOnMenuItemClickListener(this);
        MenuItem item1=subMenu.getItem(0);
        item1.setIcon(android.R.drawable.arrow_up_float);

        subMenu.add("By distance - Descending").setOnMenuItemClickListener(this);
        MenuItem item2=subMenu.getItem(1);
        item2.setIcon(android.R.drawable.arrow_down_float);

        subMenu.add("By time - Ascending").setOnMenuItemClickListener(this);
        MenuItem item3=subMenu.getItem(2);
        item3.setIcon(android.R.drawable.arrow_up_float);

        subMenu.add("By time - Descending").setOnMenuItemClickListener(this);
        MenuItem item4=subMenu.getItem(3);
        item4.setIcon(android.R.drawable.arrow_down_float);


    }
    @Override
    public boolean onMenuItemClick(MenuItem item){

        int tab_id = TabbedActivity.getCurrentTab();
        // tab_id=0 : events
        // tab_id=1 : lectures

        System.out.print("%%%%%%%%%%%%%%%%%%%!!!!!!!!!!!!!!!! O MENU ITEM CLICK !!!!!!!!!!!!!!!!!!!!!!!");
        if(item.getTitle().equals("By distance - Ascending"))
            if (tab_id==0)
                EventsTab.sortList(1,"asc");
            else
                LectureTab.sortList(1,"asc");
        else if(item.getTitle().equals("By distance - Descending"))
            if (tab_id==0)
                EventsTab.sortList(1, "desc");
            else
                LectureTab.sortList(1,"desc");
        else if(item.getTitle().equals("By time - Ascending"))
            if (tab_id==0)
                EventsTab.sortList(2,"asc");
            else
                LectureTab.sortList(2,"asc");
        else if(item.getTitle().equals("By time - Descending"))
            if (tab_id==0)
                EventsTab.sortList(2, "desc");
            else
                LectureTab.sortList(2,"desc");


        System.out.println("ITEM ID ::::::::::::::::::: " + item.getItemId());

        return true;
    }


}
