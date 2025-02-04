DROP SCHEMA api CASCADE;
CREATE SCHEMA api;

CREATE TABLE api.issues_table (
    id SERIAL,
	title VARCHAR(400) NOT NULL,
    body TEXT,
	created_at DATE NOT NULL,
	closed_at DATE NOT NULL,
	days_to_close BIGINT NOT NULL,
	issue_priority VARCHAR(20) NOT NULL,
	milestone VARCHAR(50),
	author_name VARCHAR(100),
	user_atribute_to_resolve_issue VARCHAR(100),
    issue_classification VARCHAR(200),
	CONSTRAINT pk_issues_table PRIMARY KEY (id)
)