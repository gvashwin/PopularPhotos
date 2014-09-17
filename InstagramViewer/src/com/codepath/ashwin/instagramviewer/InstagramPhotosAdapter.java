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
	
	private String CMNT_COLOR = "<font color=\"#585858\">";

	public InstagramPhotosAdapter(Context context,
			List<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InstagramPhoto photo = getItem(position);
		
		if(convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
		}
		
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		TextView tvUsrName = (TextView) convertView.findViewById(R.id.tvUserName);
		TextView tvLikeCountValue = (TextView) convertView.findViewById(R.id.tvLikeCount);
		TextView tvLastCmnt = (TextView) convertView.findViewById(R.id.tvLastCmnt);
		TextView tv2ndLastCmnt = (TextView) convertView.findViewById(R.id.tv2ndLastComment);
		TextView tvCmntsLbl = (TextView) convertView.findViewById(R.id.tvCmntsLbl);
		tvCmntsLbl.setTextColor(Color.parseColor("#848484"));
		TextView tvElapsedTime = (TextView) convertView.findViewById(R.id.tvImgTime);
		long timeStamp = Long.parseLong(photo.time);
		String elapsedTime = ""+DateUtils.getRelativeTimeSpanString((timeStamp*1000),System.currentTimeMillis(),DateUtils.HOUR_IN_MILLIS);
		if(elapsedTime.equalsIgnoreCase("0 hours ago")) {
			elapsedTime = "Just now";
		}
		tvElapsedTime.setText(""+elapsedTime);
		ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
		
		String userName ="&nbsp<b>"+photo.usrName+"</b>";
		if(photo.imgCaption!=null)
			tvCaption.setText(photo.imgCaption);
		tvUsrName.setText(Html.fromHtml(userName));

		
		tvLikeCountValue.setText(""+photo.likesCount);
		String formattedCmnt = "<b>"+photo.lastCmntUser+"</b>"+" "+CMNT_COLOR+photo.lastComment;
		tvLastCmnt.setText(Html.fromHtml(formattedCmnt));
		formattedCmnt = "<b>"+photo.secondLastCmntUser+"</b>"+" "+CMNT_COLOR+photo.secondLastComment;
		tv2ndLastCmnt.setText(Html.fromHtml(formattedCmnt));
		//secondLastComment
		//ivPhoto.getLayoutParams().height = photo.imgHeight;
		//ivPhoto.getLayoutParams().width = photo.imgWidth;
		ivPhoto.setImageResource(0);
		Picasso.with(getContext()).load(photo.imgUrl).into(ivPhoto);
		
		CircularImageView usrPhoto = (CircularImageView) convertView.findViewById(R.id.civUsrPhoto);
		Picasso.with(getContext()).load(photo.userPhoto).into(usrPhoto);
		usrPhoto.setBorderWidth(2);
		usrPhoto.addShadow();
		
		return  convertView;
	}
	

}
