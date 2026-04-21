package br.com.fiap.sustentabilidade.service;

import br.com.fiap.sustentabilidade.dto.IndicadoresResponse;
import br.com.fiap.sustentabilidade.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class IndicadoresService {

    private final JdbcTemplate jdbc;

    public IndicadoresService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public IndicadoresResponse calcular(String deIso, String ateIso) {
        // datas (default: últimos 30 dias, [de 00:00:00, ate 23:59:59.999])
        LocalDate deDia, ateDia;
        if (deIso == null && ateIso == null) {
            ateDia = LocalDate.now();
            deDia  = ateDia.minusDays(30);
        } else {
            try {
                deDia = (deIso == null || deIso.isBlank()) ? null : LocalDate.parse(deIso);
                ateDia = (ateIso == null || ateIso.isBlank()) ? null : LocalDate.parse(ateIso);
            } catch (Exception e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Datas inválidas. Use yyyy-MM-dd");
            }
            if (deDia == null && ateDia != null) deDia = ateDia.minusDays(30);
            if (ateDia == null && deDia != null)  ateDia = deDia.plusDays(30);
        }
        if (deDia.isAfter(ateDia)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "`de` não pode ser maior que `ate`");
        }

        LocalDateTime de = deDia.atStartOfDay();
        LocalDateTime ate = ateDia.atTime(LocalTime.MAX);

        var deTs  = Timestamp.valueOf(de);
        var ateTs = Timestamp.valueOf(ate);

        // consumo total
        Double consumo = jdbc.queryForObject(
                "SELECT NVL(SUM(CONSUMO_KWH), 0) FROM CONSUMO_ENERGIA WHERE DATA_HORA_LEITURA BETWEEN ? AND ?",
                Double.class, deTs, ateTs);

        // geração total e CO2
        Double[] gen = jdbc.query(con -> {
            var ps = con.prepareStatement(
                    "SELECT NVL(SUM(ENERGIA_GERADA_KWH),0), NVL(SUM(ECONOMIA_CO2_KG),0) " +
                            "FROM GERACAO_SOLAR WHERE DATA_HORA_LEITURA BETWEEN ? AND ?");
            ps.setTimestamp(1, deTs);
            ps.setTimestamp(2, ateTs);
            return ps;
        }, rs -> {
            if (rs.next()) {
                return new Double[]{rs.getDouble(1), rs.getDouble(2)};
            }
            return new Double[]{0d, 0d};
        });

        double geracao = gen[0];
        double co2     = gen[1];

        var resp = new IndicadoresResponse();
        resp.de  = deDia.toString();
        resp.ate = ateDia.toString();
        resp.consumo_total_kwh = consumo != null ? consumo : 0d;
        resp.geracao_total_kwh = geracao;
        resp.saldo_kwh = resp.geracao_total_kwh - resp.consumo_total_kwh;
        resp.taxa_compensacao = (resp.consumo_total_kwh > 0)
                ? (resp.geracao_total_kwh / resp.consumo_total_kwh)
                : null;
        resp.economia_co2_kg = co2;

        return resp;
    }
}
