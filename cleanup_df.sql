delete from ems_dashboard_tile_params where tile_id in (select tile_id from ems_dashboard_tile where dashboard_Id>1000);
delete from ems_dashboard_tile where dashboard_Id>1000;
delete from ems_dashboard_user_options where dashboard_Id>1000;
delete from ems_dashboard_set where dashboard_set_Id > 1000;
delete from ems_dashboard where dashboard_Id>1000;
delete from ems_preference;
commit;
