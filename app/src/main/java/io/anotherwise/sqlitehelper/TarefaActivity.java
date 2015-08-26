package io.anotherwise.sqlitehelper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import io.anotherwise.sqlitehelper.core.Database;

public class TarefaActivity extends AppCompatActivity {

    Database database;
    EditText tituloEdit;
    EditText descricaoEdit;
    Switch concluirSwitch;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        tituloEdit = (EditText) findViewById(R.id.tituloEdit);
        descricaoEdit = (EditText) findViewById(R.id.descricaoEdit);
        concluirSwitch = (Switch) findViewById(R.id.concluirSwitch);

        database = new Database(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        if(id != null){
            getTarefa(id);
        }
    }

    private void salvar(){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("titulo", tituloEdit.getText().toString());
        values.put("descricao", descricaoEdit.getText().toString());
        values.put("concluida", concluirSwitch.isChecked());

        long result = -1;

        if(id == null){
            result = db.insert("tarefas", null, values);
        } else {
            String[] param = {id};
            result = db.update("tarefas", values, "id = ?", param);
        }

        if(result == -1){
            toast("Falha ao tentar salvar!");
        } else {
            finish();
        }
    }

    private void delete(){
        SQLiteDatabase db = database.getWritableDatabase();

        long result = -1;

        if(id != null) {
            String[] param = {id};
            result = db.delete("tarefas", "id = ?", param);
        } else {
            finish();
        }

        if(result == -1){
            toast("Falha ao tentar Deletar!");
        } else {
            finish();
        }
    }

    private void getTarefa(String id) {
        SQLiteDatabase db = database.getReadableDatabase();
        String[] param = {id};
        Cursor cursor = db.rawQuery(
                "SELECT titulo, descricao, concluida FROM tarefas WHERE id = ?;", param);

        cursor.moveToFirst();

        tituloEdit.setText(cursor.getString(0));
        descricaoEdit.setText(cursor.getString(1));
        concluirSwitch.setChecked(cursor.getInt(2) != 0);

        cursor.close();
        database.close();
    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tarefa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_salvar) {
            salvar();
            return true;
        }
        if (id == R.id.action_delete){
            delete();
        }

        return super.onOptionsItemSelected(item);
    }
}
