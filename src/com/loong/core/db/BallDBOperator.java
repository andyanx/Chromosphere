/*
 * 文件名： BallDBOperator.java	 <br>
 * 创建日期: 2013年9月9日<br>
 * 包名: com.loong.core.db <br>
 * Copyright (C), 2011-2012, ShineTech. Co., Ltd.<br>
 * 
 */
package com.loong.core.db;

import java.util.ArrayList;
import java.util.List;

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

public class BallDBOperator {

	/**
	 * 
	 * 功能：返回所有Ball表记录 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @return List<BallBean>
	 * @throws DBException
	 *             List<BallBean>
	 */
	public List<BallBean> getBalls() throws DBException {
		return this.getBalls(0, Integer.MAX_VALUE);
	}

	/**
	 * 
	 * 功能：返回指定区间Ball表记录 <br>
	 * 注意事项：<font color="red">参数格式：yyyyxxx (yyyy：4位年份；xxx：3位期号，不够前面补0)</font> <br>
	 * 
	 * @param start
	 *            int
	 * @param end
	 *            int
	 * @return List<BallBean>
	 * @throws DBException
	 *             List<BallBean>
	 */
	@SuppressWarnings("serial")
	public List<BallBean> getBalls(final int start, final int end) throws DBException {
		return this
				.getDBAccess()
				.queryPrepareList(
						BallBean.class,
						"SELECT * FROM `ball` WHERE CAST(`issue` AS UNSIGNED) >= ? AND CAST(`issue` AS UNSIGNED) <= ? ORDER BY `entity_id` ASC",
						new ArrayList<FieldParameter>() {
							{
								this.add(new FieldParameter(1, start, FieldTypes.BIGINT));
								this.add(new FieldParameter(2, end, FieldTypes.BIGINT));
							}
						});
	}

	/**
	 * 
	 * 功能：返回等于指定期号红球和值的历史记录的下次记录 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param issue
	 *            int
	 * @param start
	 *            int
	 * @param end
	 *            int
	 * @return List<BallBean>
	 * @throws DBException
	 *             List<BallBean>
	 */
	@SuppressWarnings("serial")
	public List<BallBean> getBallsByIssueTotal(final int issue, final int start, final int end) throws DBException {
		return this
				.getDBAccess()
				.queryPrepareList(
						BallBean.class,
						"SELECT `main`.* FROM `ball` AS `main` INNER JOIN  `ball` AS `t1` ON `main`.`entity_id` = `t1`.`entity_id` + 1 INNER JOIN `ball` AS `t2` ON (`t1`.`red1` + `t1`.`red2` + `t1`.`red3` + `t1`.`red4` + `t1`.`red5` + `t1`.`red6`) = (`t2`.`red1` + `t2`.`red2` + `t2`.`red3` + `t2`.`red4` + `t2`.`red5` + `t2`.`red6`) AND CAST(`t2`.`issue` AS UNSIGNED) = ? WHERE CAST(`main`.`issue` AS UNSIGNED) >= ? AND CAST(`main`.`issue` AS UNSIGNED) <= ? ORDER BY `main`.`entity_id` ASC",
						new ArrayList<FieldParameter>() {
							{
								this.add(new FieldParameter(1, issue, FieldTypes.BIGINT));
								this.add(new FieldParameter(2, start, FieldTypes.BIGINT));
								this.add(new FieldParameter(3, end, FieldTypes.BIGINT));
							}
						});
	}

	/**
	 * 
	 * 功能：返回等于指定期号红球和值的历史记录的下次记录 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @param issue
	 *            int
	 * @param start
	 *            int
	 * @param end
	 *            int
	 * @return List<BallBean>
	 * @throws DBException
	 *             List<BallBean>
	 */
	@SuppressWarnings("serial")
	public List<BallBean> getBallsByIssueBalls(final int issue, final int start, final int end) throws DBException {
		return this
				.getDBAccess()
				.queryPrepareList(
						BallBean.class,
						"SELECT DISTINCT `main`.* FROM `ball` AS `main` INNER JOIN  `ball` AS `t1` ON `main`.`entity_id` = `t1`.`entity_id` + 1 INNER JOIN `ball` AS `t2` ON (`t2`.`red1` IN (`t1`.`red1`, `t1`.`red2`, `t1`.`red3`, `t1`.`red4`, `t1`.`red5`, `t1`.`red6`) OR `t2`.`red2` IN (`t1`.`red1`, `t1`.`red2`, `t1`.`red3`, `t1`.`red4`, `t1`.`red5`, `t1`.`red6`) OR `t2`.`red3` IN (`t1`.`red1`, `t1`.`red2`, `t1`.`red3`, `t1`.`red4`, `t1`.`red5`, `t1`.`red6`) OR `t2`.`red4` IN (`t1`.`red1`, `t1`.`red2`, `t1`.`red3`, `t1`.`red4`, `t1`.`red5`, `t1`.`red6`) OR `t2`.`red5` IN (`t1`.`red1`, `t1`.`red2`, `t1`.`red3`, `t1`.`red4`, `t1`.`red5`, `t1`.`red6`) OR `t2`.`red6` IN (`t1`.`red1`, `t1`.`red2`, `t1`.`red3`, `t1`.`red4`, `t1`.`red5`, `t1`.`red6`)) AND CAST(`t2`.`issue` AS UNSIGNED) = ? WHERE CAST(`main`.`issue` AS UNSIGNED) >= ? AND CAST(`main`.`issue` AS UNSIGNED) <= ? ORDER BY `main`.`entity_id` ASC",
						new ArrayList<FieldParameter>() {
							{
								this.add(new FieldParameter(1, issue, FieldTypes.BIGINT));
								this.add(new FieldParameter(2, start, FieldTypes.BIGINT));
								this.add(new FieldParameter(3, end, FieldTypes.BIGINT));
							}
						});
	}

	/**
	 * 
	 * 功能：返回数据库操作接口 <br>
	 * 注意事项：<font color="red">TODO</font> <br>
	 * 
	 * @return
	 *         MySQLDatabaseAccess<BallBean>
	 */
	protected MySQLDatabaseAccess<BallBean> getDBAccess() {
		return new MySQLDatabaseAccess<BallBean>();
	}
}
