#!/bin/bash

# Protects script from continuing with an error
set -eu -o pipefail

# Ensures environment variables are set
echo "Setting environment variables..."
export DOCKER_INFLUXDB_INIT_MODE=$DOCKER_INFLUXDB_INIT_MODE
export DOCKER_INFLUXDB_INIT_USERNAME=$DOCKER_INFLUXDB_INIT_USERNAME
export DOCKER_INFLUXDB_INIT_PASSWORD=$DOCKER_INFLUXDB_INIT_PASSWORD
export DOCKER_INFLUXDB_INIT_ORG=$DOCKER_INFLUXDB_INIT_ORG
export DOCKER_INFLUXDB_INIT_BUCKET=$DOCKER_INFLUXDB_INIT_BUCKET
export DOCKER_INFLUXDB_INIT_RETENTION=$DOCKER_INFLUXDB_INIT_RETENTION
export DOCKER_INFLUXDB_INIT_ADMIN_TOKEN=$DOCKER_INFLUXDB_INIT_ADMIN_TOKEN
export DOCKER_INFLUXDB_INIT_PORT=$DOCKER_INFLUXDB_INIT_PORT
export DOCKER_INFLUXDB_INIT_HOST=$DOCKER_INFLUXDB_INIT_HOST

echo "Environment variables set."

# Set the new bucket name
NEW_BUCKET_NAME="your_new_bucket_name"

# Conducts initial InfluxDB setup using the CLI
echo "Running influx setup..."
influx setup --skip-verify --bucket ${DOCKER_INFLUXDB_INIT_BUCKET} --retention ${DOCKER_INFLUXDB_INIT_RETENTION} --token ${DOCKER_INFLUXDB_INIT_ADMIN_TOKEN} --org ${DOCKER_INFLUXDB_INIT_ORG} --username ${DOCKER_INFLUXDB_INIT_USERNAME} --password ${DOCKER_INFLUXDB_INIT_PASSWORD} --host http://${DOCKER_INFLUXDB_INIT_HOST}:8086 --force

echo "Influx setup completed."

# Create a new bucket
echo "Creating new bucket..."
influx bucket create --name=${NEW_BUCKET_NAME} --org=${DOCKER_INFLUXDB_INIT_ORG} --token=${DOCKER_INFLUXDB_INIT_ADMIN_TOKEN} --host http://${DOCKER_INFLUXDB_INIT_HOST}:8086

echo "New bucket creation completed."
