CREATE DATABASE polynames;
USE polynames;

CREATE TABLE player (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `role` ENUM('spymaster', 'operative'),
    pseudo VARCHAR(50) NOT NULL,
    id_game INT NOT NULL
);

CREATE TABLE game (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    score INT DEFAULT 0 NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    `status` ENUM('waiting', 'pending', 'done') DEFAULT 'waiting' NOT NULL,
    current_player ENUM('spymaster', 'operative') DEFAULT 'spymaster' NOT NULL
);

CREATE TABLE turn (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    hint VARCHAR(50),
    score INT DEFAULT 0 NOT NULL,
    `status` ENUM('pending', 'done') DEFAULT 'pending' NOT NULL,
    hint_count INT DEFAULT 0 NOT NULL,
    discovered_cards INT DEFAULT 0 NOT NULL,
    id_game INT NOT NULL
);

CREATE TABLE card (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    position INT NOT NULL,
    `status` ENUM('visible', 'hidden') DEFAULT 'hidden' NOT NULL,
    id_game INT NOT NULL,
    id_color INT NOT NULL,
    id_word INT NOT NULL
);

CREATE TABLE turnCard (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    id_card INT NOT NULL,
    id_turn INT NOT NULL
);

CREATE TABLE word (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `text` VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE color (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `name` VARCHAR(50) UNIQUE NOT NULL
);

ALTER TABLE player
ADD CONSTRAINT fk_player_game FOREIGN KEY (id_game) REFERENCES game(id);

ALTER TABLE turn
ADD CONSTRAINT fk_turn_game FOREIGN KEY (id_game) REFERENCES game(id);

ALTER TABLE card
ADD CONSTRAINT fk_card_game FOREIGN KEY (id_game) REFERENCES game(id),
ADD CONSTRAINT fk_card_color FOREIGN KEY (id_color) REFERENCES color(id),
ADD CONSTRAINT fk_card_word FOREIGN KEY (id_word) REFERENCES word(id);

ALTER TABLE turnCard
ADD CONSTRAINT fk_turnCard_card FOREIGN KEY (id_card) REFERENCES card(id),
ADD CONSTRAINT fk_turnCard_turn FOREIGN KEY (id_turn) REFERENCES turn(id);


INSERT INTO word (`text`) VALUES ('apple'), ('banana'), ('grape'), ('orange'),
('peach'), ('pear'), ('plum'), ('kiwi'), ('lemon'), ('lime'), ('mango'),
('melon'), ('nectarine'), ('papaya'), ('pineapple'), ('pomegranate'), ('raspberry'),
('strawberry'), ('watermelon'), ('blueberry'), ('blackberry'), ('cherry'),
('coconut'), ('fig'), ('guava'), ('jackfruit'), ('lychee'), ('mandarin'), ('olive'),
('passionfruit'), ('persimmon'), ('quince'), ('rhubarb'), ('tangerine'), ('ugli'),
('yuzu'), ('apricot'), ('avocado'), ('bilberry'), ('boysenberry'), ('currant'),
('damson'), ('date'), ('elderberry'), ('gooseberry'), ('honeydew'), ('jambul'),
('kumquat'), ('loquat'), ('mulberry');

INSERT INTO color (`name`) VALUES ('blue'), ('gray'), ('black');
