package com.sunrun.sunrunframwork.uibase;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sunrun.sunrunframwork.R;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.log.Logger;
import com.sunrun.sunrunframwork.view.title.BaseTitleLayoutView;

import static com.sunrun.sunrunframwork.uiutils.UIUtils.cancelLoadDialog;


/**
 * @展示简单web页面的Activity
 */
public class DetailsForWebActivity extends BaseActivity {
    String url = null;
    WebView web;
    BaseTitleLayoutView titlebar;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.ui_details_fragment);
        web = (WebView) findViewById(R.id.web);
        titlebar =(BaseTitleLayoutView) findViewById(R.id.titlebar);
        super.onCreate(arg0);

    }

    public static void turnUrl(Activity act, String title, String url) {
        Intent intent = new Intent(act, DetailsForWebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        act.startActivity(intent);
    }

    private void showWeb() {

//		UiUtils.showLoadDialog(this, getString(R.string.loading_msg));
        UIUtils.showLoadDialog(this, "加载中");
        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.getSettings().setJavaScriptEnabled(true);
        // web.getSettings().setDisplayZoomControls(false);
        web.getSettings().setAllowFileAccess(true);
        // 设置支持缩放
        web.getSettings().setBuiltInZoomControls(true);
        web.loadUrl(url);
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title != null && !"".equals(title)) {
                    titlebar.setTitleText(title);
                }
            }
        });
        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.E(url);
                if (String.valueOf(url).startsWith("http")) {//
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                cancelLoadDialog();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
                // super.onReceivedSslError(view, handler, error);
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                UIUtils.shortM("证书错误!");
                handler.proceed();// 接收所有网站证书,忽略SSL错误提示
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                UIUtils.shortM("加载失败!");
                super.onReceivedError(view, errorCode, description, failingUrl);
                cancelLoadDialog();
            }
        });
    }

    @Override
    protected void onBack() {
        if (web.canGoBack()) { // 表示按返回键
            // 时的操作
            web.goBack(); // 后退
            return;
        }
        super.onBack();
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) { // 表示按返回键
            // 时的操作
            web.goBack(); // 后退
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void initView() {
        titlebar.setTitleText(getIntent().getStringExtra("title"));
        url = getIntent().getStringExtra("url");
        Logger.D("加载 " + url);
        showWeb();
    }

}
