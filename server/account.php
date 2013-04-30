<?php
    session_start();
    
    define("PGUSER", "alumnodb");
    define("PGPASSWORD", "alumnodb");
    define("DSN", "pgsql:host=localhost;dbname=chachacha;options='--client_encoding=UTF8'");

    if (!isset($_REQUEST["playername"]) || !isset($_REQUEST["playerpassword"])) {
        echo "-1";
        return;
    }

    $username = $_REQUEST["playername"];
    $password = $_REQUEST["playerpassword"];
    $login = isset($_REQUEST["login"]);

    try {
        $db = new PDO(DSN, PGUSER, PGPASSWORD);

        $stmt = $db->prepare("SELECT playerid, password FROM usuarios WHERE " .
                "username = :username");
        $stmt->bindParam(':username', $username, PDO::PARAM_STR);
        $stmt->execute();
        if ($stmt->rowCount()==0 && !$login) {
            $ok = $db->exec("INSERT INTO usuarios (playerid,username,password) VALUES (".
                    "uuid_generate_v4(),'".$username."', '".$password."');");
            if ($ok==1) {
                $stmt->execute();
                $linea = $stmt->fetch();
                $_SESSION['playerid']=$linea['playerid'];
                echo $linea['playerid'];
            }
            else {
                echo '-1';
            }
        }
        else if ($stmt->rowCount()==1 && $login) {
            $linea = $stmt->fetch();
            if (strcmp($linea['password'],$password)==0) {
                $_SESSION['playerid']=$linea['playerid'];
                echo $linea['playerid'];
            }
            else {
                echo '-1';
            }
            
        }
        else {
            echo '-1';
        }
    }
    catch (PDOException $e) {
        echo "-1";
    }

    $db = null;
    
?>
