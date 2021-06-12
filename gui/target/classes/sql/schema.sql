PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS npc_types;
CREATE TABLE npc_types (
    name TEXT PRIMARY KEY,
    hostile INTEGER NOT NULL DEFAULT FALSE,
    race TEXT NOT NULL DEFAULT 'Human'
);

DROP TABLE IF EXISTS npcs;
CREATE TABLE npcs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL,
    profile_image_path TEXT NULL DEFAULT NULL,
    type TEXT NOT NULL,
    description TEXT NOT NULL,
    health REAL NOT NULL DEFAULT 10.0,
    attack_strength REAL NOT NULL DEFAULT 5.0,
    defense_strength REAL NOT NULL DEFAULT 5.0,
    CONSTRAINT fk_npcs_type FOREIGN KEY (type) REFERENCES npc_types(name)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

INSERT INTO npc_types (name, hostile, race) VALUES
('Trader', FALSE, 'Human'),
('Warrior', TRUE, 'Human'),
('Archer', TRUE, 'Elf'),
('Ranger', FALSE, 'Elf'),
('Miner', FALSE, 'Dwarf'),
('Brewer', FALSE, 'Hobbit');

INSERT INTO npcs (created_at, name, profile_image_path, type, description, health, attack_strength, defense_strength) VALUES
('1931-03-17T14:02:00', 'Fëanor', 'andrew_caesar_bust.png', 'Warrior', 'Deposed leader of the Noldor elves of Valinor, and creator of the Silmarils.', 120, 67, 150),
('1942-06-01T05:47:37', 'Finwë', 'Andrew_Kenobi.png', 'Ranger', 'Father of Fëanor, and first king of the Noldor.', 60, 30, 89),
('1978-07-21T19:12:08', 'Celebrimbor', 'gandrew.png', 'Archer', 'Grandson of Fëanor, and forger of the rings of power of Middle Earth.', 75, 55, 72),
('1934-12-29T22:55:00', 'Durin', 'andrew_swolo.png', 'Miner', 'First king of the mines of Khazad-dûm (Moria), oldest of the seven fathers of the dwarves.', 70, 40, 180),
('1982-02-27T09:45:54', 'Elendil', 'andrew_mckinley.png', 'Trader', 'First high king of the exiled Númenóreans in Middle Earth following the betrayal of Ar-Pharazôn, reigning king of Númenór.', 175, 132, 168),
('1982-05-06T13:25:12', 'Aragorn II', 'andrew_jefferson.png', 'Warrior', '35th King of Gondor, following the War of the Ring.', 175, 132, 168),
('2001-01-24T08:03:44', 'Meriadoc Brandybuck', 'galiledrew.jpeg', 'Brewer', 'One of the nine companions of the Fellowship of the Ring, and eighth Master of Buckland.', 50, 15, 35);
