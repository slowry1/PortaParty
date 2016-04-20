package rodney.portaparty;

/**
 * Created by rodney on 4/3/2016.
 */
        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.HashMap;

public class CustomListViewAdapter extends BaseAdapter {

    private Context myContext;
    private ArrayList<HashMap<String, String>> buildingArray;
    private static LayoutInflater inflater = null;
    private TextView potluckItemTextView;


    public CustomListViewAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        myContext = context;
        buildingArray = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private static class ViewHolder{
        TextView  potluckItemTextView;
        int position;
    }

    @Override
    public int getCount() {
        return buildingArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_row, null);
            vh.potluckItemTextView = (TextView) convertView.findViewById(R.id.potLuckItem);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> myHashMap;

        myHashMap = buildingArray.get(position);

        vh.potluckItemTextView.setText(myHashMap.get("item"));
        return convertView;
    }
}
