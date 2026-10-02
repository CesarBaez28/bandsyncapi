-- Add the new set lists permissions and assigns them to the administrators

START TRANSACTION;

INSERT IGNORE INTO
    bandsync.permissions (name, type_permission_id)
SELECT requested_permissions.name, permission_type.id
FROM
    bandsync.types_permissions permission_type
    CROSS JOIN (
        SELECT 'Agregar setlist' AS name
        UNION ALL
        SELECT 'Modificar setlist'
        UNION ALL
        SELECT 'Eliminar setlist'
    ) requested_permissions
WHERE
    permission_type.name = 'setlists';

INSERT IGNORE INTO
    bandsync.roles_permissions (role_id, permission_id)
SELECT DISTINCT
    administrator_roles.id,
    permissions.id
FROM
    bandsync.roles administrator_roles
    JOIN bandsync.users_roles user_roles ON user_roles.role_id = administrator_roles.id
    AND user_roles.musical_band_id = administrator_roles.musical_band_id
    JOIN bandsync.permissions permissions ON permissions.name IN (
        'Agregar setlist',
        'Modificar setlist',
        'Eliminar setlist'
    )
WHERE
    administrator_roles.name = 'Administrador';

COMMIT;

SELECT
    permissions.name,
    COUNT(DISTINCT user_roles.user_id) AS administrator_users_granted
FROM
    bandsync.permissions permissions
    LEFT JOIN bandsync.roles_permissions role_permissions ON role_permissions.permission_id = permissions.id
    LEFT JOIN bandsync.roles administrator_roles ON administrator_roles.id = role_permissions.role_id
    AND administrator_roles.name = 'Administrador'
    LEFT JOIN bandsync.users_roles user_roles ON user_roles.role_id = administrator_roles.id
    AND user_roles.musical_band_id = administrator_roles.musical_band_id
WHERE
    permissions.name IN (
        'Agregar setlist',
        'Modificar setlist',
        'Eliminar setlist'
    )
GROUP BY
    permissions.id,
    permissions.name
ORDER BY permissions.name;