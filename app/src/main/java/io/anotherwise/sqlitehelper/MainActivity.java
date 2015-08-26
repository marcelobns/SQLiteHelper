package io.anotherwise.sqlitehelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.anotherwise.sqlitehelper.core.Database;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = new HashMap<String, Object>();
                item = (Map<String, Object>)parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                intent.putExtra("id", item.get("id").toString());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListView();
    }

    private void loadListView() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, titulo, descricao, concluida FROM tarefas ORDER BY concluida", null);

        cursor.moveToFirst();
        ArrayList<Map<String, Object>> lista = new ArrayList<>();

        for(int i = 0; i < cursor.getCount(); i++){
            Map<String, Object> item = new HashMap<>();

            item.put("id", cursor.getInt(0));
            item.put("titulo", cursor.getString(1));
            item.put("descricao", cursor.getString(2));
            item.put("concluida", cursor.getInt(3));

            lista.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        String[] de = {"id", "titulo"};
        int[] para = {R.id.tarefa_id, R.id.tarefa_titulo};

        SimpleAdapter adapter = new SimpleAdapter(this, lista,
                R.layout.listview_tarefa, de, para);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_nova) {

            Intent intent = new Intent(this, TarefaActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
