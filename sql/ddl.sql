create table board.board (
	id int not null auto_increment,
    title VARCHAR(45) not null,
    content text not null,
    primary key(id));