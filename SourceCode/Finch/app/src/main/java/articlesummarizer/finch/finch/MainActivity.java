package articlesummarizer.finch.finch;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private ListAdapter articleListAdapter;
    private ArticleListSQLHelper articleListSQLHelper;

    EditText urlInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateTodoList();

        urlInput = (EditText) findViewById(R.id.editText);
    }

    //update the article list UI
    private void updateTodoList() {
        articleListSQLHelper = new ArticleListSQLHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = articleListSQLHelper.getReadableDatabase();

        //cursor to read article list from database
        Cursor cursor = sqLiteDatabase.query(ArticleListSQLHelper.TABLE_NAME,
                new String[]{ArticleListSQLHelper._ID, ArticleListSQLHelper.COL1_TASK},
                null, null, null, null, null);

        articleListAdapter = new SimpleCursorAdapter(
                this, R.layout.list_item,
                cursor,
                new String[]{ArticleListSQLHelper.COL1_TASK},
                new int[]{R.id.title},
                0
        );

        this.setListAdapter(articleListAdapter);
    }

    public void onSummarize(View view) {
        String urlStringInput = urlInput.getText().toString();
        articleListSQLHelper = new ArticleListSQLHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = articleListSQLHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        //write the article input into database table
        values.put(ArticleListSQLHelper.COL1_TASK, urlStringInput);
        sqLiteDatabase.insertWithOnConflict(ArticleListSQLHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        //update the article list UI
        updateTodoList();
    }

    public void onDelete(View view) {
        View v = (View) view.getParent();
        TextView titleTV = (TextView) v.findViewById(R.id.title);
        String title = titleTV.getText().toString();

        String deleteArticleItemSql = "DELETE FROM " + ArticleListSQLHelper.TABLE_NAME +
                " WHERE " + ArticleListSQLHelper.COL1_TASK + " = '" + title + "'";

        articleListSQLHelper = new ArticleListSQLHelper(MainActivity.this);
        SQLiteDatabase sqlDB = articleListSQLHelper.getWritableDatabase();
        sqlDB.execSQL(deleteArticleItemSql);
        updateTodoList();
    }
}
