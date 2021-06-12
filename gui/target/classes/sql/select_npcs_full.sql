SELECT DISTINCT
    npcs.id AS id,
    npcs.created_at AS created_at,
    npcs.name AS name,
    npcs.description AS description,
    npcs.health AS health,
    npcs.attack_strength AS attack_strength,
    npcs.defense_strength AS defense_strength,
    nt.name AS type_name,
    nt.hostile AS type_hostile,
    nt.race AS type_race
FROM npcs
LEFT JOIN npc_types nt ON npcs.type = nt.name
ORDER BY npcs.created_at;