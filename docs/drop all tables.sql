DROP TABLE IF EXISTS SHIFT_ACTIVITIES;
DROP TABLE IF EXISTS SHIFT_CANCELS;
DROP TABLE IF EXISTS SHIFT_ROLES;
DROP TABLE IF EXISTS USER_AUTHORIZATIONS;
DROP TABLE IF EXISTS EMPLOYEE_ROLES;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS EMPLOYEES;
DROP TABLE IF EXISTS SHIFT_WORKERS;
DROP TABLE IF EXISTS SHIFT_REQUESTS;
DROP TABLE IF EXISTS SHIFTS;
DROP TABLE IF EXISTS trucks;
DROP TABLE IF EXISTS truck_drivers;
DROP TABLE IF EXISTS item_lists;
DROP TABLE IF EXISTS item_list_id_counter;
DROP TABLE IF EXISTS item_lists_items;
DROP TABLE IF EXISTS sites;
DROP TABLE IF EXISTS branch_employees;
DROP TABLE IF EXISTS branches;
DROP TABLE IF EXISTS transports;
DROP TABLE IF EXISTS transport_destinations;
DROP TABLE IF EXISTS transport_id_counter;
SELECT DISTINCT tbl_name FROM sqlite_schema;