package br.com.fiap.sustentabilidade.service;

import br.com.fiap.sustentabilidade.dto.RankingSetorItem;
import br.com.fiap.sustentabilidade.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
public class RankingService {

    private final JdbcTemplate jdbc;

    public RankingService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<RankingSetorItem> listar(String deIso, String ateIso) {
        // período padrão = últimos 30 dias
        LocalDate deDia, ateDia;
        if ((deIso == null || deIso.isBlank()) && (ateIso == null || ateIso.isBlank())) {
            ateDia = LocalDate.now();
            deDia  = ateDia.minusDays(30);
        } else {
            try {
                deIso = (deIso == null) ? "" : deIso;
                ateIso = (ateIso == null) ? "" : ateIso;
                deDia = deIso.isBlank() ? null : LocalDate.parse(deIso);
                ateDia = ateIso.isBlank() ? null : LocalDate.parse(ateIso);
            } catch (Exception e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Datas inválidas. Use yyyy-MM-dd");
            }
            if (deDia == null && ateDia != null) deDia = ateDia.minusDays(30);
            if (ateDia == null && deDia != null)  ateDia = deDia.plusDays(30);
        }
        if (deDia.isAfter(ateDia)) throw new ApiException(HttpStatus.BAD_REQUEST, "`de` não pode ser maior que `ate`");

        var deTs  = Timestamp.valueOf(deDia.atStartOfDay());
        var ateTs = Timestamp.valueOf(ateDia.atTime(LocalTime.MAX));

        // Soma de consumo por setor no período (LEFT JOIN para setor sem consumo continuar aparecendo)
        String sql = """
            SELECT
                s.ID_SETOR,
                s.NOME_SETOR,
                NVL(s.META_CONSUMO_KWH, 0) AS META_CONSUMO_KWH,
                NVL(SUM(ce.CONSUMO_KWH), 0) AS CONSUMO_TOTAL_KWH
            FROM SETORES s
            LEFT JOIN EQUIPAMENTOS e
                   ON e.ID_SETOR = s.ID_SETOR
            LEFT JOIN CONSUMO_ENERGIA ce
                   ON ce.ID_EQUIPAMENTO = e.ID_EQUIPAMENTO
                  AND ce.DATA_HORA_LEITURA BETWEEN ? AND ?
            WHERE s.ATIVO = 1
            GROUP BY s.ID_SETOR, s.NOME_SETOR, s.META_CONSUMO_KWH
            """;

        var rows = jdbc.query(sql, (rs, i) -> {
            var item = new RankingSetorItem();
            item.setor_id = rs.getLong("ID_SETOR");
            item.nome_setor = rs.getString("NOME_SETOR");
            item.meta_consumo_kwh = rs.getDouble("META_CONSUMO_KWH");
            item.consumo_total_kwh = rs.getDouble("CONSUMO_TOTAL_KWH");

            if (item.meta_consumo_kwh > 0) {
                item.eficiencia_percentual =
                        ((item.meta_consumo_kwh - item.consumo_total_kwh) / item.meta_consumo_kwh) * 100.0;
                // classificação
                double limite80 = item.meta_consumo_kwh * 0.8;
                if (item.consumo_total_kwh <= limite80) item.classificacao = "EXCELENTE";
                else if (item.consumo_total_kwh <= item.meta_consumo_kwh) item.classificacao = "BOM";
                else if (item.consumo_total_kwh <= item.meta_consumo_kwh * 1.2) item.classificacao = "REGULAR";
                else item.classificacao = "CRITICO";
            } else {
                item.eficiencia_percentual = 0.0;
                item.classificacao = "SEM_META";
            }
            return item;
        }, deTs, ateTs);

        // Ordena por quem mais "sobra" da meta (meta - consumo), depois por eficiência desc
        rows.sort(Comparator.comparingDouble(
                        r -> -1.0 * (r.meta_consumo_kwh - r.consumo_total_kwh))
                // o comparator acima ordena ASC; multiplicamos por -1 para ficar DESC
        );
        // (alternativa mais clara)
        rows.sort((a, b) -> {
            double scoreA = a.meta_consumo_kwh - a.consumo_total_kwh;
            double scoreB = b.meta_consumo_kwh - b.consumo_total_kwh;
            int cmp = Double.compare(scoreB, scoreA);
            if (cmp != 0) return cmp;
            return Double.compare(b.eficiencia_percentual, a.eficiencia_percentual);
        });

        // atribuir ranking 1..n
        int pos = 1;
        for (var r : rows) r.ranking = pos++;

        return rows;
    }
}
