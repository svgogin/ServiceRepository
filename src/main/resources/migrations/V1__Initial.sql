CREATE TABLE IF NOT EXISTS company
(
    id             bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    inn            text NOT NULL UNIQUE,
    ogrn           text,
    kpp            text,
    full_name_rus  text,
    short_name_rus text,
    status_name    text DEFAULT 'Actual'::text,
    status_date    date
);
