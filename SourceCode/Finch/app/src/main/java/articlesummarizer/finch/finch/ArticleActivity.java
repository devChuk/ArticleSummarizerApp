package articlesummarizer.finch.finch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleActivity extends Activity{

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        intent = getIntent();
        String message = intent.getStringExtra(MainActivity.TITLE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(message);
    }
}
