## 创建用户表
create table sys_user(
  user_id int not null auto_increment,
  user_name varchar(40) not null,
  password varchar(128) not null,
  nick_name varchar(40) not null,
  avatar varchar(200) default null,
  sex varchar(1) not null default '男',
  phone varchar(12) default null,
  email varchar(100) default null,
  state int(1) not null default '0',
  create_date timestamp not null,
  update_date timestamp not null default current_timestamp on update current_timestamp,
  primary key (user_id)
);


## 创建角色表
create table sys_role(
  role_id int not null AUTO_INCREMENT,
  role_name varchar(50) not null,
  describes varchar(100) default null,
  status varchar(1) not null default '0',
  create_date TIMESTAMP not null,
  update_date TIMESTAMP not null default current_timestamp on update current_timestamp,
  primary key (role_id)
);

## 创建用户角色表
create table sys_user_role(
  id int not null AUTO_INCREMENT,
  user_id int not null,
  role_id int not null,
  create_date timestamp not null,
  PRIMARY key (id),
  foreign key (role_id) references sys_role(role_id),
  foreign key (user_id) references sys_user(user_id)
);

## 创建权限表
create table sys_permission(
  permission_id int not null AUTO_INCREMENT,
  permission_name varchar(50) not null,
  mark varchar(50) DEFAULT NULL,
  menu_url varchar(50) DEFAULT NULL,
  parent_id int not null default '-1',
  is_menu int not null default '0',
  order_number int not null default '0',
  menu_icon varchar(50) default null,
  create_date timestamp not null,
  update_date timestamp not null default current_timestamp,
  primary key (permission_id)
);

## 创建角色权限表
create table sys_role_permission(
  id int not null AUTO_INCREMENT,
  role_id int not null,
  permission_id int not null,
  create_date timestamp not null,
  PRIMARY key (id),
  foreign key (role_id) references sys_role(role_id),
  foreign key (permission_id) references sys_permission(permission_id)
);

## 创建日志表
create table sys_log(
  id int not null auto_increment,
  user_id int not null,
  os_name varchar(40) default null,
  device varchar(40) default null,
  browser_type varchar(40) default null,
  ip_address varchar(40) default null,
  create_date timestamp not null,
  primary key (id),
  foreign key (user_id) references sys_user(user_id)
)

