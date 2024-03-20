CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE role AS ENUM ('USER', 'ADMIN');

CREATE TABLE user_roles (
  id UUID NOT NULL,
   created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
   updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
   role_name role,
   description TEXT,
   CONSTRAINT pk_user_roles PRIMARY KEY (id)
);

INSERT INTO user_roles (id, role_name, description)
VALUES
    ('6a1bfb7d-1ec0-4f89-9e5e-8f3a201efc36', 'ADMIN', 'Administrator role with full access privileges'),
    ('7f1d8a9c-5d4e-4a7b-a764-3fdca21a5e9b', 'USER', 'Standard user role with basic access privileges');


CREATE TABLE users (
  id UUID NOT NULL,
   created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
   updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   first_name VARCHAR(255),
   surname VARCHAR(255),
   phone VARCHAR(255),
   mobile VARCHAR(255),
   date_of_birth TIMESTAMP WITHOUT TIME ZONE,
   about_me TEXT,
   profile_photo VARCHAR(255),
   facebook VARCHAR(255),
   twitter VARCHAR(255),
   linkedin VARCHAR(255),
   instagram VARCHAR(255),
   website VARCHAR(255),
   last_login TIMESTAMP WITHOUT TIME ZONE,
   refresh_token TEXT,
   code VARCHAR(255),
   code_expiry BIGINT,
   is_account_non_expired BOOLEAN DEFAULT TRUE,
   is_account_non_locked BOOLEAN DEFAULT TRUE,
   is_credentials_non_expired BOOLEAN DEFAULT TRUE,
   is_enabled BOOLEAN DEFAULT TRUE,
   user_role_id UUID,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users ADD CONSTRAINT uc_users_mobile UNIQUE (mobile);

ALTER TABLE users ADD CONSTRAINT uc_users_phone UNIQUE (phone);

CREATE INDEX idx_email ON users(email);

ALTER TABLE users ADD CONSTRAINT FK_USERS_ON_USER_ROLE FOREIGN KEY (user_role_id) REFERENCES user_roles (id);

