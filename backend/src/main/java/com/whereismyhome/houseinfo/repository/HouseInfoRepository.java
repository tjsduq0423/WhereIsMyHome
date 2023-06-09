package com.whereismyhome.houseinfo.repository;

import com.whereismyhome.houseinfo.dto.HousePointDto;
import com.whereismyhome.houseinfo.entity.HouseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HouseInfoRepository extends JpaRepository<HouseInfo, Long> {
    //아파트 이름 + 해당 아파트 북마크 개수
    @Query(value = "select h.apt_code, " +
            "       h.apartment_name, " +
            "       count(b.houseinfo_aptcode) " +
            "from bookmark b " +
            "inner join (" +
            "    select apt_code,apartment_name " +
            "    from houseinfo " +
            "    where apartment_name like %:word% " +
            ") h on h.apt_code = b.houseinfo_aptcode " +
            "group by h.apt_code; ",nativeQuery = true)
    List<Object[]> findByHouseName(@Param("word") String word);

    //구군 + 구군에 속한 아파트 개수
    @Query(value = "select d.gugun_name, " +
            "       count(h.apt_code) as aptCount " +
            "from houseinfo h " +
            "         inner join (select dong_code, gugun_name " +
            "                     from dongcode " +
            "                     where gugun_name like %:word%) d on d.dong_code = h.dong_code " +
            "group by d.gugun_name;", nativeQuery = true)
    List<Object[]> findByGuGunName(@Param("word") String word);

    //아파트 정보 전체 조회
    @Query(value = "select " +
            "h.apt_code, " +
            "h.apartment_name, " +
            "h.lng, " +
            "h.lat, " +
            "h.build_year," +
            "h.avg " +
            "from houseinfo h " +
            "where st_contains(st_buffer(Point(:lng, :lat), :dist), h.local_point)"
            , nativeQuery = true)
    List<Object[]> findByApt(@Param("lng") String lng, @Param("lat") String lat, @Param("dist") double dist);

    @Query("select new com.whereismyhome.houseinfo.dto.HousePointDto(h.lng, h.lat) from HouseInfo h where h.aptCode = :aptCode")
    HousePointDto findByPoint(@Param("aptCode") long aptCode);

    //북마크, 조회수, 거래량 조회 쿼리 사용
    @Query(value = "SELECT COUNT(*) AS amount, h.apt_code as aptCode, hi.viewcount, coalesce(b.bookmark_count, 0) as mark," +
            "       (count(*) + hi.viewcount + coalesce(b.bookmark_count, 0)) as total " +
            "FROM housedeal h " +
            "         JOIN houseinfo hi ON h.apt_code = hi.apt_code " +
            "         JOIN dongcode d ON d.dong_code = hi.dong_code " +
            "         LEFT JOIN ( " +
            "            SELECT houseinfo_aptcode, COUNT(*) AS bookmark_count " +
            "            FROM bookmark " +
            "            GROUP BY houseinfo_aptcode " +
            ") b ON b.houseinfo_aptcode = h.apt_code " +
            "WHERE d.sido_name = :sidoName AND d.gugun_name = :gugunName " +
            "GROUP BY h.apt_code, hi.viewcount, hi.dong, b.bookmark_count " +
            "order by total desc;"
            , nativeQuery = true)
    List<Object[]> makeRank(@Param("sidoName") String sidoName, @Param("gugunName") String gugunName);

    @Query(value = "SELECT d.gugun_name, " +
            "       count(distinct b.houseinfo_aptcode) AS markCount, " +
            "       CAST(sum(distinct h.viewcount)AS SIGNED) AS viewcount, " +
            "       count(hd.apt_code) AS dealAmount, " +
            "       (sum(distinct h.viewcount) + count(hd.apt_code) + count(distinct b.houseinfo_aptcode)) as total " +
            "FROM houseinfo h " +
            "         JOIN dongcode d ON h.dong_code = d.dong_code " +
            "         LEFT JOIN bookmark b ON h.apt_code = b.houseinfo_aptcode " +
            "         LEFT JOIN housedeal hd ON h.apt_code = hd.apt_code " +
            "WHERE d.sido_name = :sidoName " +
            "GROUP BY d.gugun_name " +
            "order by total desc " +
            "limit 15;", nativeQuery = true)
    List<Object[]> chartData(@Param("sidoName") String sidoName);

}
