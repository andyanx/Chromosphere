/*
 * 文件名： ChromosphereTotalNumbers.java <br>
 * 创建日期: 2013年9月9日<br>
 * 包名: com.loong.core <br>
 * Copyright (C), 2011-2012, ShineTech. Co., Ltd.<br>
 */
package com.loong.core;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.loong.core.bean.BallBean;
import com.loong.core.db.BallDBOperator;
import com.shinetech.sql.exception.DBException;

/**
 * 功能描述: TODO <br>
 * 作者: <a href="mailto:wdps.cn@gmail.com">loong</a> <br>
 * 版本: 1.0.0<br>
 */

@SuppressWarnings("serial")
public class ChromosphereTotalNumbers extends ApplicationFrame {

	/**
	 * 
	 * 目的和功能：构造方法,构造ChromosphereTotalNumbers类的一个实例 <br>
	 * 注意事项：
	 * 
	 * @param title
	 */
	public ChromosphereTotalNumbers(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * 功能：统计指定期号区间红蓝球出现的次数，使用BarChart显示 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param start
	 * @param end
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws DBException
	 *             void
	 */
	public void totalNumbersBarChart(int start, int end) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, DBException {

		// 红球统计
		Map<Integer, Integer> redMap = new HashMap<Integer, Integer>();
		// 篮球统计
		Map<Integer, Integer> blueMap = new HashMap<Integer, Integer>();

		BallDBOperator operator = new BallDBOperator();
		for (BallBean ball : operator.getBalls(start, end)) {
			// 通过反射机制获取6个红球值
			for (int i = 1; i <= 6; i++) {
				Integer red = (Integer) ball.getClass().getDeclaredMethod("getRed" + i).invoke(ball);
				int sum = 1;
				if (redMap.containsKey(red)) {
					sum = redMap.get(red) + 1;
				}
				redMap.put(red, sum);
			}

			// 获取篮球值
			int blue = ball.getBlue();
			int sum = 1;
			if (blueMap.containsKey(blue)) {
				sum = blueMap.get(blue) + 1;
			}
			blueMap.put(blue, sum);
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 1; i <= 33; i++) {
			if (redMap.containsKey(i)) {
				dataset.addValue(redMap.get(i), "Red", String.valueOf(i));
			}
			if (blueMap.containsKey(i)) {
				dataset.addValue(blueMap.get(i), "Blue", String.valueOf(i));
			}
		}

		JFreeChart chart = ChartFactory.createBarChart("Total Numbers", "Ball Number", "Total", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		CategoryItemRenderer render = chart.getCategoryPlot().getRenderer();
		render.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		render.setBaseItemLabelFont(new Font("Serif", Font.PLAIN, 12));
		render.setBaseItemLabelsVisible(true);

		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(1024, 768));
		this.setContentPane(chartPanel);
	}

	/**
	 * 
	 * 功能：统计指定期号区间红球出现的次数，使用XYLineChart显示 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param start
	 * @param end
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws DBException
	 *             void
	 */
	public void totalNumbersXYLineChart(int start, int end) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, DBException {
		// 年份统计
		Map<String, Map<Integer, Integer>> yearMap = new HashMap<String, Map<Integer, Integer>>();

		BallDBOperator operator = new BallDBOperator();
		for (BallBean ball : operator.getBalls(start, end)) {
			// 获取年份
			String year = String.valueOf(ball.getEntity_id()).substring(0, 4);

			// 通过反射机制获取6个红球值
			for (int i = 1; i <= 6; i++) {
				Integer red = (Integer) ball.getClass().getDeclaredMethod("getRed" + i).invoke(ball);
				int sum = 1;
				if (yearMap.containsKey(year)) {
					if (yearMap.get(year).containsKey(red)) {
						sum = yearMap.get(year).get(red) + 1;
					}
					yearMap.get(year).put(red, sum);
				} else {
					Map<Integer, Integer> yMap = new HashMap<Integer, Integer>();
					yMap.put(red, sum);
					yearMap.put(year, yMap);
				}
			}
		}

		Object[] years = yearMap.keySet().toArray();
		Arrays.sort(years);

		XYSeriesCollection dataset = new XYSeriesCollection();
		for (Object y : years) {
			String year = y.toString();
			Map<Integer, Integer> yMap = yearMap.get(year);
			XYSeries series = new XYSeries(year);

			for (int i = 1; i <= 33; i++) {
				if (yMap.containsKey(i)) {
					series.add(i, yMap.get(i));
				}
			}

			dataset.addSeries(series);
		}

		JFreeChart chart = ChartFactory.createXYLineChart("Total Numbers", "Red Ball No.", "Total Red Balls", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
		render.setBaseShapesVisible(true);
		render.setBaseShapesFilled(true);

		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(1024, 768));
		this.setContentPane(chartPanel);
	}

	public void totalNumbersByIssueTotalBarChart(int issue, int start, int end) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, DBException,
			IOException {
		// 红球统计
		Map<Integer, Integer> redMap = new HashMap<Integer, Integer>();
		// 篮球统计
		Map<Integer, Integer> blueMap = new HashMap<Integer, Integer>();

		BallDBOperator operator = new BallDBOperator();
		for (BallBean ball : operator.getBallsByIssueTotal(issue, start, end)) {
			// 通过反射机制获取6个红球值
			for (int i = 1; i <= 6; i++) {
				Integer red = (Integer) ball.getClass().getDeclaredMethod("getRed" + i).invoke(ball);
				int sum = 1;
				if (redMap.containsKey(red)) {
					sum = redMap.get(red) + 1;
				}
				redMap.put(red, sum);
			}

			// 获取篮球值
			int blue = ball.getBlue();
			int sum = 1;
			if (blueMap.containsKey(blue)) {
				sum = blueMap.get(blue) + 1;
			}
			blueMap.put(blue, sum);
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 1; i <= 33; i++) {
			if (redMap.containsKey(i)) {
				dataset.addValue(redMap.get(i), "Red", String.valueOf(i));
			}
			if (blueMap.containsKey(i)) {
				dataset.addValue(blueMap.get(i), "Blue", String.valueOf(i));
			}
		}

		JFreeChart chart = ChartFactory.createBarChart("Total Numbers", "Ball Number", "Total", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		CategoryItemRenderer render = chart.getCategoryPlot().getRenderer();
		render.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		render.setBaseItemLabelFont(new Font("Serif", Font.PLAIN, 12));
		render.setBaseItemLabelsVisible(true);

		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(1024, 768));
		this.setContentPane(chartPanel);

		String outDir = System.getProperty("user.dir") + File.separator + "data" + File.separator
				+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		File out = new File(outDir);
		if (!out.exists()) {
			out.mkdirs();
		}
		if (out.canWrite()) {
			ChartUtilities.saveChartAsJPEG(new File(outDir + File.separator + "totalValueByIssueTotal.jpg"), chart,
					1024, 768);
		}
	}

	public void totalNumbersByIssueBallsBarChart(int issue, int start, int end) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, DBException,
			IOException {
		// 红球统计
		Map<Integer, Integer> redMap = new HashMap<Integer, Integer>();
		// 篮球统计
		Map<Integer, Integer> blueMap = new HashMap<Integer, Integer>();

		BallDBOperator operator = new BallDBOperator();
		for (BallBean ball : operator.getBallsByIssueBalls(issue, start, end)) {
			// 通过反射机制获取6个红球值
			for (int i = 1; i <= 6; i++) {
				Integer red = (Integer) ball.getClass().getDeclaredMethod("getRed" + i).invoke(ball);
				int sum = 1;
				if (redMap.containsKey(red)) {
					sum = redMap.get(red) + 1;
				}
				redMap.put(red, sum);
			}

			// 获取篮球值
			int blue = ball.getBlue();
			int sum = 1;
			if (blueMap.containsKey(blue)) {
				sum = blueMap.get(blue) + 1;
			}
			blueMap.put(blue, sum);
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 1; i <= 33; i++) {
			if (redMap.containsKey(i)) {
				dataset.addValue(redMap.get(i), "Red", String.valueOf(i));
			}
			if (blueMap.containsKey(i)) {
				dataset.addValue(blueMap.get(i), "Blue", String.valueOf(i));
			}
		}

		JFreeChart chart = ChartFactory.createBarChart("Total Numbers", "Ball Number", "Total", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		CategoryItemRenderer render = chart.getCategoryPlot().getRenderer();
		render.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		render.setBaseItemLabelFont(new Font("Serif", Font.PLAIN, 12));
		render.setBaseItemLabelsVisible(true);

		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(1024, 768));
		this.setContentPane(chartPanel);

		String outDir = System.getProperty("user.dir") + File.separator + "data" + File.separator
				+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		File out = new File(outDir);
		if (!out.exists()) {
			out.mkdirs();
		}
		if (out.canWrite()) {
			ChartUtilities.saveChartAsJPEG(new File(outDir + File.separator + "totalValueByIssueBall.jpg"), chart,
					1024, 768);
		}
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
		ChromosphereTotalNumbers tn = new ChromosphereTotalNumbers("Chromosphere Total Numbers");
		try {
			// tn.totalNumbersBarChart(2006000, 2006200);
			// tn.totalNumbersXYLineChart(2013055, 2013105);
			// tn.totalNumbersByIssueTotalBarChart(2013107, 0, 2013107);
			tn.totalNumbersByIssueBallsBarChart(2013107, 2013007, 2013107);
			tn.pack();
			RefineryUtilities.centerFrameOnScreen(tn);
			tn.setVisible(true);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
