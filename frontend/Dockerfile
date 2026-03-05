FROM nginx:alpine

# Borramos la página por defecto de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copiamos el frontend a la carpeta pública del servidor web
COPY ./ /usr/share/nginx/html/

# Exponemos el puerto 80 (el estándar web)
EXPOSE 80
