package sun.tianyu.ijob.common.adview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.HashMap;

import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonActivity;

/**
 * Created by Developer on 15/07/23.
 */
public class AdWebActivity extends CommonActivity{
    WebView adWebView;
    FrameLayout playerView;

    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mVideoProgressView;

    ProgressDialog pd;
    Handler handler;
    private String termsUrl = "";
    String titleName = "";
    /* 1:Adバナー */
    private int fromType;
    private boolean isTremReloadFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_web);

        // FLAG_HARDWARE_ACCELERATED.
        if (android.os.Build.VERSION.SDK_INT > 10) {
            getWindow().setFlags(0x01000000, 0x01000000);
        }


        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        String strLoadingData = "Loading...";
        pd.setMessage(strLoadingData);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            termsUrl = extras.getString("loadUrl");
            fromType = extras.getInt("wherefrom");
        }
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Thread.currentThread().isInterrupted();
                if (!Thread.currentThread().isInterrupted()) {
                    switch (msg.what) {
                        case 0:
                            pd.show();
                            break;
                        case 1:
                            titleName = adWebView.getTitle();
                            setTitle(titleName);
                            pd.dismiss();
                            break;
                    }
                }
                super.handleMessage(msg);
            }
        };

    }

    public void onResume() {
        super.onResume();

        if (adWebView == null) {
            termsOfServiceInit();
            loadurl(adWebView, termsUrl);
        } else {
            try {
                WebView.class.getMethod("onResume").invoke(adWebView);
            } catch(Exception e) {
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (fromType == 1) {
                adWebView.setInitialScale(100);
            }

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    public void termsOfServiceInit() {
        playerView = (FrameLayout) findViewById(R.id.playerFrame);

        adWebView = (WebView) findViewById(R.id.wv_termsOfService);
        adWebView.getSettings().setDomStorageEnabled(true);// WebStorageは有効になる。重要
        adWebView.getSettings().setJavaScriptEnabled(true);// JS利用できる
        adWebView.getSettings().setSupportZoom(true);
        adWebView.getSettings().setBuiltInZoomControls(true);

        if(fromType ==1 ){
            adWebView.getSettings().setUseWideViewPort(true);
            adWebView.getSettings().setLoadWithOverviewMode(true);
        }
        try {
            // マルチタッチを有効にしたまま、zoom controlを消す
            Field nameField = adWebView.getSettings().getClass()
                    .getDeclaredField("mBuiltInZoomControls");
            nameField.setAccessible(true);
            nameField.set(adWebView.getSettings(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        adWebView.requestFocus();
        adWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        adWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (fromType == 1) {
            adWebView.setInitialScale(10);
        } else if (fromType == 0){

        }
        adWebView.getSettings().setDefaultTextEncodingName("utf-8");// 設置しないとローカルのhtmlファイルが表示できない
        adWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

				/* 利用規約をリロードすると画面表示幅が可変することを対応*/
                if (fromType == 0) {
                    isTremReloadFlag = !isTremReloadFlag;
                }


            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://market.android.com") || url.startsWith("https://play.google.com")) {
                    String[] temporary = url.split("://");
                    String[] array     = temporary[1].split("\\?");

                    Uri uri = Uri.parse("market://details?" + array[1]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        adWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();

                if (customView == null) {
                    return;
                } else if (customView != null) {
                    playerView.setVisibility(View.INVISIBLE);
                    playerView.removeView(customView);
                    adWebView.setVisibility(View.VISIBLE);
                    customViewCallback.onCustomViewHidden();

                    customView = null;
                    customViewCallback = null;
                    mVideoProgressView = null;
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView == null) {
                    customView = view;
                    customViewCallback = callback;

                    adWebView.setVisibility(View.INVISIBLE);
                    playerView.addView(customView, ViewGroup.LayoutParams.FILL_PARENT);
                    playerView.setVisibility(View.VISIBLE);
                } else if (customView != null) {
                    playerView.removeView(customView);
                    customViewCallback.onCustomViewHidden();

                    customView = null;
                    customViewCallback = null;
                    mVideoProgressView = null;

                    return;
                }

                super.onShowCustomView(view, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    handler.sendEmptyMessage(1);
                }
                super.onProgressChanged(view, progress);
            }
        });
    }

    public void onPause() {
        if (adWebView != null) {
            adWebView.stopLoading();

            try {
                WebView.class.getMethod("onPause").invoke(adWebView);
            } catch(Exception e) {
            }
        }

        super.onPause();
    }

    public void loadurl(final WebView view, final String url) {
        new Thread() {
            public void run() {
                handler.sendEmptyMessage(0);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.loadUrl(url);
                    }
                });
            }
        }.start();
    }

    public void loadurlReload(final WebView view) {
        if (view != null) {
            view.clearCache(true);
        }
        new Thread() {
            public void run() {
                handler.sendEmptyMessage(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.reload();
                    }
                });
            }
        }.start();
    }

    // menu "戻る""進む""更新"
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (customView != null) {
            return false;
        }

        boolean retCode = super.onPrepareOptionsMenu(menu);
//        if (termOfServiceWV.canGoBack()) {
//            menu.findItem(R.id.m13_item01).setEnabled(true);		// 有効
//        } else {
//            menu.findItem(R.id.m13_item01).setEnabled(false);		// 無効
//        }
//        if (termOfServiceWV.canGoForward()) {
//            menu.findItem(R.id.m13_item02).setEnabled(true);// 有効
//
//        } else {
//            menu.findItem(R.id.m13_item02).setEnabled(false);// 無効
//        }

        return retCode;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//
//            case R.id.m13_item01:
//                if (termOfServiceWV != null) {
//                    if (termOfServiceWV.canGoBack()) {
//                        termOfServiceWV.goBack();
//                        invalidateOptionsMenu();
//                    }
//                }
//                break;
//            case R.id.m13_item02:
//                if (termOfServiceWV.canGoForward()) {
//                    termOfServiceWV.goForward();
//                }
//                break;
//            case R.id.m13_item03:
//                loadurlReload(termOfServiceWV);
//                break;
//        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (customView != null) {
                    playerView.setVisibility(View.INVISIBLE);
                    playerView.removeView(customView);
                    adWebView.setVisibility(View.VISIBLE);
                    customViewCallback.onCustomViewHidden();

                    customView = null;
                    customViewCallback = null;
                    mVideoProgressView = null;

                    return false;
                } else {
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }



}
