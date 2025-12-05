FROM nginx:alpine

# Instalamos openssl para generar el certificado
RUN apk add --no-cache openssl

# Carpeta para el certificado
RUN mkdir -p /etc/nginx/ssl

# Generamos un certificado autofirmado sencillo
# CN=example.com para probar con un dominio "realista"
RUN openssl req -x509 -nodes -days 365 \
    -subj "/C=ES/ST=POC/L=POC/O=POC/OU=POC/CN=example.com" \
    -newkey rsa:2048 \
    -keyout /etc/nginx/ssl/selfsigned.key \
    -out /etc/nginx/ssl/selfsigned.crt

# Copiamos la config de nginx
COPY default.conf /etc/nginx/conf.d/default.conf

# Copiamos el HTML
COPY index.html /usr/share/nginx/html/index.html
