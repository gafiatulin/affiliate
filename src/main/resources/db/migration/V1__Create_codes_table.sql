create table ref_codes (
    id char(32) NOT NULL PRIMARY KEY,
    visits BIGSERIAL,
    regs BIGSERIAL,
    partner_regs BIGSERIAL
);