package lijie.com.zhbj.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import lijie.com.zhbj.MainActivity;
import lijie.com.zhbj.R;
import lijie.com.zhbj.base.impl.NewsCenterPager;
import lijie.com.zhbj.domain.NewsBean;

/**
 * 左侧菜单Fragment
 * 
 * @author Kevin
 * 
 */
public class LeftMenuFragment extends BaseFragment {

	private static final String TAG = LeftMenuFragment.class.getSimpleName();
	ArrayList<NewsBean.NewsMenuBean> mMenuList;// 菜单列表
	private ListView mListView;
	private LeftMenuAdapter mAdapter;

	private int mCurrentPosition;// 当前选中第几个菜单

	@Override
	public View initView() {
		mListView = new ListView(mActivity);
		mListView.setBackgroundColor(Color.BLACK);// 设置背景色
		mListView.setDividerHeight(0);// 分割线高度设置为0(去掉分割线)
		mListView.setSelector(android.R.color.transparent);// 将点击效果设置为透明(去掉点击效果)
		mListView.setPadding(0, 40, 0, 0);// 设置边距

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPosition = position;
				Log.d(TAG, "当前菜单:" + mCurrentPosition);
				mAdapter.notifyDataSetChanged();// 刷新listview

				MainActivity mainUI = (MainActivity) mActivity;
				SlidingMenu slidingMenu = mainUI.getSlidingMenu();
				slidingMenu.toggle();// 如果侧边栏打开,则关闭;如果关闭,则打开
				
				setCurrentDetailPager(position);//设置当前要显示的详情页面
			}
		});

		return mListView;
	}

	/**
	 * 设置当前要显示的详情页面
	 * @param position
	 */
	protected void setCurrentDetailPager(int position) {
		MainActivity mainUI = (MainActivity) mActivity;
		ContentFragment fragment = mainUI.getContentFragment();//获取主页Fragment
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();//获取新闻中心页面
		newsCenterPager.setCurrentDetailPager(position);//设置新闻中心的详情页面
	}

	/**
	 * 设置侧边栏数据
	 * 
	 * @param data
	 */
	public void setNewsMenuData(ArrayList<NewsBean.NewsMenuBean> data) {
		this.mMenuList = data;
		mCurrentPosition = 0;//初始化位置信息
		Log.d(TAG, "侧边栏数据: " + data);
		mAdapter = new LeftMenuAdapter();
		mListView.setAdapter(mAdapter);//设置listview的数据源
	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMenuList.size();
		}

		@Override
		public NewsBean.NewsMenuBean getItem(int position) {
			return mMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = (TextView) View.inflate(mActivity,
					R.layout.left_menu_item, null);
			textView.setText(getItem(position).title);

			// 如果是当前选中的item, 置为可用,显示为红色,如果不是,置为不可用,显示为白色
			textView.setEnabled(position == mCurrentPosition);
			return textView;
		}

	}
}
