package com.chuanonly.mergegame;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

	public Card(Context context) {
		super(context);

		LayoutParams lp = null;

		background = new View(getContext());
		lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
//		background.setBackgroundColor(0x33ffffff);
		background.setBackgroundResource(R.drawable.circle_rect_shape);
		addView(background, lp);

		label = new TextView(getContext());
		label.setTextSize(28);
		label.setGravity(Gravity.CENTER);

		lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
		addView(label, lp);

		setNum(0);
	}


	private int num = 0;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;

		if (num<=0) {
			label.setText("");
		}else{
			label.setText(num+"");
		}

		switch (num) {
		case 0:
			label.setBackgroundColor(0x00000000);
			break;
		case 2:
//			label.setBackgroundColor(0xffeee4da);
			label.setBackgroundResource(R.drawable.shape2);
			break;
		case 4:
//			label.setBackgroundColor(0xffede0c8);
			label.setBackgroundResource(R.drawable.shape4);
			break;
		case 8:
//			label.setBackgroundColor(0xfff2b179);
			label.setBackgroundResource(R.drawable.shape8);
			break;
		case 16:
//			label.setBackgroundColor(0xfff59563);
			label.setBackgroundResource(R.drawable.shape16);
			break;
		case 32:
//			label.setBackgroundColor(0xfff67c5f);
			label.setBackgroundResource(R.drawable.shape32);
			break;
		case 64:
//			label.setBackgroundColor(0xfff65e3b);
			label.setBackgroundResource(R.drawable.shape64);
			break;
		case 128:
//			label.setBackgroundColor(0xffedcf72);
			label.setBackgroundResource(R.drawable.shape128);
			break;
		case 256:
//			label.setBackgroundColor(0xffedcc61);
			label.setBackgroundResource(R.drawable.shape256);
			break;
		case 512:
//			label.setBackgroundColor(0xffedc850);
			label.setBackgroundResource(R.drawable.shape512);
			break;
		case 1024:
//			label.setBackgroundColor(0xffedc53f);
			label.setBackgroundResource(R.drawable.shape1024);
			break;
		case 2048:
//			label.setBackgroundColor(0xffedc22e);
			label.setBackgroundResource(R.drawable.shape2048);
			break;
		default:
//			label.setBackgroundColor(0xff3c3a32);
			label.setBackgroundResource(R.drawable.shape_other);
			break;
		}
	}

	public boolean equals(Card o) {
		return getNum()==o.getNum();
	}
	
	protected Card clone(){
		Card c= new Card(getContext());
		c.setNum(getNum());
		return c;
	}

	public TextView getLabel() {
		return label;
	}
	
	private TextView label;
	private View background;
}
