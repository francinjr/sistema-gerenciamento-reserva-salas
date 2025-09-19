/**
 *
 * Esta migration implementa a lógica de negócio de atualização do caixa diretamente
 * no banco de dados, quando o status de um agendamento muda para CONFIRMADO ou FINALIZADO
 */

-- Cria a função que será executada pelo trigger
CREATE OR REPLACE FUNCTION atualizar_caixa_sala_trigger_func()
RETURNS TRIGGER AS $$
BEGIN
    -- A lógica só é executada se a operação for um UPDATE e o status tiver mudado
    IF (TG_OP = 'UPDATE' AND NEW.status <> OLD.status) THEN

        -- Se o novo status for CONFIRMADO, soma o valor do sinal ao caixa DA SALA
        IF (NEW.status = 'CONFIRMADO') THEN
UPDATE tb_salas
SET caixa = caixa + NEW.valor_sinal
WHERE id = NEW.sala_id;

-- Se o novo status for FINALIZADO, soma o valor de finalização ao caixa DA SALA
ELSIF (NEW.status = 'FINALIZADO') THEN
UPDATE tb_salas
SET caixa = caixa + NEW.valor_finalizacao
WHERE id = NEW.sala_id;
END IF;
END IF;

    -- Retorna o registro modificado para que o UPDATE original prossiga
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Remove o trigger se ele já existir, para garantir a idempotência
DROP TRIGGER IF EXISTS atualizar_caixa_sala_trigger ON tb_agendamentos;

-- Cria o trigger que chama a função acima DEPOIS de cada UPDATE na tabela de agendamentos
CREATE TRIGGER atualizar_caixa_sala_trigger
    AFTER UPDATE ON tb_agendamentos
    FOR EACH ROW EXECUTE FUNCTION atualizar_caixa_sala_trigger_func();