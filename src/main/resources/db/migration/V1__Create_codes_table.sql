create table "partners" ("id" char(40) NOT NULL, PRIMARY KEY ("id"), UNIQUE ("id"));
create table "actions" ("id" uuid NOT NULL, "partner" char(40) NOT NULL, "actionType" smallint, "time" timestamp, PRIMARY KEY ("id"), FOREIGN KEY ("partner") REFERENCES "partners"("id") ON DELETE CASCADE ON UPDATE CASCADE);
