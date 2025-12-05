docker build -t poc-ssl-client .
docker run -it --rm --add-host=example.com:host-gateway poc-ssl-client sh
java -cp src app.poc.SSLConection https://example.com:8443/
