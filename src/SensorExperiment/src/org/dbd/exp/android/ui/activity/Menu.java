package org.dbd.exp.android.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.dbd.exp.android.dev.R;
import org.dbd.exp.android.ui.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends BaseActivity implements Runnable {

	private static final String TAG = "Menu";
	private ListView mListView;
	private ActivityArrayAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list_menu);

		mListView = (ListView) findViewById(R.id.list_activity);
		mAdapter = new ActivityArrayAdapter(this,
				android.R.layout.simple_list_item_1);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mAdapter);

		new Thread(this).start(); // get list activity

		// update listview
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mListView.invalidateViews();
			}
		});
	}

	@Override
	public void run() {
		mAdapter.addAll(getActivityItem());
	}

	private List<ActivityInfo> getActivityItem() {
		List<ActivityInfo> result = new ArrayList<ActivityInfo>();
		PackageManager manager = getPackageManager();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(CATEGORY_EXAMPLE);
		// Query for all activities that match my filter and request that the
		// filter used
		// to match is returned in the ResolveInfo
		List<ResolveInfo> infos = manager.queryIntentActivities(intent,
				PackageManager.GET_RESOLVED_FILTER);
		for (ResolveInfo info : infos) {
			ActivityInfo activityInfo = info.activityInfo;
			// This activity resolves my Intent with the filter I'm looking
			// for
			String activityPackageName = activityInfo.packageName;
			String activityName = activityInfo.name;
			Log.e(TAG, "Activity " + activityPackageName + "/" + activityName);
			result.add(activityInfo);
		}
		return result;
	}

	private class ActivityArrayAdapter extends ArrayAdapter<ActivityInfo>
			implements OnItemClickListener {

		public ActivityArrayAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId, new ArrayList<ActivityInfo>());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);

			return v;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ActivityInfo info = (ActivityInfo) getItem(position);
			Intent i = new Intent();
			i.setClassName(info.applicationInfo.packageName, info.name);
			startActivity(i);

		}

	}
}
