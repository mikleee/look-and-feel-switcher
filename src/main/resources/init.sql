CREATE TABLE bindThemes
(
id int NOT NULL AUTO_INCREMENT,
userId int NOT NULL,
themeId varchar(255) NOT NULL,
groupId int NOT NULL,
colorSchemeId varchar(255),
PRIMARY KEY (id)
)