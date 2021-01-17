package io.vov.vitamio.demo;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vov.vitamio.utils.Log;

public class XspfActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getContentResolver();

        setListAdapter(new SimpleAdapter(this, getData(), android.R.layout.simple_list_item_1, new String[]{"title"}, new int[]{android.R.id.text1}));
    }

    protected List<Map<String, String>> getData() {
        List<Map<String, String>> myData = new ArrayList<Map<String, String>>();
        XmlPullParser pullParser = Xml.newPullParser();
        XspfData xspfData = null;
        try {
            pullParser.setInput(getResources().openRawResource(R.raw.hsck),"UTF-8");
            while (pullParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (pullParser.getEventType() == XmlPullParser.START_TAG) {
                    if (pullParser.getName().equals("track")) {
                        xspfData = new XspfData();
                    }
                    if (pullParser.getName().equals("title")&&xspfData!=null) {
                        xspfData.setTitle(pullParser.nextText());
                    }
                    if (pullParser.getName().equals("location")&&xspfData!=null) {
                        xspfData.setUrl(pullParser.nextText());
                    }
                } else if (pullParser.getEventType() == XmlPullParser.END_TAG) {
                    if (pullParser.getName().equals("track")) {
                        addItem(myData,xspfData.getTitle(), xspfData.getUrl());
                        xspfData = null;
                    }
                }
                pullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myData;
    }

    protected void addItem(List<Map<String, String>> data, String name, String intent) {
        Map<String, String> temp = new HashMap<>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        String url = (String) map.get("intent");
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("xspf", url));
        Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, VideoViewDemo.class);
//        intent.putExtra("url", url);
//        startActivity(intent);
    }
}
