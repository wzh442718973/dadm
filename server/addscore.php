<?php
    session_start();
    
    define("PGUSER", "alumnodb");
    define("PGPASSWORD", "alumnodb");
    define("DSN", "pgsql:host=127.0.0.1;dbname=chachacha;options='--client_encoding=UTF8'");

    if (!isset($_REQUEST["duration"]) || !isset($_REQUEST["numberoftiles"]) ||
            !isset($_REQUEST["date"]) || !isset($_REQUEST["board"]) || 
            !isset($_REQUEST["playerid"])) {
        echo "-1";
        return;
    }

    $playerid = $_REQUEST["playerid"];
    $duracion = $_REQUEST["duration"];
    $npiezas  = $_REQUEST["numberoftiles"];
    $date     = $_REQUEST["date"];
    $board    = $_REQUEST["board"];

    try {
        $db = new PDO(DSN, PGUSER, PGPASSWORD);

        $stat = $db->query("select getboardid('".$board."');");
        $boardid = $stat->fetch();
        $boardid = $boardid[0];

        $sql = "INSERT INTO scores (playerid,boardid,npiezas,".
                "fecha,duracion) VALUES ('".
                    $playerid."',".$boardid.",".$npiezas.",'".$date."',".$duracion.
                     ");";
        $ok = $db->exec($sql);
        
        if ($ok==1) {
            echo $playerid;
        }
        else {
           echo -1;
        }
    }
    catch (PDOException $e) {
        echo "-1";
    }

    $db = null;
    
?>
