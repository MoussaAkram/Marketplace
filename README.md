# Marketplace Platform

An innovative online marketplace platform built using Spring Boot, React with Vite, and MySQL, designed to provide a scalable, secure, and user-friendly experience for both buyers and sellers. This project addresses common challenges in e-commerce, such as user customization, data security, and platform scalability.

## Table of Contents

    - [Project Overview](#project-overview)
    - [Challenges Addressed](#challenges-addressed)
    - [System Roles](#system-roles)
    - [Core Functionalities](#core-functionalities)
    - [Technology Stack](#technology-stack)
    - [Getting Started](#getting-started)
    - [Contributing](#contributing)
    
## Project Overview

With the rapid growth of e-commerce, online marketplaces must constantly innovate to remain competitive while providing a seamless user experience. This project seeks to develop a marketplace capable of handling increasing demand while ensuring data security, user personalization, and reliability in transactions.

## Challenges Addressed

Despite technological advancements, online marketplaces face challenges such as:

  - Scalability and Reliability: Complex systems managing interactions between buyers and sellers can reduce transaction reliability.
  - Data and Payment Security: Ensuring secure transactions is essential, as breaches can significantly impact user trust.
  - Customizability: Tailoring the platform to meet specific customer needs while managing rapid growth can be complex.
This project aims to overcome these challenges by creating a scalable, secure, and user-friendly platform that maintains performance as the user base grows.

## System Roles

This marketplace platform serves the following key user roles:
    - Visitor: A site visitor who can browse and search for products without needing to register.
    - Client (Buyer): A registered user who can place orders, check order status, and rate products.
    - Seller: A vendor who lists products and fulfills customer orders.
    - Administrator: Responsible for platform maintenance, user management, and regulatory compliance.

## Core Functionalities

Below are the main features of the marketplace platform, organized by user role.

### Visitor Capabilities
    - Browse Products: Explore available products and view vendor information.
    - Account Creation: Register on the platform to gain access to additional features.
    - Search: Find specific products or vendors.
### Client (Buyer) Capabilities
    - Cart Management: Add, modify, and remove products before placing an order.
    - Place Orders: Select products and provide necessary information to complete a purchase.
    - Product Rating and Review: Leave ratings and reviews for purchased items.
    - Update Personal Information: Edit personal details like address and phone number.
### Seller Capabilities
    - Add and Manage Products: Create, update, or remove product listings.
    - Order Management: Track and process orders placed by clients.
    - Update Store Information: Edit personal and store details as needed.
### Administrator Capabilities
    - User Account Management: Monitor and manage user accounts, process account deletion requests, and handle security issues.
    - Product Management: Oversee product-related functionalities across the platform.
    - Customer Complaint Handling: Receive, process, and resolve customer complaints.

## Technology Stack

This project leverages the following technologies:
    - Backend: Spring Boot – for handling business logic, API endpoints, and data transactions.
    - Frontend: React with Vite – for fast, interactive, and dynamic user interfaces.
    - Database: MySQL – for secure, relational data storage.

## Getting Started

Follow these instructions to set up the project locally.

### Prerequisites
    - Java: Ensure Java is installed for running Spring Boot.
    - Node.js & npm: Required for running React with Vite.
    - MySQL: For database setup.

### Installation
    1. Clone the repository:
        ```bash
        git clone https://github.com/MoussaAkram/marketplace.git
        cd marketplace
        ```

    2. Set up the database:
        - Configure a MySQL database with the required tables.
        - Update the Spring Boot application.properties file with your database credentials.

    3. Run the frontend:
        ```bash
            cd frontend
            npm install
        ```
        - Create file .env and set your environment variables
        ```bash
            npm run dev
        ```

    4. Access the platform: Open your browser and navigate to http://localhost:5173.

## Contributing

We welcome contributions! Please follow these steps to contribute:
    1. Fork the repository.
    2. Create a new branch (git checkout -b feature/YourFeature).
    3. Commit your changes (git commit -m 'Add new feature').
    4. Push to the branch (git push origin feature/YourFeature).
    5. Open a pull request.
## License

This project is licensed under the Apache License 2.0. See the [Apache License](https://github.com/MoussaAkram/Marketplace/blob/main/LICENSE) file for more details.
