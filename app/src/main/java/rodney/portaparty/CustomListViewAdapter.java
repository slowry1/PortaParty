package rodney.portaparty;

/**
 * Created by rodney on 4/3/2016.
 */
        import android.content.Context;
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
    private TextView potluckNameTextView;


    public CustomListViewAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        myContext = context;
        buildingArray = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_row, null);
            potluckItemTextView = (TextView) view.findViewById(R.id.potLuckItem);
           // potluckNameTextView = (TextView) view.findViewById(R.id.potLuckName);

            HashMap<String, String> myHashMap = new HashMap<>();
            myHashMap = buildingArray.get(position);
            potluckItemTextView.setText(myHashMap.get("item"));
            //potluckNameTextView.setText(myHashMap.get("username"));

            //        iconImage.setImageDrawable(myContext.getResources().getDrawable(R.drawable.ic_perm_media));
            //        expandImage.setImageDrawable(myContext.getResources().getDrawable(R.drawable.ic_navigate_next_black));
        }
        return view;
    }
}
