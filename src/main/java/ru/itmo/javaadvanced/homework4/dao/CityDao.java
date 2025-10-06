package ru.itmo.javaadvanced.homework4.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itmo.javaadvanced.homework4.entity.City;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CityDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<City> rowMapper = (rs, rowNum) -> {
        City city = new City();
        city.setId(rs.getLong("id"));
        city.setCode(rs.getString("code"));
        city.setNameRu(rs.getString("name_ru"));
        city.setNameEn(rs.getString("name_en"));
        city.setPopulation(rs.getLong("population"));
        Long regionId = rs.getLong("region_id");
        city.setRegionId(rs.wasNull() ? null : regionId);
        return city;
    };

    public CityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public City save(City city) {
        String sql = "INSERT INTO cities (code, name_ru, name_en, population, region_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, city.getCode());
            ps.setString(2, city.getNameRu());
            ps.setString(3, city.getNameEn());
            ps.setLong(4, city.getPopulation());
            if (city.getRegionId() != null) {
                ps.setLong(5, city.getRegionId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }
            return ps;
        }, keyHolder);
        
        var keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            city.setId(((Number) keys.get("id")).longValue());
        }
        return city;
    }

    public List<City> findAll() {
        String sql = "SELECT * FROM cities ORDER BY id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<City> findById(Long id) {
        String sql = "SELECT * FROM cities WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public Optional<City> findByCode(String code) {
        String sql = "SELECT * FROM cities WHERE code = ?";
        return jdbcTemplate.query(sql, rowMapper, code).stream().findFirst();
    }

    public List<City> findByRegionId(Long regionId) {
        String sql = "SELECT * FROM cities WHERE region_id = ?";
        return jdbcTemplate.query(sql, rowMapper, regionId);
    }

    public void update(City city) {
        String sql = "UPDATE cities SET code = ?, name_ru = ?, name_en = ?, population = ?, region_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, city.getCode(), city.getNameRu(), city.getNameEn(), 
                           city.getPopulation(), city.getRegionId(), city.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cities WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}