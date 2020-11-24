
1 - Primero generamos el certificado, la public/private key y las almacenamos en un fichero     keystore
keytool -genkey -keyalg RSA -keysize 2048 -validity 360 -alias mykey -keystore myKeyStore.jks

2 - Exportamos el certificado con la clave publica que debe se renviada al cliente
keytool -export -alias mykey -keystore myKeyStore.jks -file mykey.cert

3 - Añadimos la clave en el lado del cliente en la TrustedStore para verificar el servidor
keytool -import -file mykey.cert -alias mykey -keystore myTrustStore.jts

4 - Compilamos el Servidor
javac Server.java

5 - Compilamos el Cliente
javac Client.java

6 - Ejecutamos el servidor en una terminal (especificamos el puerto de escucha)
java Server 8000

7 - Ejecutamos el cliente en otra terminal (especificamos la IP y el puerto)
java Client localhost 8000
