DROP TABLE IF EXISTS configs;

CREATE TABLE configs
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    config_name        VARCHAR(250) NOT NULL,
    monitoring_enabled VARCHAR(250) NOT NULL,
    cpu_enabled        VARCHAR(250) DEFAULT NULL,
    cpu_value          VARCHAR(250) DEFAULT NULL
);
