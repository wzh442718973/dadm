<?php
    session_start();
    
    define("PGUSER", "alumnodb");
    define("PGPASSWORD", "alumnodb");
    define("DSN", "pgsql:host=localhost;dbname=chachacha;options='--client_encoding=UTF8'");

    try {

        $db = new PDO(DSN, PGUSER, PGPASSWORD);
        
        $bn = isset($_REQUEST["boardname"]) ? $_REQUEST["boardname"] : 'completo';
        
        $stmt = $db->prepare("SELECT * FROM topten('".$bn."');");
        $stmt->execute();
        
        if (isset($_REQUEST["json"])) {
            $rr = array();
            while ($row=$stmt->fetch(PDO::FETCH_ASSOC)) {
                $rr[] = $row;
            }

            print json_encode($rr);
        }
        else {
            $xml = new SimpleXMLElement('<xml/>');

            for ($i = 0; $row=$stmt->fetch() ; ++$i) {
                $figura = $xml->addChild('score');
                $figura->addChild('username', $row['usuario']);
                $figura->addChild('npiezas', $row['npiezas']);
                $figura->addChild('duracion', $row['duracion']);
                $figura->addChild('fecha', $row['fecha']);
            }

            Header('Content-type: text/xml');
            print($xml->asXML());
        }
    } catch (PDOException $e) {
        echo "Error";
        echo $e;
    }

    $db = null;
    
?>
