package articlesummarizer.finch.finch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleActivity extends Activity{

    Intent intent;
    TextView title, info, date, summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        title = (TextView) findViewById(R.id.title);
        info = (TextView) findViewById(R.id.info); //TODO reformat author & url to account for all cases
        date = (TextView) findViewById(R.id.date); //TODO reformat date
        summary = (TextView) findViewById(R.id.summary);

        intent = getIntent();
        title.setText(intent.getStringExtra(MainActivity.TITLE));
        info.setText(intent.getStringExtra(MainActivity.AUTHOR) + "|" + intent.getStringExtra(MainActivity.URL));
        date.setText(intent.getStringExtra(MainActivity.PUBLICATIONDATE));
        summary.setText(intent.getStringExtra(MainActivity.SUMMARY));
    }
}
