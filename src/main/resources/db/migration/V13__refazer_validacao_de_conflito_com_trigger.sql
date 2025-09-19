/**
 * Refazer a validação de conflito de horário com uma função e trigger.
 */

-- Primeiro, remove a constraint antiga, caso ela exista, para evitar conflitos.
ALTER TABLE tb_agendamentos DROP CONSTRAINT IF EXISTS agendamentos_conflito_de_horario_ex;

-- Cria a função que conterá a nossa lógica de validação.
CREATE OR REPLACE FUNCTION verificar_conflito_de_agendamento()
RETURNS TRIGGER AS $$
BEGIN
    -- A verificação só é necessária se estamos a inserir ou atualizar
    -- um agendamento para o status 'CONFIRMADO'.
    IF NEW.status = 'CONFIRMADO' THEN
        -- Verifica se existe algum OUTRO agendamento (a.id <> NEW.id)
        -- para a MESMA SALA (a.sala_id = NEW.sala_id)
        -- que já esteja CONFIRMADO (a.status = 'CONFIRMADO')
        -- e cujo período de tempo se sobreponha ao novo período.
        IF EXISTS (
            SELECT 1
            FROM tb_agendamentos a
            WHERE a.id <> NEW.id
              AND a.sala_id = NEW.sala_id
              AND a.status = 'CONFIRMADO'
              AND a.data_hora_inicio < NEW.data_hora_fim
              AND a.data_hora_fim > NEW.data_hora_inicio
        ) THEN
            -- Se a consulta acima encontrar algum registo, significa que há um conflito.
            -- Lançamos uma exceção com uma mensagem clara.
            RAISE EXCEPTION 'Conflito de horário! Já existe um agendamento confirmado para esta sala neste período.';
        END IF;
    END IF;

    -- Se não houver conflitos, permite que a operação (INSERT ou UPDATE) continue.
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Remove o trigger se ele já existir, para garantir a idempotência.
DROP TRIGGER IF EXISTS trigger_validar_conflito_agendamento ON tb_agendamentos;

-- Cria o trigger que chama a função acima ANTES de cada INSERT ou UPDATE na tabela.
CREATE TRIGGER trigger_validar_conflito_agendamento
BEFORE INSERT OR UPDATE ON tb_agendamentos
FOR EACH ROW EXECUTE FUNCTION verificar_conflito_de_agendamento();