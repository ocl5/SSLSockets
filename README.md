
1 - Primero generamos el certificado, la public/private key y las almacenamos en un fichero     keystore
keytool -genkey -keyalg RSA -keysize 2048 -validity 360 -alias mykey -keystore myKeyStore.jks

2 - Exportamos el certificado con la clave publica que debe se renviada al cliente
keytool -export -alias mykey -keystore myKeyStore.jks -file mykey.cert

3 - Añadimos la clave en el lado del cliente en la TrustedStore para verificar el servidor
keytool -import -file mykey.cert -alias mykey -keystore myTrustStore.jts


########        VERSIÓN SECUENCIAL        ###########
# Solo atenderemos a un cliente de forma simumtánea #

4a - Compilamos el Servidor
javac Server.java

5a - Compilamos el Cliente
javac Client.java

6a - Ejecutamos el servidor en una terminal (especificamos el puerto de escucha)
java Server 8000

7a - Ejecutamos el cliente en otra terminal (especificamos la IP y el puerto)
java Client localhost 8000

########        VERSIÓN CONCURRENTE       ###########
# Varios cliente de forma simulánea mediante hilos  #
4b - Compilamos el Servidor
javac ConcurrentServer.java

5b - Compilamos el Cliente
javac Client.java

6b - Ejecutamos el servidor en una terminal (especificamos el puerto de escucha)
java ConcurrentServer 8000

7b - Ejecutamos el cliente en otra terminal (especificamos la IP y el puerto)
java Client localhost 8000

8b - Podemos poner a otro cliente a solicitar servicios en otra terminal si lo deseamos.


===============================
Version cifrada vs no cifrada =
===============================
Tanto la versión secuencial como en la concurrente están codificadas de forma que se puede
variar entre estas dos comentando las lineas en las que se indica.
Esta codificación y negociación se puede rastrear fácilmente en Wireskark mediante el filtro:
tcp.port == 8000

