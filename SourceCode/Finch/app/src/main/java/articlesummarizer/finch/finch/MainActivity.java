package articlesummarizer.finch.finch;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ListActivity {

    private ListAdapter articleListAdapter;
    private ArticleListSQLHelper articleListSQLHelper;

    RequestQueue queue;
    // Tag used to log messages
    private static final String TAG = MainActivity.class.getSimpleName();

    EditText urlInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateTodoList();

        // Setup Volley networking request
        queue = Volley.newRequestQueue(this); // Need to set up a queue that holds all Volley requests

        urlInput = (EditText) findViewById(R.id.editText);
    }

    //update the article list UI
    private void updateTodoList() {
        articleListSQLHelper = new ArticleListSQLHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = articleListSQLHelper.getReadableDatabase();

        //cursor to read article list from database
        Cursor cursor = sqLiteDatabase.query(ArticleListSQLHelper.TABLE_NAME,
                new String[]{ArticleListSQLHelper._ID, ArticleListSQLHelper.COL1_TASK, ArticleListSQLHelper.COL2_TASK},
                null, null, null, null, null);

        articleListAdapter = new SimpleCursorAdapter(
                this, R.layout.list_item,
                cursor,
                new String[]{ArticleListSQLHelper.COL1_TASK, ArticleListSQLHelper.COL2_TASK},
                new int[]{R.id.title, R.id.subtitle},
                0
        );

        this.setListAdapter(articleListAdapter);
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

    public void onSummarize(View view) {
        final String urlStringInput = urlInput.getText().toString();

        StringRequest extractReq = new StringRequest(Request.Method.POST,"https://api.aylien.com/api/v1/extract",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("extractReq", obj.toString());


                        articleListSQLHelper = new ArticleListSQLHelper(MainActivity.this);
                        SQLiteDatabase sqLiteDatabase = articleListSQLHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.clear();

                        //write the article input into database table
                        values.put(ArticleListSQLHelper.COL1_TASK, obj.get("title").toString());
                        values.put(ArticleListSQLHelper.COL2_TASK, urlStringInput);
                        values.put(ArticleListSQLHelper.COL3_TASK, obj.get("author").toString());
                        values.put(ArticleListSQLHelper.COL4_TASK, obj.get("publishDate").toString());
                        values.put(ArticleListSQLHelper.COL5_TASK, "ayy lmao");

                        sqLiteDatabase.insertWithOnConflict(ArticleListSQLHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                        //update the article list UI
                        updateTodoList();

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.toString(), error);
                }
            }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("url", urlStringInput); //"https://medium.com/@zan2434/how-i-learned-to-code-d93260baf219#.6w4wtfch7"
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("X-AYLIEN-TextAPI-Application-Key","ac6f5d725a9c90e8373c77c233f2273e");
                params.put("X-AYLIEN-TextAPI-Application-ID","6432fd32");
                return params;
            }
        };
        queue.add(extractReq);



        /////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////
    }
}
/*
        StringRequest sr = new StringRequest(Request.Method.POST,"https://api.aylien.com/api/v1/summarize",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("My App", obj.toString());
                            textView.setText(obj.get("sentences").toString());
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }

//                        Toast.makeText(
//                                getApplicationContext(),
//                                response,
//                                Toast.LENGTH_SHORT)
//                                .show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString(), error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("sentences_number","20");
                params.put("url",editText.getText().toString()); //"https://medium.com/@zan2434/how-i-learned-to-code-d93260baf219#.6w4wtfch7"

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("X-AYLIEN-TextAPI-Application-Key","ac6f5d725a9c90e8373c77c233f2273e");
                params.put("X-AYLIEN-TextAPI-Application-ID","6432fd32");
                return params;
            }
        };
        queue.add(sr);
 */