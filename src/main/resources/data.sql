-- Clean orphaned FK values before Hibernate applies/updates constraints.
UPDATE users u
SET city_id = NULL
WHERE u.city_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM city c
      WHERE c.id = u.city_id
  );

UPDATE users u
SET gender_id = NULL
WHERE u.gender_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM gender g
      WHERE g.id = u.gender_id
  );

UPDATE users u
SET status_id = NULL
WHERE u.status_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM status s
      WHERE s.id = u.status_id
  );
