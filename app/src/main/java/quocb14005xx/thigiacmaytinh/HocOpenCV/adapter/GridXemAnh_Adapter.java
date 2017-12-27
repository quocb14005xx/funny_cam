package quocb14005xx.thigiacmaytinh.HocOpenCV.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;

/**
 * Created by quocb14005xx on 12/27/2017.
 */

public class GridXemAnh_Adapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    // Declare variables
    private Activity activity;
    private String[] filepath;
    private String[] filename;

    public GridXemAnh_Adapter(Activity a, String[] fpath, String[] fname) {
        activity = a;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return filepath.length;

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
        {
            vi = inflater.inflate(R.layout.gridview_item, null);
        }
        TextView text = (TextView) vi.findViewById(R.id.text);
        ImageView image = (ImageView) vi.findViewById(R.id.image);
        text.setText(filename[position]);

        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        image.setImageBitmap(bmp);
        parent.setBackgroundColor(Color.GRAY);
        return vi;
    }
}
