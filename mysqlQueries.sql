ALTER TABLE amirDB.task
DROP CONSTRAINT task_chk_1

ALTER TABLE amirdb.task
MODIFY task_status VARCHAR(40);

ALTER TABLE amirDB.task
ADD CONSTRAINT ch_task_status  CHECK (task_status in ('NEW','IN_PROGRESS','DONE','CANCELLED'));


ALTER TABLE employee
MODIFY job_title ENUM('AI','Backend','Devops','Frontend','UIUX','HR');

ALTER TABLE employee_attendance
MODIFY ATTENDANCE_STATUS ENUM('ABSENT','ATTENDED','DEDUCTED','OVERTIME','OFF')



ALTER TABLE vacation_request_history
ADD CONSTRAINT fr_key_vacation_request_id
FOREIGN KEY (vacation_request_id)
REFERENCES vacation_request(vacation_request_id)
ON DELETE CASCADE
ON UPDATE CASCADE

ALTER TABLE Vacation_request
DROP CONSTRAINT FK3x4acxfn5jshn9x92cwixj46m;
ALTER TABLE Vacation_request
DROP CONSTRAINT FK84i65e2jkkn5tvflt50smsga0;
ALTER TABLE Vacation_request
DROP CONSTRAINT FKg33enciyxv9dbwdajxeyp1eh3;

------------ apply ON DELETE and ON UPDATE on foreign keys.

ALTER TABLE vacation_request
MODIFY dep_manager_id bigint ;           ---  if this column prevent null values --> make it nullable.


-- then apply ON DELETE and ON UPDATE on foreign keys.
ALTER TABLE Vacation_request
ADD CONSTRAINT fr_key_dep_manager_id
FOREIGN KEY (dep_manager_id)
REFERENCES employee(employee_id)
ON DELETE SET NULL
ON UPDATE CASCADE



