package quocb14005xx.thigiacmaytinh.HocOpenCV.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

import quocb14005xx.thigiacmaytinh.HocOpenCV.R;
import quocb14005xx.thigiacmaytinh.HocOpenCV.adapter.GridXemAnh_Adapter;
import quocb14005xx.thigiacmaytinh.HocOpenCV.object.MyContants;

public class ThuVienAnh_Activity extends AppCompatActivity {
    GridView grid;
    GridXemAnh_Adapter adapter;
    File file;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thu_vien_anh_);

        //khoi tao path chua cac image
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            file = new File(MyContants.PATH_IMAGE);
            file.mkdirs();
        }
        //neu buoc 1 ok thi load cac ten file va ten duong dan
        if (file.isDirectory()) {
            listFile = file.listFiles();

            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // file image
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // file name image
                FileNameStrings[i] = listFile[i].getName();
            }
        }
        grid = (GridView) findViewById(R.id.gridview);
        adapter = new GridXemAnh_Adapter(this, FilePathStrings, FileNameStrings);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }
}
