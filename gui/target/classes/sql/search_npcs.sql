SELECT *
FROM npcs
WHERE instr(lower(name), lower(?)) > 0
ORDER BY ?;