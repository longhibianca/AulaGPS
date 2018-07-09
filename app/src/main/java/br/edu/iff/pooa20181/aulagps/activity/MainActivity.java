package br.edu.iff.pooa20181.aulagps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.edu.iff.pooa20181.aulagps.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String[] activities = {"LocalizaActivity"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities);

        ListView listView = (ListView) findViewById(R.id.listaActivity);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = null;

        try {

            Class obj = Class.forName ("br.edu.iff.pooa20181.aulagps.activity."+activities[position]);


            intent = new Intent(this, obj);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        startActivity(intent);
    }
}
