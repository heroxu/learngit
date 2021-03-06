package lijie.com.zhbj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 新闻详情页面
 * 
 * @author Kevin
 * 
 */
@SuppressWarnings("deprecation")
public class NewsDetailActivity extends Activity implements OnClickListener {

	protected static final String TAG = NewsDetailActivity.class
			.getSimpleName();

	@ViewInject(R.id.btn_text_size)
	private ImageButton btnTextSize;

	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;

	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;

	@ViewInject(R.id.btn_menu)
	private ImageButton btnMenu;

	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;

	@ViewInject(R.id.wv_webview)
	private WebView mWebView;

	@ViewInject(R.id.pb_news_detail)
	private ProgressBar mProgress;

	private String mUrl;

	private int mCurrentSizeIndex = -1;// 当前选择的字体
	private int mSelectedSizeIndex = 2;// 当前已选的字体, 默认是正常字体, 值为2

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		setContentView(R.layout.activity_news_detail);
		ViewUtils.inject(this);

		mUrl = getIntent().getStringExtra("news_url");

		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		llControl.setVisibility(View.VISIBLE);// 显示字体大小和分享按钮
		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);

		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		btnBack.setOnClickListener(this);

		if (!TextUtils.isEmpty(mUrl)) {
			WebSettings settings = mWebView.getSettings();

			settings.setJavaScriptEnabled(true);// 打开js功能
			settings.setBuiltInZoomControls(true);// 显示放大缩小的按钮
			settings.setUseWideViewPort(true);// 双击缩放

			// mWebView.loadUrl("http://www.itcast.cn");
			mWebView.setWebViewClient(new WebViewClient() {

				// 监听网页加载结束的事件
				@Override
				public void onPageFinished(WebView view, String url) {
					mProgress.setVisibility(View.GONE);
				}
			});

			mWebView.loadUrl(mUrl);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_text_size:
			showChangeSizeDialog();
			break;
		case R.id.btn_share:

			break;

		default:
			break;
		}
	}

	/**
	 * 展示修改字体的对话框
	 */
	private void showChangeSizeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };

		// 设置单选对话框
		builder.setSingleChoiceItems(items, mSelectedSizeIndex,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d(TAG, "选中:" + which);
						mCurrentSizeIndex = which;
					}
				});

		builder.setTitle("字体设置");// 设置标题
		builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (mCurrentSizeIndex) {
				case 0:
					mWebView.getSettings().setTextSize(TextSize.LARGEST);// 设置WebView中字体的大小
					break;
				case 1:
					mWebView.getSettings().setTextSize(TextSize.LARGER);
					break;
				case 2:
					mWebView.getSettings().setTextSize(TextSize.NORMAL);
					break;
				case 3:
					mWebView.getSettings().setTextSize(TextSize.SMALLER);
					break;
				case 4:
					mWebView.getSettings().setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}

				mSelectedSizeIndex = mCurrentSizeIndex;
			}
		});

		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 一键分享
	 */

}
