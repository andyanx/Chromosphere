/*
 * 文件名： ChromosphereDataUpdate.java	 <br>
 * 创建日期: 2013年9月7日<br>
 * 包名: com.loong.core <br>
 * Copyright (C), 2011-2012, ShineTech. Co., Ltd.<br>
 * 
 */
package com.loong.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shinetech.sql.FieldParameter;
import com.shinetech.sql.FieldTypes;
import com.shinetech.sql.MySQLParameter;
import com.shinetech.sql.impl.MySQLDatabaseAccess;

/**
 * 功能描述: 从线上更新已开奖数据，并做初步统计分析 <br>
 * 作者: <a href="mailto:wdps.cn@gmail.com">loong</a> <br>
 * 版本: 1.0.0<br>
 */

public class ChromosphereDataUpdate {

	/**
	 * 功能：TODO <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param args
	 *            void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChromosphereDataUpdate cdu = new ChromosphereDataUpdate();
		cdu.updateBallData();

		// Map<String, Integer> map = cdu.loadNeteaseSales(2013109);
		// for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
		// String field = iter.next();
		// System.out.println(field + ", " + map.get(field));
		// }

		// Map<String, Integer> map = cdu.loadSinaSales(2012061);
		// for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
		// String field = iter.next();
		// System.out.println(field + ", " + map.get(field));
		// }

		// cdu.test(new ArrayList<Integer>() {
		// {
		// this.add(2);
		// this.add(4);
		// this.add(9);
		// this.add(13);
		// this.add(18);
		// this.add(20);
		// }
		// });

		// 当期销售数量查看地址
		// http://caipiao.163.com/award/ssq/xxxxxxx.html(http://caipiao.163.com/order/ssq/guolv.html)
		// http://zst.sina.aicai.com/betTrend!index.jhtml(http://sports.sina.com.cn/l/2caipiao/ssq/)
		// 
	}

	/**
	 * 
	 * 功能：更新已开奖数据 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * void
	 */
	@SuppressWarnings("serial")
	public void updateBallData() {
		try {
			URL url = new URL("http://www.17500.cn/getData/ssq.TXT");

			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("User-Agent", "Microsoft Office Excel 2013");
			conn.connect();

			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			MySQLDatabaseAccess<Integer> db = new MySQLDatabaseAccess<Integer>();
			List<MySQLParameter> list = new ArrayList<MySQLParameter>();

			String ballSql = "INSERT INTO `ball` (`entity_id`, `red1`, `red2`, `red3`, `red4`, `red5`, `red6`, `blue`, `created_at`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String awardSql = "INSERT INTO `award` (`entity_id`, `betting_amount`, `pool_amount`, `level1_nums`, `level1_award`, `level2_nums`, `level2_award`, `level3_nums`, `level3_award`, `level4_nums`, `level4_award`, `level5_nums`, `level5_award`, `level6_nums`, `level6_award`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String researchSql = "INSERT INTO `research` (`entity_id`, `value`, `sum`, `span`, `big_nums`, `small_nums`, `odd_nums`, `even_nums`, `prime_nums`, `composite_nums`, `consecutive_nums`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String salesSql = "INSERT INTO `sales` (`entity_id`, `web_id`, %s) VALUES (%s)";
			String line = null;
			while ((line = reader.readLine()) != null) {
				final String[] cols = line.split(" ");
				if (cols.length != 29) {
					throw new Exception("Error Data: " + line);
				}

				final int entityId = Integer.parseInt(cols[0]);
				final int r1 = Integer.parseInt(cols[9]);
				final int r2 = Integer.parseInt(cols[10]);
				final int r3 = Integer.parseInt(cols[11]);
				final int r4 = Integer.parseInt(cols[12]);
				final int r5 = Integer.parseInt(cols[13]);
				final int r6 = Integer.parseInt(cols[14]);
				final int b = Integer.parseInt(cols[8]);
				final Date date = Date.valueOf(cols[1]);
				final List<Integer> rl = new ArrayList<Integer>(6) {
					{
						this.add(r1);
						this.add(r2);
						this.add(r3);
						this.add(r4);
						this.add(r5);
						this.add(r6);
					}
				};

				StringBuffer tbuf = new StringBuffer();

				// http://www.17500.cn/
				// 处理乐彩网下载数据
				// 插入到表ball、award、research
				if (db.queryPrepareFirst(Integer.class, "SELECT `entity_id` FROM `ball` WHERE `entity_id` = ?",
						new ArrayList<FieldParameter>(1) {
							{
								this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
							}
						}) == null) {
					list.add(new MySQLParameter(ballSql, new ArrayList<FieldParameter>(9) {
						{
							this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
							this.add(new FieldParameter(2, r1, FieldTypes.SMALLINT));
							this.add(new FieldParameter(3, r2, FieldTypes.SMALLINT));
							this.add(new FieldParameter(4, r3, FieldTypes.SMALLINT));
							this.add(new FieldParameter(5, r4, FieldTypes.SMALLINT));
							this.add(new FieldParameter(6, r5, FieldTypes.SMALLINT));
							this.add(new FieldParameter(7, r6, FieldTypes.SMALLINT));
							this.add(new FieldParameter(8, b, FieldTypes.SMALLINT));
							this.add(new FieldParameter(9, date, FieldTypes.DATE));
						}
					}));

					list.add(new MySQLParameter(awardSql, new ArrayList<FieldParameter>(15) {
						{
							this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
							this.add(new FieldParameter(2, Integer.parseInt(cols[15]), FieldTypes.BIGINT));
							this.add(new FieldParameter(3, Integer.parseInt(cols[16]), FieldTypes.BIGINT));
							this.add(new FieldParameter(4, Integer.parseInt(cols[17]), FieldTypes.BIGINT));
							this.add(new FieldParameter(5, Integer.parseInt(cols[18]), FieldTypes.BIGINT));
							this.add(new FieldParameter(6, Integer.parseInt(cols[19]), FieldTypes.BIGINT));
							this.add(new FieldParameter(7, Integer.parseInt(cols[20]), FieldTypes.BIGINT));
							this.add(new FieldParameter(8, Integer.parseInt(cols[21]), FieldTypes.BIGINT));
							this.add(new FieldParameter(9, Integer.parseInt(cols[22]), FieldTypes.BIGINT));
							this.add(new FieldParameter(10, Integer.parseInt(cols[23]), FieldTypes.BIGINT));
							this.add(new FieldParameter(11, Integer.parseInt(cols[24]), FieldTypes.BIGINT));
							this.add(new FieldParameter(12, Integer.parseInt(cols[25]), FieldTypes.BIGINT));
							this.add(new FieldParameter(13, Integer.parseInt(cols[26]), FieldTypes.BIGINT));
							this.add(new FieldParameter(14, Integer.parseInt(cols[27]), FieldTypes.BIGINT));
							this.add(new FieldParameter(15, Integer.parseInt(cols[28]), FieldTypes.BIGINT));
						}
					}));

					final String value = this.getValue(rl);
					final int sum = this.getSum(rl);
					final int span = this.getSpan(rl);
					final int bigNums = this.getBigNums(rl);
					final int smallNums = this.getSmallNums(rl);
					final int oddNums = this.getOddNums(rl);
					final int evenNums = this.getEvenNums(rl);
					final int primeNums = this.getPrimeNums(rl);
					final int compositeNums = this.getCompositeNums(rl);
					final int consecutiveNums = this.getConsecutiveNums(rl);

					list.add(new MySQLParameter(researchSql, new ArrayList<FieldParameter>(10) {
						{
							this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
							this.add(new FieldParameter(2, value, FieldTypes.VARCHAR));
							this.add(new FieldParameter(3, sum, FieldTypes.SMALLINT));
							this.add(new FieldParameter(4, span, FieldTypes.SMALLINT));
							this.add(new FieldParameter(5, bigNums, FieldTypes.SMALLINT));
							this.add(new FieldParameter(6, smallNums, FieldTypes.SMALLINT));
							this.add(new FieldParameter(7, oddNums, FieldTypes.SMALLINT));
							this.add(new FieldParameter(8, evenNums, FieldTypes.SMALLINT));
							this.add(new FieldParameter(9, primeNums, FieldTypes.SMALLINT));
							this.add(new FieldParameter(10, compositeNums, FieldTypes.SMALLINT));
							this.add(new FieldParameter(11, consecutiveNums, FieldTypes.SMALLINT));
						}
					}));

					tbuf.append("ball、award、research");
				}

				// http://caipiao.163.com/award/ssq/xxxxxxx.html
				// 处理网易销售数据
				// 插入到表sales
				if (db.queryPrepareFirst(Integer.class,
						"SELECT `entity_id` FROM `sales` WHERE `entity_id` = ? AND `web_id` = 1",
						new ArrayList<FieldParameter>(1) {
							{
								this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
							}
						}) == null) {
					Map<String, Integer> map = this.loadNeteaseSales(entityId);
					if (map.size() == 14) {
						StringBuffer buf = new StringBuffer();
						List<FieldParameter> fp = new ArrayList<FieldParameter>(16) {
							{
								this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
								this.add(new FieldParameter(2, 1, FieldTypes.INTEGER));
							}
						};
						int i = 3;
						for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
							String field = iter.next();
							buf.append(field);
							if (i < 16) {
								buf.append(", ");
							}
							fp.add(new FieldParameter(i, map.get(field), FieldTypes.INTEGER));
							i++;
						}

						list.add(new MySQLParameter(String.format(salesSql, buf.toString(),
								"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"), fp));

						if (tbuf.length() > 0) {
							tbuf.append("、");
						}

						tbuf.append("sales(NetEase)");
					}

				}

				// http://zst.sina.aicai.com/betTrend!index.jhtml
				// 处理新浪销售数据
				// 插入到表sales
				if (db.queryPrepareFirst(Integer.class,
						"SELECT `entity_id` FROM `sales` WHERE `entity_id` = ? AND `web_id` = 2",
						new ArrayList<FieldParameter>(1) {
							{
								this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
							}
						}) == null) {
					Map<String, Integer> map = this.loadSinaSales(entityId);
					if (map.size() == 49) {
						StringBuffer buf = new StringBuffer();
						List<FieldParameter> fp = new ArrayList<FieldParameter>(51) {
							{
								this.add(new FieldParameter(1, entityId, FieldTypes.INTEGER));
								this.add(new FieldParameter(2, 2, FieldTypes.INTEGER));
							}
						};
						int i = 3;
						for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
							String field = iter.next();
							buf.append(field);
							if (i < 51) {
								buf.append(", ");
							}
							fp.add(new FieldParameter(i, map.get(field), FieldTypes.INTEGER));
							i++;
						}

						list.add(new MySQLParameter(
								String.format(
										salesSql,
										buf.toString(),
										"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"),
								fp));

						if (tbuf.length() > 0) {
							tbuf.append("、");
						}

						tbuf.append("sales(Sina)");
					}

				}

				if (tbuf.length() > 0) {
					System.out.println(String.format("Insert into No. %d data to table(s) (%s): ", entityId,
							tbuf.toString()));
				}

			}

			db.executePrepareMultiBathDependent(list);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 功能：下载指定期号网易销售数量 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param entityId
	 * @return
	 *         Map<String,Integer>
	 */
	public Map<String, Integer> loadNeteaseSales(int entityId) {
		Map<String, Integer> map = new HashMap<String, Integer>(14);

		if (entityId < 2012008) {
			return map;
		}

		String url = String.format("http://caipiao.163.com/award/ssq/%d.html", entityId);

		try {
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.timeout(5000).get();

			for (int i = 1; i <= 2; i++) {
				Element ele = doc.getElementById("hotCoolCon" + i);
				Elements trs = ele.select("table").select("tr");
				for (int j = 0; j < trs.size(); j++) {
					Elements tds = trs.get(j).select("td");
					Element ball = tds.first().getElementsByTag("span").first();

					int bn = Integer.parseInt(ball.text());
					int nums = Integer.parseInt(tds.last().text().replace("次", ""));

					if (ball.attr("class").equalsIgnoreCase("red_ball")) {
						map.put("red" + bn, nums);
					} else if (ball.attr("class").equalsIgnoreCase("blue_ball")) {
						map.put("blue" + bn, nums);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 
	 * 功能：下载指定期号新浪销售数量 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param entityId
	 * @return
	 *         Map<String,Integer>
	 */
	@SuppressWarnings("serial")
	public Map<String, Integer> loadSinaSales(int entityId) {
		Map<String, Integer> map = new HashMap<String, Integer>(49);

		if (entityId < 2012061) {
			return map;
		}

		final String issueNo = String.valueOf(entityId + 1);
		String url = "http://zst.sina.aicai.com/betTrend!index.jhtml";

		try {
			// 请求页面
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.timeout(5000).data(new HashMap<String, String>() {
						{
							this.put("order", "number");
							this.put("issueNo", issueNo);
							this.put("upDown", "up");
							this.put("stopstep", "1");
						}
					}).post();

			// 分析红球投注倾向
			Element rEle = doc.getElementsByClass("statsRed_tab").first();
			Elements rTrs = rEle.select("table").select("tr");
			for (int i = 0; i < rTrs.size(); i++) {
				if (rTrs.get(i).attr("class").contains("bot")) {

					Elements tds = rTrs.get(i).select("td");

					for (int j = 0; j < tds.size(); j++) {
						map.put("red" + (j + 1), Integer.parseInt(tds.get(j).getElementsByTag("span").first().text()));
					}
				}
			}

			// 分析蓝球投注倾向
			Element bEle = doc.getElementsByClass("statsBlue_tab").first();
			Elements bTrs = bEle.select("table").select("tr");
			for (int i = 0; i < bTrs.size(); i++) {
				if (bTrs.get(i).attr("class").contains("bot")) {

					Elements tds = bTrs.get(i).select("td");

					for (int j = 0; j < tds.size(); j++) {
						map.put("blue" + (j + 1), Integer.parseInt(tds.get(j).getElementsByTag("span").first().text()));
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 
	 * 功能：返回序值 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         String
	 */
	private String getValue(List<Integer> list) {
		Collections.sort(list);

		StringBuffer buf = new StringBuffer();
		for (int r : list) {
			buf.append(String.format("%02d", r));
		}

		return buf.toString();
	}

	/**
	 * 
	 * 功能：返回和值 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getSum(List<Integer> list) {
		int sum = 0;
		for (int r : list) {
			sum += r;
		}
		return sum;
	}

	/**
	 * 
	 * 功能：返回跨度 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getSpan(List<Integer> list) {
		Collections.sort(list);

		return list.get(list.size() - 1) - list.get(0);

	}

	/**
	 * 
	 * 功能：返回大球数（大小比） <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getBigNums(List<Integer> list) {
		int bigNums = 0;
		for (int r : list) {
			if (r >= 17 && r <= 33) {
				bigNums++;
			}
		}
		return bigNums;
	}

	/**
	 * 
	 * 功能：返回小球数（大小比） <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getSmallNums(List<Integer> list) {
		int smallNums = 0;
		for (int r : list) {
			if (r >= 1 && r <= 16) {
				smallNums++;
			}
		}
		return smallNums;
	}

	/**
	 * 
	 * 功能：返回奇球数（奇偶比） <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getOddNums(List<Integer> list) {
		int oddNums = 0;
		for (int r : list) {
			if (r % 2 != 0) {
				oddNums++;
			}
		}
		return oddNums;
	}

	/**
	 * 
	 * 功能：返回偶球数（奇偶比） <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getEvenNums(List<Integer> list) {
		int evenNums = 0;
		for (int r : list) {
			if (r % 2 == 0) {
				evenNums++;
			}
		}
		return evenNums;
	}

	/**
	 * 
	 * 功能：返回质球数（质合比） <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getPrimeNums(List<Integer> list) {
		int primeNums = 0;
		for (int r : list) {
			Math.sqrt(r);
			if (r == 1 || r == 2) {
				primeNums++;
			} else {
				int count = 0;
				for (int i = 1; i <= r; i++) {
					if (r % i == 0) {
						count++;
					}
				}
				if (count == 2) {
					primeNums++;
				}
			}
		}
		return primeNums;
	}

	/**
	 * 
	 * 功能：返回合球数（质合比） <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getCompositeNums(List<Integer> list) {
		int compositeNums = 0;
		for (int r : list) {
			if (r > 2) {
				int count = 0;
				for (int i = 1; i <= r; i++) {
					if (r % i == 0) {
						count++;
					}
				}
				if (count > 2) {
					compositeNums++;
				}
			}
		}
		return compositeNums;
	}

	/**
	 * 
	 * 功能：返回连号数 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 * @return
	 *         int
	 */
	private int getConsecutiveNums(List<Integer> list) {
		Collections.sort(list);

		int consecutiveNums = 0;
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i) + 1 == list.get(i + 1)) {
				consecutiveNums++;
			}
		}
		return consecutiveNums;
	}

	/**
	 * 
	 * 功能：测试分析函数 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param list
	 *            void
	 */
	protected void test(List<Integer> list) {
		System.out.println("序值: " + this.getValue(list));
		System.out.println("和值：" + this.getSum(list));
		System.out.println("跨度：" + this.getSpan(list));
		System.out.println("大小比：" + this.getBigNums(list) + ":" + this.getSmallNums(list));
		System.out.println("奇偶比：" + this.getOddNums(list) + ":" + this.getEvenNums(list));
		System.out.println("质合比：" + this.getPrimeNums(list) + ":" + this.getCompositeNums(list));
		System.out.println("连号：" + this.getConsecutiveNums(list));
	}
}
