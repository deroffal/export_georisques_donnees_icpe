create table etablissement
(
    id         bigint not null
        constraint etablissement_pkey
            primary key,
    code_siret bigint,
    id_inst    varchar(255),
    nom        varchar(255)
);

create table localisation
(
    id               bigint not null
        constraint localisation_pkey
            primary key,
    adresse1         varchar(255),
    adresse2         varchar(255),
    code_insee       varchar(255),
    code_postal      varchar(255),
    commune          varchar(255),
    departement      varchar(255),
    region           varchar(255),
    x                bigint,
    y                bigint,
    etablissement_id bigint
        constraint fkhbwm2os7m1ugd0ynbekl6vgx4
            references etablissement
);

create table situation
(
    id                    bigint not null
        constraint situation_pkey
            primary key,
    activite_nomenclature varchar(255),
    alinea                varchar(255),
    code_nomenclature     varchar(255),
    date_autorisation     date,
    etat_activite         integer,
    famille_nomenclature  varchar(255),
    id_regime             varchar(255),
    regime                varchar(255),
    seveso                varchar(255),
    unite                 varchar(255),
    volume                varchar(255),
    etablissement_id      bigint
        constraint fkeh2syjw4hl6yfpuhskl11i7em
            references etablissement
);

create table texte
(
    id               bigint not null
        constraint texte_pkey
            primary key,
    date_doc         date,
    description_doc  varchar(255),
    type_doc         varchar(255),
    url_doc          varchar(255),
    etablissement_id bigint
        constraint fkc9532och9kv7wdq3jjfuu3o09
            references etablissement
);