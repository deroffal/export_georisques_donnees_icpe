INSERT INTO public.etablissement (code_siret, id_inst, nom, date_synchronisation)
VALUES (42175762600013, '0585-03392', 'Nom non-publiable', '2020-11-13 07:57:07.894000');

INSERT INTO public.localisation (etablissement_id, adresse1, adresse2, code_insee, code_postal, commune, departement, region, x, y, date_synchronisation)
VALUES (1, 'LES GORNIERES', null, '85266', '85110', 'ST PROUANT', 'VENDEE', 'PAYS DE LA LOIRE', 395358, 6636496, '2020-11-13 07:57:07.894000');

INSERT INTO public.situation (etablissement_id, activite_nomenclature, alinea, code_nomenclature, famille_nomenclature, date_autorisation, etat_activite, id_regime, regime, seveso, unite, volume, date_synchronisation)
VALUES (1, 'Autres installations que celles visées au 1 ', '1', '2111', 'xxx', null, 1, 'A', 'Autorisation', 'NS', null, '66000.000', '2020-11-13 07:57:07.894000'),
       (1, 'avec plus de 40 000 emplacements pour les volailles', 'a', '3660', '3xxx', null, 1, 'A', 'Autorisation', 'NS', 'u', '66000.000', '2020-11-13 07:57:07.894000');