docker build -t poc-https-selfsigned .
docker run --rm -p 8443:443 -p 8080:80 --name poc-nginx-selfsigned poc-https-selfsigned