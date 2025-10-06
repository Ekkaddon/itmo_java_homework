package ru.itmo.javaadvanced.homework4.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itmo.javaadvanced.homework4.entity.Region;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class RegionDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Region> rowMapper = (rs, rowNum) -> {
        Region region = new Region();
        region.setId(rs.getLong("id"));
        region.setCode(rs.getString("code"));
        region.setNameRu(rs.getString("name_ru"));
        region.setNameEn(rs.getString("name_en"));
        return region;
    };

    public RegionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Region save(Region region) {
        String sql = "INSERT INTO regions (code, name_ru, name_en) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, region.getCode());
            ps.setString(2, region.getNameRu());
            ps.setString(3, region.getNameEn());
            return ps;
        }, keyHolder);
        
        var keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            region.setId(((Number) keys.get("id")).longValue());
        }
        return region;
    }

    public List<Region> findAll() {
        String sql = "SELECT * FROM regions ORDER BY id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Region findById(Long id) {
        String sql = "SELECT * FROM regions WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst().orElse(null);
    }

    public Region findByCode(String code) {
        String sql = "SELECT * FROM regions WHERE code = ?";
        return jdbcTemplate.query(sql, rowMapper, code).stream().findFirst().orElse(null);
    }

    public void update(Region region) {
        String sql = "UPDATE regions SET code = ?, name_ru = ?, name_en = ? WHERE id = ?";
        jdbcTemplate.update(sql, region.getCode(), region.getNameRu(), region.getNameEn(), region.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM regions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}