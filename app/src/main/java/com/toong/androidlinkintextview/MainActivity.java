package com.toong.androidlinkintextview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.myText);
        textView.setLinkTextColor(
                Color.BLUE); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan normalLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Normal Link", Toast.LENGTH_SHORT).show();
            }
        };

        ClickableSpan noUnderLineClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "NoUnderLine Link", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.MAGENTA); // specific color for this link
            }
        };

        ClickableSpan highlightClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Highlight Link", Toast.LENGTH_SHORT)
                        .show();
                view.invalidate();
                Log.i("TAG", "onClick");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                if (textView.isPressed() && textView.getSelectionStart() != -1 && textView.getText()
                        .toString()
                        .substring(textView.getSelectionStart(), textView.getSelectionEnd())
                        .equals("Highlight Link")) {
                    ds.setColor(Color.RED);
                    textView.invalidate();
                } else {
                    ds.setColor(Color.GREEN);
                }
            }
        };

        makeLinks(textView, new String[] {
                "Normal Link", "NoUnderLine Link", "Highlight Link"
        }, new ClickableSpan[] {
                normalLinkClickSpan, noUnderLineClickSpan, highlightClickSpan
        });
    }

    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }
}
