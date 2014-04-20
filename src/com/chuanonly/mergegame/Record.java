package com.chuanonly.mergegame;


public class Record {
	public int[][] nums = new int[Config.LINES][Config.LINES];
	public int score = 0;
	public Record(Card[][] cardsMap, int score) 
	{
		for(int i=0; i< Config.LINES; i++)
		{
			for(int j=0; j< Config.LINES; j++)
			{
				nums[i][j] = cardsMap[i][j].getNum();
			}
		}
		this.score = score;
	}
	
}
