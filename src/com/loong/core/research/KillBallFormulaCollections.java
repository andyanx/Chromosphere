/*
 * 文件名： KillBallFormulaCollections.java <br>
 * 创建日期: 2013年9月21日<br>
 * 包名: com.loong.core.research <br>
 * Copyright (C), 2011-2012, ShineTech. Co., Ltd.<br>
 */
package com.loong.core.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neu.sxc.expression.Expression;
import neu.sxc.expression.ExpressionFactory;
import neu.sxc.expression.tokens.Valuable;

import com.loong.core.bean.BallBean;
import com.shinetech.sql.FieldParameter;
import com.shinetech.sql.FieldTypes;
import com.shinetech.sql.exception.DBException;
import com.shinetech.sql.impl.MySQLDatabaseAccess;

/**
 * 功能描述: TODO <br>
 * 作者: <a href="mailto:wdps.cn@gmail.com">loong</a> <br>
 * 版本: 1.0.0<br>
 */

public class KillBallFormulaCollections {

	@SuppressWarnings("serial")
	private Map<String, Integer>	map	= new TreeMap<String, Integer>() {
											{
												this.put("b3 - b2", 0);
												this.put("b5 - b2", 0);
												this.put("b5 - b3", 0);
												this.put("b6 - b1", 0);
												this.put("b7 - b5", 0);
												this.put("b1 + 9", 0);
												this.put("b2 + 5", 0);
												this.put("b3 + 4", 0);
												this.put("b3 + 7", 0);
												this.put("b6 + 4", 0);
												this.put("b7 + 7", 0);
												this.put("b7 + 9", 0);
												this.put("b1 + b2", 0);
												this.put("b1 + b7", 0);
												this.put("b1 + b2 + b3 + b7 - 1", 0);
												this.put("b2 + b7 - 1", 0);
												this.put("b3 - b1 + b7 + 2", 0);
												this.put("b5 - b4 + b7 + 1", 0);
												this.put("b6 - b1 + b7 - 3", 0);
												this.put("b7 - b4 + 1", 0);
												this.put("b1 * 4 - 2", 0);
												this.put("b1 * b7", 0);
												this.put("b1 * 3 + b7 * 3", 0);
												this.put("b7 * 5 + 2", 0);
												this.put("b7 * v25 + 2", 0);
											}
										};

	/**
	 * 
	 * 功能：转换红球序值为数组 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param value
	 * @return
	 *         int[]
	 */
	public int[] getRedBallArray(String value) {
		int[] values = new int[value.length() / 2];
		for (int i = 0; i < value.length(); i += 2) {
			values[i / 2] = Integer.parseInt(value.substring(i, i + 2));
		}

		return values;
	}

	/**
	 * 
	 * 功能：转换红球序值及蓝球为数组 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param value
	 * @param blue
	 * @return
	 *         int[]
	 */
	public int[] getAllBallArray(String value, int blue) {
		value += String.format("%02d", blue);

		return this.getRedBallArray(value);
	}

	/**
	 * 
	 * 功能：返回不可能出现红球数组 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param balls
	 * @return
	 *         Integer[]
	 */
	public int[] getForecastBallArray(int[] balls) {
		return this.getForecastBallArray(balls, false);
	}

	/**
	 * 
	 * 功能：返回不可能出现红球数组 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param balls
	 * @param distinct
	 * @return
	 *         int[]
	 */
	public int[] getForecastBallArray(int[] balls, boolean distinct) {

		int i = 0;
		int[] forecastBalls = new int[this.map.size()];
		ExpressionFactory factory = ExpressionFactory.getInstance();

		for (Iterator<String> iter = this.map.keySet().iterator(); iter.hasNext();) {
			String express = iter.next() + ";";
			Expression expression = factory.getExpression(express);
			expression.initVariable("b1", balls[0]);
			expression.initVariable("b2", balls[1]);
			expression.initVariable("b3", balls[2]);
			expression.initVariable("b4", balls[3]);
			expression.initVariable("b5", balls[4]);
			expression.initVariable("b6", balls[5]);
			expression.initVariable("b7", balls[6]);
			expression.initVariable("v25", (balls[6] % 2 == 0) ? 2 : 5);
			Valuable result = expression.reParseAndEvaluate();

			int r = result.getNumberValue().intValue();

			if ("b1 + b7".equals(express) || "b3 - b1 + b7 + 2".equals(express)) {
				int index = Arrays.binarySearch(balls, balls[6]);
				if (index >= 0 && index != 6) {
					r--;
				}
			}

			r = Math.abs(r) % 33;

			if (distinct) {
				if (Arrays.binarySearch(forecastBalls, r) < 0) {
					forecastBalls[i] = r;
					i++;
				}
			} else {
				forecastBalls[i] = r;
				i++;
			}

		}

		if (distinct && i < this.map.size()) {
			forecastBalls = Arrays.copyOfRange(forecastBalls, 0, i);
		}

		return forecastBalls;
	}

