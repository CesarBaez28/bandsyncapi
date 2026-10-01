-- Delete the column repertoire_id from table events

SET @repertoire_fk_name = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'events'
      AND COLUMN_NAME = 'repertoire_id'
      AND REFERENCED_TABLE_NAME = 'repertoires'
    LIMIT 1
);

SET @drop_repertoire_fk_sql = IF(
    @repertoire_fk_name IS NULL,
    'SELECT 1',
    CONCAT('ALTER TABLE events DROP FOREIGN KEY `', @repertoire_fk_name, '`')
);

PREPARE drop_repertoire_fk FROM @drop_repertoire_fk_sql;
EXECUTE drop_repertoire_fk;
DEALLOCATE PREPARE drop_repertoire_fk;

ALTER TABLE events
    DROP COLUMN repertoire_id;

SELECT COUNT(*) AS preserved_event_count FROM events;