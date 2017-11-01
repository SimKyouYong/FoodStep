package sky.foodstep.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sky.foodstep.R;
import sky.foodstep.common.CommonUtil;
import sky.foodstep.obj.CommentObj;
//AIzaSyC1bRHhlzxfHtELQLq1gqK2yV2XvfzXlgA
public class Comment_Adapter extends BaseAdapter {
	CommonUtil dataSet = CommonUtil.getInstance();

	private Activity activity;
	private static LayoutInflater inflater=null;
	ArrayList<CommentObj> items;
	private Typeface ttf;

	public Comment_Adapter(Activity a, ArrayList<CommentObj> m_board  ) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = m_board;

	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView t_id , t_date , t_body;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommentObj board = items.get(position);
		ViewHolder vh = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_comment_item,null);
			vh.t_id = (TextView) convertView.findViewById(R.id.t_id);
			vh.t_date = (TextView) convertView.findViewById(R.id.t_date);
			vh.t_body = (TextView) convertView.findViewById(R.id.t_body);

			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.t_id.setText(board.getID());
		vh.t_date.setText(board.getDATE());
		vh.t_body.setText(board.getBODY());
		return convertView;
	}

}