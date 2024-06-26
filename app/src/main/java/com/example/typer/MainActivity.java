package com.example.typer;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Initialize WebView
        webView = findViewById(R.id.web_view);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Enable cookies
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        // Sync cookies with the default browser
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

        // Load Bing.com
        webView.loadUrl("https://www.bing.com");

        // Set WebViewClient to handle URL loading within the app
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals("https://www.bing.com/")) {
                    // Simulate random text input after the page has loaded
                    new Handler().postDelayed(MainActivity.this::simulateRandomTextInput, 2000);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // Load URL in the WebView
                return true;
            }
        });
    }

    private void simulateRandomTextInput() {
        // Define the number of searches
        int numberOfSearches = 23;

        // Define the delay between searches (in milliseconds)
        long delayBetweenSearches = 10000;

        // Start the loop for multiple searches
        new Thread(() -> {
            for (int i = 0; i < numberOfSearches; i++) {
                // Generate random text for each search
                String randomText = generateRandomText(5);

                // Inject JavaScript to input the text and trigger the search
                String jsCode = "document.getElementById('sb_form_q').value = '" + randomText + "';" +
                        "document.getElementById('sb_form_q').focus();" +
                        "var event = new KeyboardEvent('keydown', { 'keyCode': 13, 'which': 13 });" +
                        "document.getElementById('sb_form_q').dispatchEvent(event);";

                // Evaluate the JavaScript code in the WebView
                webView.postDelayed(() -> webView.evaluateJavascript(jsCode, null), i * delayBetweenSearches);
            }
        }).start();
    }

    private String generateRandomText(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }
}
