create table ref_codes (
    id char(32) NOT NULL PRIMARY KEY,
    referer char(32),
    visits BIGSERIAL,
    regs BIGSERIAL,
    partner_regs BIGSERIAL
);