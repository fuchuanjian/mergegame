package com.chuanonly.mergegame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
	private LinkedList<Record> records = new LinkedList<Record>();
	private boolean isPause = true;
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initGameView();
	}

	public GameView(Context context) {
		super(context);

		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initGameView();
	}

	private void initGameView(){
		setColumnCount(Config.LINES);
//		setBackgroundColor(0xffbbada0);


		setOnTouchListener(new View.OnTouchListener() {

			private float startX,startY,offsetX,offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isPause == true) return true;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX()-startX;
					offsetY = event.getY()-startY;


					if (Math.abs(offsetX)>Math.abs(offsetY)) {
						if (offsetX<-5) {
							swipeLeft();
						}else if (offsetX>5) {
							swipeRight();
						}
					}else{
						if (offsetY<-5) {
							swipeUp();
						}else if (offsetY>5) {
							swipeDown();
						}
					}

					break;
				}
				return true;
			}
		});
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int size = Math.min(w, h);
		Config.CARD_WIDTH = (Math.min(w, h)-10)/Config.LINES;

		addCards(Config.CARD_WIDTH,Config.CARD_WIDTH);

		startGame();
		getSaveRecord();
	}

	private void addCards(int cardWidth,int cardHeight){

		Card c;

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				c = new Card(getContext());
				addView(c, cardWidth, cardHeight);

				cardsMap[x][y] = c;
			}
		}
	}

	public void startGame(){
		isPause = false;
		records.clear();
		MainHomeActivity aty = MainHomeActivity.getMainActivity();
		aty.clearScore();
		aty.showBestScore(aty.getBestScore());

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				cardsMap[x][y].setNum(0);
			}
		}

		addRandomNum();
		addRandomNum();
		MainHomeActivity.getMainActivity().setUndoBtnEnable(false);
	}

	private void addRandomNum(){

		emptyPoints.clear();

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				if (cardsMap[x][y].getNum()<=0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}

		if (emptyPoints.size()>0) {

			Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
			cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);

			MainHomeActivity.getMainActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
		}
	}


	private void swipeLeft(){
		MainHomeActivity.getMainActivity().setUndoBtnEnable(true);
		records.addLast(new Record(cardsMap, MainHomeActivity.getMainActivity().score));
		boolean merge = false;

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {

				for (int x1 = x+1; x1 < Config.LINES; x1++) {
					if (cardsMap[x1][y].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {

							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y],cardsMap[x][y], x1, x, y, y);

							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x--;
							merge = true;

						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							MainHomeActivity.getMainActivity().getAnimLayer().createScaleAnimate(cardsMap[x][y]);
							cardsMap[x1][y].setNum(0);

							MainHomeActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeRight(){
		MainHomeActivity.getMainActivity().setUndoBtnEnable(true);
		records.addLast(new Record(cardsMap, MainHomeActivity.getMainActivity().score));
		boolean merge = false;

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = Config.LINES-1; x >=0; x--) {

				for (int x1 = x-1; x1 >=0; x1--) {
					if (cardsMap[x1][y].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							MainHomeActivity.getMainActivity().getAnimLayer().createScaleAnimate(cardsMap[x][y]);
							cardsMap[x1][y].setNum(0);
							MainHomeActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeUp(){
		MainHomeActivity.getMainActivity().setUndoBtnEnable(true);
		records.addLast(new Record(cardsMap, MainHomeActivity.getMainActivity().score));
		boolean merge = false;

		for (int x = 0; x < Config.LINES; x++) {
			for (int y = 0; y < Config.LINES; y++) {

				for (int y1 = y+1; y1 < Config.LINES; y1++) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y--;

							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							MainHomeActivity.getMainActivity().getAnimLayer().createScaleAnimate(cardsMap[x][y]);
							cardsMap[x][y1].setNum(0);
							MainHomeActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;

					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeDown(){
		MainHomeActivity.getMainActivity().setUndoBtnEnable(true);
		records.addLast(new Record(cardsMap, MainHomeActivity.getMainActivity().score));
		boolean merge = false;

		for (int x = 0; x < Config.LINES; x++) {
			for (int y = Config.LINES-1; y >=0; y--) {

				for (int y1 = y-1; y1 >=0; y1--) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							MainHomeActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							MainHomeActivity.getMainActivity().getAnimLayer().createScaleAnimate(cardsMap[x][y]);
							cardsMap[x][y1].setNum(0);
							MainHomeActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	private void checkComplete(){

		boolean lose = true;
		boolean win = false;
		
		CHECK_WIN:
			for (int y = 0; y < Config.LINES; y++) {
				for (int x = 0; x < Config.LINES; x++) {
					if (cardsMap[x][y].getNum() >= MainHomeActivity.SCORE[MainHomeActivity.GAME_MODE])
					{
						win = true;
						break CHECK_WIN;
					}
				}
			}
		
		
		ALL:
			for (int y = 0; y < Config.LINES; y++) {
				for (int x = 0; x < Config.LINES; x++) {
					if (cardsMap[x][y].getNum()==0||
							(x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
							(x<Config.LINES-1&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
							(y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
							(y<Config.LINES-1&&cardsMap[x][y].equals(cardsMap[x][y+1]))) {

						lose = false;
						break ALL;
					}
				}
			}
		if (win)
		{
			MainHomeActivity.getMainActivity().winGame();
			isPause = true;
		}
		if (lose) {
			MainHomeActivity.getMainActivity().loseGame();
			isPause = true;
		}

	}
	public void undo()
	{
		if(!records.isEmpty())
		{
			Record record = records.removeLast();
			for(int i=0; i< Config.LINES; i++)
			{
				for(int j=0; j< Config.LINES; j++)
				{
					 cardsMap[i][j].setNum(record.nums[i][j]);
				}
			}
			MainHomeActivity.getMainActivity().undo(record.score);
			if (records.isEmpty())
			{
				MainHomeActivity.getMainActivity().setUndoBtnEnable(false);
			}
		}else
		{
			MainHomeActivity.getMainActivity().setUndoBtnEnable(false);
		}
	}
	private Card[][] cardsMap = new Card[Config.LINES][Config.LINES];
	private List<Point> emptyPoints = new ArrayList<Point>();
	public void saveRecord()
	{
		try {
			StringBuffer sb = new StringBuffer();
			for(int i=0; i< Config.LINES; i++)
			{
				for(int j=0; j< Config.LINES; j++)
				{
					sb.append(cardsMap[i][j].getNum()).append(";");
				}
			}
			sb.append(MainHomeActivity.getMainActivity().score);
			Util.setStringToSharedPref("record", sb.toString());
		} catch (Exception e) {
		}
		
	}
	public void getSaveRecord()
	{
		try {			
			String recordStr = Util.getStringFromSharedPref("record", "");
			if (!TextUtils.isEmpty(recordStr))
			{			
				String nums[] = recordStr.split(";");
				int len = nums.length;
				for(int i=0; i< len-2; i++)
				{
					cardsMap[i/Config.LINES][i%Config.LINES].setNum(Integer.valueOf(nums[i]));
				}
				MainHomeActivity.getMainActivity().undo(Integer.valueOf(nums[len-1]));
			}
		} catch (Exception e) {
		}
	}
}
