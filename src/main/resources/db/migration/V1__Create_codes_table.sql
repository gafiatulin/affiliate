create table "partners" ("id" char(24) NOT NULL, PRIMARY KEY ("id"), UNIQUE ("id"));
create table "actions" ("id" bigserial NOT NULL, "partner" char(24) NOT NULL, "actionType" smallint, PRIMARY KEY ("id"), FOREIGN KEY ("partner") REFERENCES "partners"("id") ON DELETE CASCADE ON UPDATE CASCADE);
