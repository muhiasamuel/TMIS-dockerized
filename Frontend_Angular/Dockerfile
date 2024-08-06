# Use a more recent Node.js runtime as a parent image
FROM node:18-alpine AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install --legacy-peer-deps

# Copy the rest of the application code to the working directory
COPY . .

# Run Angular compiler to build the project
RUN npx ngcc --properties es2023 browser module main --first-only --create-only --create-ivy--entry-points
RUN npm run build

# Verify the contents of the build directory
RUN ls -l /app/dist/tmis

# Use a lightweight web server to serve the Angular application
FROM nginx:stable

# Copy custom Nginx configuration
COPY default.conf /etc/nginx/conf.d

# Copy the built application from the previous stage to the Nginx web server
COPY --from=build /app/dist/tmis/browser /usr/share/nginx/html/browser

# Expose the port on which the app will run
EXPOSE 80