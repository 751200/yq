package com.gec._03_wiki.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gec._03_wiki.pojo.EbookSnapshot;
import com.gec._03_wiki.pojo.StatisticResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 751200
* @description 针对表【ebook_snapshot】的数据库操作Mapper
* @createDate 2023-09-08 09:50:00
* @Entity com.gec._03_wiki.pojo.EbookSnapshot
*/
@Mapper
public interface EbookSnapshotMapper extends BaseMapper<EbookSnapshot> {

    @Update("INSERT INTO ebook_snapshot(ebook_id,`date`,view_count,vote_count,view_increase,vote_increase)\n" +
            "SELECT t1.id,CURDATE(),0,0,0,0\n" +
            "FROM ebook t1\n" +
            "WHERE not EXISTS(SELECT 1\n" +
            "    from ebook_snapshot t2\n" +
            "    WHERE t1.id = t2.ebook_id\n" +
            "    and t2.`date` = CURDATE()\n" +
            "); " +
            "UPDATE ebook_snapshot t1,ebook t2\n" +
            "SET t1.view_count = t2.view_count,t1.vote_count = t2.vote_count\n" +
            "WHERE t1.`date` = CURDATE()\n" +
            "AND t1.ebook_id = t2.id;" +
            "UPDATE ebook_snapshot t1 LEFT JOIN (SELECT ebook_id,view_count,vote_count FROM ebook_snapshot \n" +
            "WHERE `date` = DATE_SUB(CURDATE(),INTERVAL 1 day)) t2 \n" +
            "ON t1.ebook_id = t2.ebook_id \n" +
            "set t1.view_increase = (t1.view_count - IFNULL(t2.view_count,0)),\n" +
            "        t1.vote_increase = (t1.vote_count - IFNULL(t2.vote_count,0))\n" +
            "WHERE  t1.`date` = CURDATE();")
    void genSnapshot();


    @Select("SELECT\n" +
            "    t1.`date` as `date`,\n" +
            "    SUM(t1.view_count) AS view_count,\n" +
            "    SUM(t1.vote_count) AS vote_count,\n" +
            "    SUM(t1.view_increase) AS view_increase,\n" +
            "    SUM(t1.vote_increase) AS vote_increase\n" +
            "FROM\n" +
            "    ebook_snapshot t1\n" +
            "WHERE\n" +
            "    t1.`date` >= DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "GROUP BY\n" +
            "    t1.`date`\n" +
            "ORDER BY\n" +
            "    t1.`date` DESC")
    List<StatisticResp> getStatistic();
    @Select("SELECT\n" +
            "    t1.`date` as `date`,\n" +
            "    SUM(t1.view_increase) AS view_increase,\n" +
            "    SUM(t1.vote_increase) AS vote_increase\n" +
            "FROM\n" +
            "    ebook_snapshot t1\n" +
            "WHERE\n" +
            "    t1.`date` between DATE_SUB(CURDATE(),INTERVAL 30 day) and DATE_SUB(CURDATE(),INTERVAL 1 day)\n" +
            "GROUP BY\n" +
            "    t1.`date`\n" +
            "ORDER BY\n" +
            "    t1.`date` ASC")
    List<StatisticResp> get30Statistic();


}




