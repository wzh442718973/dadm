<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>

        <a href="account.php?playername=go&playerpassword=go">account</a><br/>
        <a href="topten.php?boardname=completo">topten para el tablero completo en XML</a><br/>
        <a href="topten.php?boardname=completo&json">topten para el tablero completo en JSON</a><br/>
        <a href="addscore.php?playerid=c93288f1-dc55-40ce-8fca-b22e9d983ccc&board=completo&duration=1223&numberoftiles=3&date=31-12-2012">addscore</a> <br/>
        <a href="figures.php">figures en XML</a><br/>
        <a href="figures.php?json">figures en JSON</a><br/>
        <br>
        <br>
        Funciones para enviar mensajes de un usuario a otro:
        <ul>
            <li>Enviar: El siguiente enlace envia el mensaje hola al usuario 'gogogogo' desde el usuario
                con código playerid (longitud máxima 140 chars):
                <a href="http://ptha.ii.uam.es/chachacha/sendmsg.php?playerid=c93288f1-dc55-40ce-8fca-b22e9d983ccc&to=gogogogo&msg=hola">enviar</a>
            </li>
            <li>Recibir: El siguiente enlace recupera los mensajes no leídos para 'gogogogo' con su playerid:
                <a href="http://ptha.ii.uam.es/chachacha/getmsgs.php?playerid=b1f056ce-5cfd-4b1f-b64a-aebed21b71f5">recuperar</a> 
                Si se añade el parámetro 'markasread' se marcaran los mensajes como leídos y no se recuperarán de nuevo en la siguiente petición.
            </li>
        </ul>

    </body>
</html>
