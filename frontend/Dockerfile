FROM node:14.15.4-slim

# Set directory
WORKDIR /frontend

# Copy dependencies
COPY package.json package.json
RUN npm install

# Copy the rest of the frontend
COPY . .

CMD ["npm", "run", "dev"]