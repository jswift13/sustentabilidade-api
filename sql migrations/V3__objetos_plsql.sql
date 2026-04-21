ALTER SESSION SET CURRENT_SCHEMA=ESG;

-- TRIGGER: alerta automático de consumo anormal
CREATE OR REPLACE TRIGGER TRG_ALERTA_CONSUMO_ANORMAL
BEFORE INSERT ON CONSUMO_ENERGIA
FOR EACH ROW
DECLARE
    v_media_consumo NUMBER;
    v_potencia_equip NUMBER;
BEGIN
    SELECT POTENCIA_WATTS INTO v_potencia_equip
      FROM EQUIPAMENTOS
     WHERE ID_EQUIPAMENTO = :NEW.ID_EQUIPAMENTO;

    v_media_consumo := (v_potencia_equip * :NEW.PERIODO_HORAS) / 1000;

    IF :NEW.CONSUMO_KWH > (v_media_consumo * 1.2) THEN
        :NEW.ALERTA_EXCESSO := 1;
        DBMS_OUTPUT.PUT_LINE('ALERTA: Equipamento ' || :NEW.ID_EQUIPAMENTO ||
                             ' com consumo anormal: ' || :NEW.CONSUMO_KWH || ' kWh');
    ELSE
        :NEW.ALERTA_EXCESSO := 0;
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        :NEW.ALERTA_EXCESSO := 0;
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Erro ao verificar consumo: ' || SQLERRM);
END;

-- (a barra acima é obrigatória para compilar o bloco)

-- PROCEDURE: cálculo de eficiência por setor (últimos 30 dias)
CREATE OR REPLACE PROCEDURE PROC_CALCULAR_EFICIENCIA_SETORES AS
    CURSOR c_setores IS
        SELECT ID_SETOR, NOME_SETOR, META_CONSUMO_KWH
          FROM SETORES
         WHERE ATIVO = 1;

    v_consumo_real NUMBER;
    v_eficiencia   NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('========================================');
    DBMS_OUTPUT.PUT_LINE('  RELATORIO DE EFICIENCIA ENERGETICA');
    DBMS_OUTPUT.PUT_LINE('========================================');

    FOR setor IN c_setores LOOP
        SELECT NVL(SUM(ce.CONSUMO_KWH), 0)
          INTO v_consumo_real
          FROM CONSUMO_ENERGIA ce
          JOIN EQUIPAMENTOS e ON ce.ID_EQUIPAMENTO = e.ID_EQUIPAMENTO
         WHERE e.ID_SETOR = setor.ID_SETOR
           AND ce.DATA_HORA_LEITURA >= ADD_MONTHS(SYSDATE, -1);

        IF setor.META_CONSUMO_KWH > 0 THEN
            v_eficiencia := ((setor.META_CONSUMO_KWH - v_consumo_real) / setor.META_CONSUMO_KWH) * 100;
        ELSE
            v_eficiencia := 0;
        END IF;

        DBMS_OUTPUT.PUT_LINE('Setor: ' || setor.NOME_SETOR);
        DBMS_OUTPUT.PUT_LINE('  Meta Mensal: ' || setor.META_CONSUMO_KWH || ' kWh');
        DBMS_OUTPUT.PUT_LINE('  Consumo Real: ' || ROUND(v_consumo_real, 2) || ' kWh');
        DBMS_OUTPUT.PUT_LINE('  Eficiencia: ' || ROUND(v_eficiencia, 2) || '%');
        DBMS_OUTPUT.PUT_LINE(CASE WHEN v_eficiencia >= 0 THEN '  Status: META ATINGIDA'
                                  ELSE '  Status: ACIMA DA META' END);
        DBMS_OUTPUT.PUT_LINE('----------------------------------------');
    END LOOP;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20002, 'Erro ao calcular eficiencia: ' || SQLERRM);
END;


-- TRIGGER: cálculo automático de economia de CO2 na geração
CREATE OR REPLACE TRIGGER TRG_CALCULAR_ECONOMIA_CO2
BEFORE INSERT ON GERACAO_SOLAR
FOR EACH ROW
DECLARE
    v_fator_emissao CONSTANT NUMBER := 0.0817; -- kg CO2 por kWh (aprox.)
BEGIN
    :NEW.ECONOMIA_CO2_KG := :NEW.ENERGIA_GERADA_KWH * v_fator_emissao;

    IF :NEW.IRRADIACAO_SOLAR > 0 THEN
        :NEW.EFICIENCIA_PERCENTUAL := (:NEW.ENERGIA_GERADA_KWH / :NEW.IRRADIACAO_SOLAR) * 100;
    END IF;

    DBMS_OUTPUT.PUT_LINE('Economia CO2: ' || ROUND(:NEW.ECONOMIA_CO2_KG, 2) || ' kg');
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'Erro ao calcular CO2: ' || SQLERRM);
END;

-- VIEW: ranking de setores
CREATE OR REPLACE VIEW VW_RANKING_SUSTENTABILIDADE AS
SELECT 
    s.NOME_SETOR,
    ROUND(SUM(ce.CONSUMO_KWH), 2) AS CONSUMO_TOTAL_KWH,
    s.META_CONSUMO_KWH,
    ROUND(((s.META_CONSUMO_KWH - SUM(ce.CONSUMO_KWH)) / s.META_CONSUMO_KWH) * 100, 2) AS EFICIENCIA_PERCENTUAL,
    CASE 
        WHEN SUM(ce.CONSUMO_KWH) <= s.META_CONSUMO_KWH * 0.8 THEN 'EXCELENTE'
        WHEN SUM(ce.CONSUMO_KWH) <= s.META_CONSUMO_KWH THEN 'BOM'
        WHEN SUM(ce.CONSUMO_KWH) <= s.META_CONSUMO_KWH * 1.2 THEN 'REGULAR'
        ELSE 'CRITICO'
    END AS CLASSIFICACAO,
    RANK() OVER (ORDER BY (s.META_CONSUMO_KWH - SUM(ce.CONSUMO_KWH)) DESC) AS RANKING
FROM SETORES s
JOIN EQUIPAMENTOS e ON s.ID_SETOR = e.ID_SETOR
JOIN CONSUMO_ENERGIA ce ON e.ID_EQUIPAMENTO = ce.ID_EQUIPAMENTO
WHERE s.ATIVO = 1
  AND ce.DATA_HORA_LEITURA >= ADD_MONTHS(SYSDATE, -1)
GROUP BY s.ID_SETOR, s.NOME_SETOR, s.META_CONSUMO_KWH
ORDER BY RANKING;

COMMIT;
