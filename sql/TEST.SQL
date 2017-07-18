



/*

服务器，可以是物理机，也可以是虚拟机

*/
create table t_machine(

machine_id,
machine_name
machine_ip,
user_name,
pwd,
remark
);


/*
服务的部署 ，即哪个war部署在哪个服务器上的哪个位置，war包名字，
及服务的起、停、重启等命令
*/
create table t_proj_deploy(
deploy_id,
machine_id,
proj_name,
war_name,
war_path,
start_command,
stop_command,
restart_command
);



create t_online_apply(
appy_id,
online_war_name,
online_war_path,
apply_state
);








