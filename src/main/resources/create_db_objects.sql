DROP SCHEMA IF EXISTS demo CASCADE;

IF (NOT EXISTS(SELECT * FROM INFORMATION_SCHEMA.USERS WHERE Lower(name) = 'reader'));
    CREATE USER reader PASSWORD 'reader_password';
    ECHO User reader successfully created
END IF;

IF (NOT EXISTS(SELECT * FROM INFORMATION_SCHEMA.USERS WHERE Lower(name) = 'writer'));
    CREATE USER writer PASSWORD 'writer_password';
    ECHO User writer successfully created
END IF;

CREATE SCHEMA demo;
GRANT SELECT ON SCHEMA demo TO reader;
GRANT ALL ON SCHEMA demo TO writer;
ECHO Schema demo successfully created