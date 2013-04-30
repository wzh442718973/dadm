<?php
    session_start();
    
    define("PGUSER", "alumnodb");
    define("PGPASSWORD", "alumnodb");
    define("DSN", "pgsql:host=127.0.0.1;dbname=chachacha;options='--client_encoding=UTF8'");

    try {
        $db = new PDO(DSN, PGUSER, PGPASSWORD);
        
        $stmt = $db->prepare("SELECT boardname, board  FROM boards;");
        $stmt->execute();
        
        if (isset($_REQUEST["json"])) {
            $rr = array();
            for ($i = 0; $row=$stmt->fetch(PDO::FETCH_ASSOC) ; ++$i) {
                $rr[] = $row;
            }
            print json_encode($rr);    
        }
        else {
            $xml = new SimpleXMLElement('<xml/>');

            for ($i = 0; $row=$stmt->fetch() ; ++$i) {
                $figura = $xml->addChild('figura');
                $figura->addChild('nombre', $row['boardname']);
                $figura->addChild('valor', $row['board']);
            }

            Header('Content-type: text/xml');
            print($xml->asXML());
        }       
    } catch (PDOException $e) {
        echo "Error";
    }

    $db = null;
    
?>