	/**
	 * 
	 * 功能：验证杀号方法 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param entityId
	 *            void
	 */
	@SuppressWarnings("serial")
	public void checkKillBallFormula(final int entityId) {
		try {
			// 获取历史开奖记录
			List<BallBean> balls = new MySQLDatabaseAccess<BallBean>()
					.queryPrepareList(
							BallBean.class,
							"SELECT `ball`.*, `research`.`value` FROM `ball`, `research` WHERE `ball`.`entity_id` >= ? AND `ball`.`entity_id` = `research`.`entity_id` ORDER BY `ball`.`entity_id`",
							new ArrayList<FieldParameter>() {
								{
									this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
								}
							});

			// 验证
			int historyRecords = balls.size() - 1;
			for (int i = 0; i < historyRecords; i++) {
				int[] next = this.getRedBallArray(balls.get(i + 1).getValue());
				int blue = balls.get(i).getBlue();

				int[] ballArray = this.getAllBallArray(balls.get(i).getValue(), blue);

				System.out.println("=================");
				this.arrayToString(ballArray);

				int[] forecastBalls = this.getForecastBallArray(ballArray);

				this.arrayToString(forecastBalls, true);
				this.arrayToString(next, true);

				int j = 0;
				for (Iterator<String> iter = this.map.keySet().iterator(); iter.hasNext();) {

					String express = iter.next();
					int value = this.map.get(express);

					int result = Arrays.binarySearch(next, forecastBalls[j]);

					if (result < 0) {
						this.map.put(express, value + 1);
					}

					j++;
				}

			}

			System.out.println(String.format("SUM: %d", historyRecords));
			for (Iterator<String> iter = this.map.keySet().iterator(); iter.hasNext();) {
				String express = iter.next();
				int value = this.map.get(express);
				System.out.println(String.format("%s: %.2f%%", express, (value * 100.0 / historyRecords)));
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	public BallBean getBallBean(final int entityId) {
		try {
			return new MySQLDatabaseAccess<BallBean>()
					.queryPrepareFirst(
							BallBean.class,
							"SELECT `ball`.*, `research`.`value` FROM `ball`, `research` WHERE `ball`.`entity_id` = ? AND `ball`.`entity_id` = `research`.`entity_id`",
							new ArrayList<FieldParameter>() {
								{
									this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
								}
							});
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 功能：输出数组 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param a
	 * @param sort
	 *            void
	 */
	public void arrayToString(int[] a) {
		this.arrayToString(a, false);
	}

	/**
	 * 
	 * 功能：输出数组 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param a
	 * @param sort
	 *            void
	 */
	public void arrayToString(int[] a, boolean sort) {
		if (sort) {
			Arrays.sort(a);
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			buf.append(String.format("%02d", a[i]));
			if (i < a.length - 1) {
				buf.append(", ");
			}
		}
		System.out.println(buf.toString());
	}

	/**
	 * 功能：TODO <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param args
	 *            void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int entityId = 2013090;

		KillBallFormulaCollections kbf = new KillBallFormulaCollections();
		kbf.checkKillBallFormula(entityId);

		BallBean ballBean = kbf.getBallBean(2013110);

		int[] forecastBalls = kbf.getForecastBallArray(kbf.getAllBallArray(ballBean.getValue(), ballBean.getBlue()),
				true);

		kbf.arrayToString(forecastBalls);
		kbf.arrayToString(forecastBalls, true);
	}
}
