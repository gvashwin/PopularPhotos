package com.codepath.ashwin.instagramviewer;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	
	private static class ViewHolder {
		CircularImageView usrPhoto;
		TextView usrName;
		ImageView clock;
		TextView time;
		
		ImageView photo;
		ImageView likeIcon;
		TextView likeCountValue;
		TextView likeLabel;
        TextView caption;
        
        TextView cmntsLabel;
        TextView lastCmnt;
        TextView secondLastCmnt;
    }
	
	private String CMNT_COLOR = "<font color=\"#585858\">";

	public InstagramPhotosAdapter(Context context,
			List<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		InstagramPhoto photo = getItem(position);
		
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
			// getting reference to all the view elements
			viewHolder.usrPhoto = (CircularImageView) convertView.findViewById(R.id.civUsrPhoto);
			viewHolder.usrName = (TextView) convertView.findViewById(R.id.tvUserName);
			viewHolder.clock = (ImageView) convertView.findViewById(R.id.ivClock);
			viewHolder.time = (TextView) convertView.findViewById(R.id.tvImgTime);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
			viewHolder.likeIcon = (ImageView) convertView.findViewById(R.id.ivLikeIcon);
			viewHolder.likeCountValue = (TextView) convertView.findViewById(R.id.tvLikeCount);
			viewHolder.likeLabel = (TextView) convertView.findViewById(R.id.tvLikes);
			viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
			viewHolder.cmntsLabel = (TextView) convertView.findViewById(R.id.tvCmntsLbl);
			viewHolder.lastCmnt = (TextView) convertView.findViewById(R.id.tvLastCmnt);
			viewHolder.secondLastCmnt = (TextView) convertView.findViewById(R.id.tv2ndLastComment);
			// saving the view holder
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// user profile pic
		Picasso.with(getContext()).load(photo.userPhoto).into(viewHolder.usrPhoto);
		viewHolder.usrPhoto.setBorderWidth(2);
		viewHolder.usrPhoto.addShadow();
		
		// user name
		String userName ="&nbsp<b>"+photo.usrName+"</b>";
		viewHolder.usrName.setText(Html.fromHtml(userName)); 
		 
		// time stamp
		long timeStamp = Long.parseLong(photo.time);
		String elapsedTime = ""+DateUtils.getRelativeTimeSpanString((timeStamp*1000),System.currentTimeMillis(),DateUtils.HOUR_IN_MILLIS);
		if(elapsedTime.equalsIgnoreCase("0 hours ago")) {
			elapsedTime = "Just now";
		}
		viewHolder.time.setText(""+elapsedTime);
		
		// photo
		viewHolder.photo.setImageResource(0);
		Picasso.with(getContext()).load(photo.imgUrl).into(viewHolder.photo);
		
		// likeCount
		viewHolder.likeCountValue.setText(""+photo.likesCount);
		
		// caption
		viewHolder.caption.setText(photo.imgCaption);
		
		// comments label
		viewHolder.cmntsLabel.setTextColor(Color.parseColor("#848484"));
		
		// last comment
		String formattedCmnt = "<b>"+photo.lastCmntUser+"</b>"+" "+CMNT_COLOR+photo.lastComment;
		viewHolder.lastCmnt.setText(Html.fromHtml(formattedCmnt));
		
		// 2nd last comment
		formattedCmnt = "<b>"+photo.secondLastCmntUser+"</b>"+" "+CMNT_COLOR+photo.secondLastComment;
		viewHolder.secondLastCmnt.setText(Html.fromHtml(formattedCmnt));
		
		return  convertView;
	}
	

}
