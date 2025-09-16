INSERT INTO training_type (id, training_type_name)
VALUES (gen_random_uuid(), 'Weightlifting')
ON CONFLICT (training_type_name) DO NOTHING;

INSERT INTO training_type (id, training_type_name)
VALUES (gen_random_uuid(), 'Cardio')
ON CONFLICT (training_type_name) DO NOTHING;

INSERT INTO training_type (id, training_type_name)
VALUES (gen_random_uuid(), 'Yoga')
ON CONFLICT (training_type_name) DO NOTHING;

INSERT INTO training_type (id, training_type_name)
VALUES (gen_random_uuid(), 'Pilates')
ON CONFLICT (training_type_name) DO NOTHING;

INSERT INTO training_type (id, training_type_name)
VALUES (gen_random_uuid(), 'Crossfit')
ON CONFLICT (training_type_name) DO NOTHING;