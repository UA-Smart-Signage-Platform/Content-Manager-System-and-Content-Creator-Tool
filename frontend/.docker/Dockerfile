FROM node:22-alpine

# Install pnpm
RUN npm install -g pnpm

# Create and define the node_modules's cache directory.
WORKDIR /cache

# install app dependencies
COPY package.json .
COPY pnpm-lock.yaml .
RUN pnpm install --silent

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY . .