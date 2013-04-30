--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET search_path = public, pg_catalog;

--
-- Name: getboardid(text); Type: FUNCTION; Schema: public; Owner: alumnodb
--

CREATE FUNCTION getboardid(text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
    bn ALIAS FOR $1;
    fila boards%ROWTYPE;
BEGIN
    SELECT * INTO fila FROM boards WHERE boardname=bn;
    RETURN fila.boardid;
END;
$_$;


ALTER FUNCTION public.getboardid(text) OWNER TO alumnodb;

--
-- Name: topten(text); Type: FUNCTION; Schema: public; Owner: alumnodb
--

CREATE FUNCTION topten(text) RETURNS TABLE(usuario character varying, npiezas integer, duracion integer, fecha date)
    LANGUAGE plpgsql
    AS $_$
DECLARE
    bn ALIAS FOR $1;
BEGIN

RETURN query SELECT u.username as usuario, s.npiezas, s.duracion, s.fecha 
             FROM scores AS s, usuarios AS u, boards AS b 
             WHERE u.playerid=s.playerid AND b.boardid=s.boardid AND
                   b.boardname = bn
             ORDER BY s.npiezas ASC, s.duracion ASC
             LIMIT 10;
END;
$_$;


ALTER FUNCTION public.topten(text) OWNER TO alumnodb;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: boards; Type: TABLE; Schema: public; Owner: alumnodb; Tablespace: 
--

CREATE TABLE boards (
    boardid integer NOT NULL,
    board character varying(50) NOT NULL,
    boardname character varying(128) NOT NULL
);


ALTER TABLE public.boards OWNER TO alumnodb;

--
-- Name: board_boardid_seq; Type: SEQUENCE; Schema: public; Owner: alumnodb
--

CREATE SEQUENCE board_boardid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.board_boardid_seq OWNER TO alumnodb;

--
-- Name: board_boardid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alumnodb
--

ALTER SEQUENCE board_boardid_seq OWNED BY boards.boardid;


--
-- Name: mensajes; Type: TABLE; Schema: public; Owner: alumnodb; Tablespace: 
--

CREATE TABLE mensajes (
    destinatario character varying NOT NULL,
    mensaje character(140),
    fecha timestamp without time zone,
    enviado boolean DEFAULT false,
    remitente uuid
);


ALTER TABLE public.mensajes OWNER TO alumnodb;

--
-- Name: scores; Type: TABLE; Schema: public; Owner: alumnodb; Tablespace: 
--

CREATE TABLE scores (
    playerid uuid NOT NULL,
    gameid integer NOT NULL,
    boardid integer DEFAULT 1 NOT NULL,
    npiezas integer NOT NULL,
    fecha date NOT NULL,
    duracion integer NOT NULL
);


ALTER TABLE public.scores OWNER TO alumnodb;

--
-- Name: scores_gameid_seq; Type: SEQUENCE; Schema: public; Owner: alumnodb
--

CREATE SEQUENCE scores_gameid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.scores_gameid_seq OWNER TO alumnodb;

--
-- Name: scores_gameid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alumnodb
--

ALTER SEQUENCE scores_gameid_seq OWNED BY scores.gameid;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: alumnodb; Tablespace: 
--

CREATE TABLE usuarios (
    playerid uuid NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    descripcion character varying(128)
);


ALTER TABLE public.usuarios OWNER TO alumnodb;

--
-- Name: boardid; Type: DEFAULT; Schema: public; Owner: alumnodb
--

ALTER TABLE ONLY boards ALTER COLUMN boardid SET DEFAULT nextval('board_boardid_seq'::regclass);


--
-- Name: gameid; Type: DEFAULT; Schema: public; Owner: alumnodb
--

ALTER TABLE ONLY scores ALTER COLUMN gameid SET DEFAULT nextval('scores_gameid_seq'::regclass);


--
-- Name: board_boardname_key; Type: CONSTRAINT; Schema: public; Owner: alumnodb; Tablespace: 
--

ALTER TABLE ONLY boards
    ADD CONSTRAINT board_boardname_key UNIQUE (boardname);


--
-- Name: board_pkey; Type: CONSTRAINT; Schema: public; Owner: alumnodb; Tablespace: 
--

ALTER TABLE ONLY boards
    ADD CONSTRAINT board_pkey PRIMARY KEY (boardid);


--
-- Name: scores_pkey; Type: CONSTRAINT; Schema: public; Owner: alumnodb; Tablespace: 
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT scores_pkey PRIMARY KEY (gameid);


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: public; Owner: alumnodb; Tablespace: 
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT user_pkey PRIMARY KEY (playerid);


--
-- Name: user_username_key; Type: CONSTRAINT; Schema: public; Owner: alumnodb; Tablespace: 
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT user_username_key UNIQUE (username);


--
-- Name: mensajes_destinatario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alumnodb
--

ALTER TABLE ONLY mensajes
    ADD CONSTRAINT mensajes_destinatario_fkey FOREIGN KEY (destinatario) REFERENCES usuarios(username);


--
-- Name: mensajes_remitente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alumnodb
--

ALTER TABLE ONLY mensajes
    ADD CONSTRAINT mensajes_remitente_fkey FOREIGN KEY (remitente) REFERENCES usuarios(playerid);


--
-- Name: scores_boardid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alumnodb
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT scores_boardid_fkey FOREIGN KEY (boardid) REFERENCES boards(boardid);


--
-- Name: scores_playerid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alumnodb
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT scores_playerid_fkey FOREIGN KEY (playerid) REFERENCES usuarios(playerid);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

