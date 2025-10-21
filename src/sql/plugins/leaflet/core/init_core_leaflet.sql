-- liquibase formatted sql
-- changeset leaflet:init_core_leaflet.sql
-- preconditions onFail:MARK_RAN onError:WARN
INSERT INTO core_datastore VALUES ('leaflet.icon.icons.default.installed', 'true');
INSERT INTO core_datastore VALUES ('leaflet.icon.icons.red.installed', 'true');
INSERT INTO core_datastore VALUES ('leaflet.icon.icons.yellow.installed', 'true');
INSERT INTO core_datastore VALUES ('leaflet.icon.icons.green.installed', 'true');
