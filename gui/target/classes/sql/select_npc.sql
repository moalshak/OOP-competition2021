SELECT
    npcs.id,
    npcs.created_at,
    npcs.name,
    npcs.profile_image_path,
    npcs.description,
    npcs.health,
    npcs.attack_strength,
    npcs.defense_strength,
    npc_types.name AS type_name,
    npc_types.hostile AS type_hostile,
    npc_types.race AS type_race
FROM npcs
LEFT JOIN npc_types ON npcs.type = npc_types.name
WHERE npcs.id = ?;