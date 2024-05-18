FROM influxdb:2.0.4
COPY ./entrypoint.sh /docker-entrypoint-initdb.d
RUN chmod +x /docker-entrypoint-initdb.d/entrypoint.sh