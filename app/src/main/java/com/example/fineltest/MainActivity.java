package com.example.fineltest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    File[] imageFiles;
    ArrayList<ItemData> ImageList;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_MEDIA_IMAGES}, MODE_PRIVATE);

        ListView listView = (ListView) findViewById(R.id.listView);

        imageFiles = new File("/sdcard/").listFiles();
        ImageList = new ArrayList<>();

        for(File file : imageFiles) {
            String fileName = file.getName();
            String extName = fileName.substring(fileName.length() - 3);
            if(extName.equals("jpg") || extName.equals("png")) {
                ImageList.add(new ItemData("/sdcard/" + fileName));
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트뷰 이벤트 처리
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class); // 현재 액티비티에서 다른 액티비티로 전환
                intent.putExtra("str", ImageList.get(position).getName()); // 리스트내에 저장한 mp3 파일 이름을 다른 엑티비티로 보냄
                startActivity(intent); // 다른 액티비티 시작
            }
        });
        MainAdapter listAdapt = new MainAdapter(getApplicationContext(), ImageList); // 리스트뷰를 위한 어댑터 생성
        listView.setAdapter(listAdapt);
    }

    class MainAdapter extends BaseAdapter {
        private Context conText;
        private ArrayList<ItemData> imageList = new ArrayList<>();

        public MainAdapter(Context context, ArrayList<ItemData> itemdata) {
            this.conText = context;
            this.imageList = itemdata;
        }
        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return imageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) conText.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_layout, parent, false);

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.item1);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.item2);
            ImageView imageView3 = (ImageView) convertView.findViewById(R.id.item3);

            try {
                File file1 = new File(imageList.get(position).getName());
                File file2 = new File(imageList.get(position + 1).getName());
                File file3 = new File(imageList.get(position + 2).getName());

                File[] files = new File(imageList.get(position).getName()).listFiles();

                if(file1.exists() == true) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                    imageView1.setImageBitmap(myBitmap);
                }
                if(file2.exists() == true) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                    imageView2.setImageBitmap(myBitmap);
                }
                if(file3.exists() == true) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file3.getAbsolutePath());
                    imageView3.setImageBitmap(myBitmap);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            position += 3;
            return convertView;
        }
    }
}