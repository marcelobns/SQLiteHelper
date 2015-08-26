package io.anotherwise.sqlitehelper.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcelobarbosa on 8/26/15.
 */
public class Database extends SQLiteOpenHelper {

    final static String name = "db_name";
    final static int version = 3;

    public Database(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE tarefas ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "titulo TEXT," +
                        "descricao TEXT," +
                        "concluida INTEGER);"
        );

        db.execSQL( "INSERT INTO tarefas (titulo, descricao, concluida) " +
                    "VALUES ('Compras', 'Arroz, feijão e batata frita', 0)");

        db.execSQL( "INSERT INTO tarefas (titulo, descricao, concluida) " +
                    "VALUES ('Exercícios', 'Caminhar 12km', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tarefas");
        onCreate(db);
    }
}
