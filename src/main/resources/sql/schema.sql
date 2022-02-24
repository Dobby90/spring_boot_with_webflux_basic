CREATE TABLE public.customer (
	id serial4 NOT NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);

CREATE TABLE public.random_num (
	id serial4 NOT NULL,
	num float8 NULL,
	CONSTRAINT random_num_pkey PRIMARY KEY (id)
);

-- test data
insert into random_num (NUM)
select RANDOM() from pg_catalog.generate_series(1, 1000000)
;
