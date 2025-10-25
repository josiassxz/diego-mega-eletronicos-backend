-- Script de migração para adicionar senha padrão aos vendedores existentes
-- Atualiza todos os vendedores que não possuem senha com uma senha padrão
UPDATE vendedores 
SET senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye/Zo1VK6C/BdSUGorv/7u.B0VXjNA/jC' -- senha padrão: "123456" criptografada
WHERE senha IS NULL OR senha = '';

-- Após a atualização, podemos tornar a coluna NOT NULL novamente
-- (isso será feito em uma próxima migração após confirmar que todos os registros têm senha)