-- Permissions of the system
INSERT INTO bandsync.permissions (name) VALUES 
('Integrantes'), 
('Agregar integrante'),
('Modificar integrante'),
('Eliminar integrante'),
('Repertorios'), 
('Agregar repertorio'),
('Modificar repertorio'),
('Eliminar repertorio'),
('Eventos'), 
('Agregar evento'),
('Modificar evento'),
('Eliminar evento'),
('Artistas'), 
('Agregar artista'),
('Modificar artista'),
('Eliminar artista'),
('Canciones'), 
('Agregar cancion'),
('Modificar cancion'),
('Eliminar cancion'),
('Calendario'),
('Roles y permisos'),
('Agregar rol'),
('Modificar rol'),
('Eliminar rol'),
('Generos musicales'),
('Agregar genero musical'),
('Modificar genero musical'),
('Eliminar genero musical'),
('Roles musicales'),
('Agregar rol musical'),
('Modificar rol musical'),
('Eliminar rol musical');

-- Users status
INSERT INTO bandsync.users_status (name) VALUES 
('ACTIVE'),
('PLACEHOLDER'),
('DEACTIVATED');

-- Users musical bands status
INSERT INTO bandsync.users_musical_bands_status (name) VALUES 
('ACTIVE'),
('PENDING_ACTIVATION'),
('INACTIVE');