package articlesummarizer.finch.finch;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ArticleActivity extends Activity {

    Intent intent;
    TextView title, info, date, summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        title = (TextView) findViewById(R.id.title);
        info = (TextView) findViewById(R.id.info);
        date = (TextView) findViewById(R.id.date);
        summary = (TextView) findViewById(R.id.summary);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ChaparralPro-Regular.otf");

        title.setTypeface(tf);
        info.setTypeface(tf);
        date.setTypeface(tf);
        summary.setTypeface(tf);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        intent = getIntent();
        title.setText(intent.getStringExtra(MainActivity.TITLE));

        String author = intent.getStringExtra(MainActivity.AUTHOR);
        String url = intent.getStringExtra(MainActivity.URL);
        info.setText(author + ((!author.equals("") && !url.equals("")) ? " | " : "") + url);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = intent.getStringExtra(MainActivity.PUBLICATIONDATE);
        try {
            if (dateString.contains("T")) {
                dateString = dateString.substring(0, dateString.indexOf("T"));
                Date _date = dateFormat.parse(dateString);
                Calendar cal = Calendar.getInstance();
                cal.setTime(_date);
                date.setText(new SimpleDateFormat("MMMM d yyyy").format(cal.getTime()));
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }

        summary.setText(intent.getStringExtra(MainActivity.SUMMARY));
    }

    public void oninfoClick(View view) {
        Log.d("Redirecting to:", intent.getStringExtra(MainActivity.URL));
        Intent share = new Intent(Intent.ACTION_VIEW);
        share.setData(Uri.parse(intent.getStringExtra(MainActivity.URL)));
        startActivity(share);
    }
}
